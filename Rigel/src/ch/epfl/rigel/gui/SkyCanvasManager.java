package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.CelestialObject;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableObjectValue;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Transform;

import static ch.epfl.rigel.coordinates.HorizontalCoordinates.ofDeg;

/**
 * SkyCanvasManager : Classe qui gere les interactions entre l utilisateur et le canvas.
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
public final class SkyCanvasManager {

    private static final RightOpenInterval INTERVAL_AZ = RightOpenInterval.of(0, 360);
    private static final ClosedInterval INTERVAL_ALT = ClosedInterval.of(5, 90);
    private static final ClosedInterval INTERVAL_MOLETTE = ClosedInterval.of(30, 150);


    private final StarCatalogue starCatalogue;
    private final DateTimeBean dateTimeBean;
    private final ObserverLocationBean observerLocationBean;
    private final ViewingParametersBean viewingParametersBean;
    private final Canvas canvas;

    // Propriete, Liens

    private final ObservableDoubleValue mouseAzDeg;
    private final ObservableDoubleValue mouseAltDeg;

    private final ObjectProperty<Point2D> mousePosition;

    private final ObservableObjectValue<CelestialObject> objectUnderMouse;
    private final ObservableObjectValue<StereographicProjection> projection;
    private final ObservableObjectValue<Transform> planeToCanvas;
    private final ObservableObjectValue<ObservedSky> observedSky;
    private final ObservableObjectValue<HorizontalCoordinates> mouseHorizontalPosition;

    // Constructeur

    public SkyCanvasManager(StarCatalogue sc,
                            DateTimeBean dtb,
                            ObserverLocationBean olb,
                            ViewingParametersBean vpb) {

        this.canvas = new Canvas();
        SkyCanvasPainter peintre = new SkyCanvasPainter(canvas);

        this.starCatalogue = sc;
        this.dateTimeBean = dtb;
        this.observerLocationBean = olb;
        this.viewingParametersBean = vpb;

        // 1) Propriete et Liens
        this.mousePosition = new SimpleObjectProperty<>(Point2D.ZERO);

        this.projection = Bindings.createObjectBinding(
                () -> new StereographicProjection(vpb.getCenter()), vpb.centerProperty());
        this.planeToCanvas = Bindings.createObjectBinding(
                () -> {
                    double facteurDilat = canvas.widthProperty().get() / projection.getValue().applyToAngle(Angle.ofDeg(vpb.getFieldOfViewDeg()));
                    Transform dilatation = Transform.scale(facteurDilat, -facteurDilat);
                    Transform translation = Transform.translate(canvas.widthProperty().get() / 2.0, canvas.heightProperty().get() / 2.0);
                    return translation.createConcatenation(dilatation);
                }, projection, canvas.widthProperty(), canvas.heightProperty(), vpb.fieldOfViewDegProperty());

        this.observedSky = Bindings.createObjectBinding(
                () -> new ObservedSky(dtb.getZonedDateTime(), olb.getCoordinates(), projection.getValue(), this.starCatalogue), dtb.zoneProperty(),
                dtb.timeProperty(), dtb.dateProperty(), olb.coordinatesProperty(), projection);


        this.mouseHorizontalPosition = Bindings.createObjectBinding(
                () -> {
                    try {
                        Point2D point2D = planeToCanvas.getValue().inverseTransform(mousePosition.getValue());
                        return projection.getValue().inverseApply(CartesianCoordinates.of(point2D.getX(), point2D.getY()));
                    } catch (NonInvertibleTransformException exception) {
                        return null;
                    }

                }, planeToCanvas, mousePosition, projection);

        // 2) auditeur (listener) pour être informé des mouvements du curseur de la souris, et stocker sa position dans une propriété
        canvas.setOnMouseMoved(e -> mousePosition.set(new Point2D(e.getX(), e.getY())));

        mouseAzDeg = Bindings.createDoubleBinding(() -> {
            var positionSouris = mouseHorizontalPosition.get();
            return positionSouris == null ? Double.NaN : positionSouris.azDeg();
        }, mouseHorizontalPosition);

        mouseAltDeg = Bindings.createDoubleBinding(() -> {
            var positionSouris = mouseHorizontalPosition.get();
            return positionSouris == null ? Double.NaN : positionSouris.altDeg();
        }, mouseHorizontalPosition);


      objectUnderMouse = Bindings.createObjectBinding(() ->
        {
            try {
                Point2D point2D = planeToCanvas.getValue().inverseTransform(mousePosition.getValue());
                CartesianCoordinates cartesianCoordinates = CartesianCoordinates.of(point2D.getX(), point2D.getY());
                var objetLePlusProche = observedSky.getValue().objectClosestTo(cartesianCoordinates, 10).orElse(null);
                return objetLePlusProche; //Magic number, conseil d Elior.
            } catch (NonInvertibleTransformException exception) {
                return null;
            }
        }, observedSky, mousePosition, planeToCanvas);


        // 3) auditeur pour détecter les clics de la souris sur le canevas et en faire alors le destinataire des événements clavier
        //canvas.setOnMousePressed(e -> );
        canvas.setOnMousePressed(e -> {
            if (e.isPrimaryButtonDown()) {
                canvas.requestFocus();
            }
        });

        // 4) auditeur pour réagir aux mouvements de la molette de la souris et/ou du trackpad et changer le champ de vue en fonction
        canvas.setOnScroll(e -> {
            double valeurMaximalZoom = Math.abs(e.getDeltaX()) > Math.abs(e.getDeltaY()) ? e.getDeltaX() : e.getDeltaY();
            double valeurMaximalZoomClip = INTERVAL_MOLETTE.clip(vpb.getFieldOfViewDeg() + valeurMaximalZoom);
            vpb.setFieldOfViewDeg(valeurMaximalZoomClip);
        });

        // 5) auditeur pour réagir aux pressions sur les touches du curseur et changer le centre de projection en fonction
        canvas.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) {
                double az = INTERVAL_AZ.reduce(vpb.getCenter().azDeg() - 10);
                vpb.setCenter(ofDeg(az, vpb.getCenter().altDeg()));

            } else if (e.getCode() == KeyCode.RIGHT) {
                double az = INTERVAL_AZ.reduce(vpb.getCenter().azDeg() + 10);
                vpb.setCenter(ofDeg(az, vpb.getCenter().altDeg()));

            } else if (e.getCode() == KeyCode.UP) {
                double alt = INTERVAL_ALT.clip(vpb.getCenter().altDeg() + 5);
                vpb.setCenter(ofDeg(vpb.getCenter().azDeg(), alt));

            } else if (e.getCode() == KeyCode.DOWN) {
                double alt = INTERVAL_ALT.clip(vpb.getCenter().altDeg() - 5);
                vpb.setCenter(ofDeg(vpb.getCenter().azDeg(), alt));

            }
            e.consume();
        });


        // 6) installe des auditeurs pour être informé des changements des liens
        // et propriétés ayant un impact sur le dessin du ciel,
        // et demander dans ce cas au peintre de le redessiner.

        observedSky.addListener(o -> dessinDuCiel(peintre));
        planeToCanvas.addListener(o -> dessinDuCiel(peintre));


    }

    /**
     * Accesseur de la propriété objectUnderMouse
     * @return objectUnderMouse
     */
    public ObservableObjectValue<CelestialObject> objectUnderMouseProperty() {
        return this.objectUnderMouse;
    }
    /**
     * Accesseur du canvas
     * @return canvas
     */
    public Canvas canvas() {
        return this.canvas;
    }

    /**
     * Getteur
     * @return
     */
    public Double getMouseAzDeg() {
        return mouseAzDeg.get();
    }

    /**
     * Getteur
     * @return
     */
    public ObservableDoubleValue mouseAzDegProperty() {
        return mouseAzDeg;
    }

    /**
     * Getteur
     * @return
     */
    public Double getMouseAltDeg() {
        return mouseAltDeg.get();
    }

    /**
     * Getteur
     * @return
     */
    public ObservableDoubleValue mouseAltDegProperty() {
        return mouseAltDeg;
    }

    /**
     * Getteur
     * @return
     */
    public ObservableObjectValue<CelestialObject> getObjectUnderMouse() {
        return objectUnderMouse;
    }


    /**
     * Methode qui dessine le ciel, portion de code utilisée plusieurs fois.
     * @param peintre
     */
    public void dessinDuCiel (SkyCanvasPainter peintre){
        StereographicProjection projection = this.projection.get();
        ObservedSky observedSky = this.observedSky.get();
        Transform transform = this.planeToCanvas.get();
        peintre.clear();
        peintre.drawStars(observedSky, projection, transform);
        peintre.drawPlanets(observedSky, projection, transform);
        peintre.drawSun(observedSky, projection, transform);
        peintre.drawMoon(observedSky, projection, transform);
        peintre.drawHorizon(projection, transform);
    }
}


