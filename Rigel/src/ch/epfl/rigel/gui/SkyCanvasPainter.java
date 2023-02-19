
package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.Asterism;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.Planet;
import ch.epfl.rigel.astronomy.Star;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;

import java.util.List;

/**
 * SkyCanvasPainter: Classe qui s'occupe de dessiner notre ciel
 * en fonction des differents parametres (Lieu, date, fuseau..)
 *
 * @author BERRADA EL AZIZ Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
public final class SkyCanvasPainter {

    private Canvas canvas;
    private GraphicsContext ctx;
    private final static ClosedInterval MAGNITUDE_ECRETAGE = ClosedInterval.of(-2, 5);

    /**
     * Constructeur qui initialise le canvas
     * @param canvas
     */
    public SkyCanvasPainter(Canvas canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getGraphicsContext2D();
    }

    /**
     * Methode qui "clear" le canvas en creeant un canvas noir
     */
    public void clear() {
        ctx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
        ctx.setFill(Color.BLACK);
        ctx.fillRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
    }


    /**
     * Dessin et affichage de la lune.
     *
     * @param sky
     * @param projection
     * @param planeToCanvas
     */
    public void drawMoon(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        double tailleAngulaireLune = sky.moon().angularSize();
        double diametreAvantTransformation = projection.applyToAngle(tailleAngulaireLune);

        // Le diamètre à afficher
        double diametreApresTransformation = planeToCanvas.deltaTransform(0, diametreAvantTransformation).magnitude();


        double x = sky.moonPosition().x();
        double y = sky.moonPosition().y();

        Point2D point2D = planeToCanvas.transform(x, y);   //transform(x, y);

        // Methode auxiliaire
        dessin(Color.WHITE, point2D, diametreApresTransformation);
    }

    /**
     * Dessin et affichage du soleil.
     *
     * @param sky
     * @param projection
     * @param planeToCanvas
     */
    public void drawSun(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        double tailleAngulaireSoleil = sky.sun().angularSize();
        double diametreAvantTransformation = projection.applyToAngle(tailleAngulaireSoleil);

        double diametreApresTransformation = planeToCanvas.deltaTransform(0, diametreAvantTransformation).magnitude();

        double x = sky.sunPosition().x();
        double y = sky.sunPosition().y();
        Point2D point2D = planeToCanvas.transform(x, y);

        ctx.setFill(Color.YELLOW);
        ctx.setGlobalAlpha(0.25);
        ctx.fillOval(point2D.getX() - (2.2 * diametreApresTransformation / 2), point2D.getY() - (2.2 * diametreApresTransformation / 2),
                (diametreApresTransformation * 2.2), (diametreApresTransformation * 2.2));

        ctx.setGlobalAlpha(1);
        ctx.fillOval(point2D.getX() - (2.0 + diametreApresTransformation) / 2.0, point2D.getY() - (2.0 + diametreApresTransformation) / 2.0,
                (diametreApresTransformation) + 2.0, (diametreApresTransformation) + 2.0);

        ctx.setFill(Color.WHITE);
        ctx.fillOval(point2D.getX() - (diametreApresTransformation / 2), point2D.getY() - (diametreApresTransformation / 2),
                diametreApresTransformation, diametreApresTransformation);
    }


    /**
     * Dessin et affichage des planètes.
     *
     * @param sky
     * @param projection
     * @param planeToCanvas
     */
    public void drawPlanets(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {
        int i = 0;
        for (Planet p : sky.planets()) {
            double diametreAvantTransformation = diametreDisqueAffiche(p.magnitude(), Angle.ofDeg(0.5), projection);

            // Le diamètre à afficher
            double diametreApresTransformation = planeToCanvas.deltaTransform(0, diametreAvantTransformation).magnitude();

            double x = sky.planetPositions()[i];
            double y = sky.planetPositions()[i + 1];
            Point2D point2D = planeToCanvas.transform(x, y);

            dessin(Color.LIGHTGRAY, point2D, diametreApresTransformation);
            i += 2;
        }
    }

    /**
     * Dessin et affichage de l'horizon et les points cardinaux et intercardinaux.
     *
     * @param projection
     * @param planeToCanvas
     */
    public void drawHorizon(StereographicProjection projection, Transform planeToCanvas) {

        HorizontalCoordinates parallel = HorizontalCoordinates.ofDeg(0, 0);
        CartesianCoordinates centre = projection.circleCenterForParallel(parallel);
        Point2D point2D = planeToCanvas.transform(centre.x(), centre.y());

        double rayonAvantTransformation = projection.circleRadiusForParallel(parallel);
        double diametreApresTransformation = planeToCanvas.deltaTransform(0, rayonAvantTransformation * 2).magnitude();

        ctx.setStroke(Color.RED);
        ctx.setLineWidth(2.0);
        ctx.strokeOval(point2D.getX() - diametreApresTransformation / 2, point2D.getY() - diametreApresTransformation / 2, diametreApresTransformation, diametreApresTransformation);

        for (double i = 0; i < 360.0; i += 45) {
            HorizontalCoordinates horizontalCoordinates = HorizontalCoordinates.ofDeg(i, -0.5);
            CartesianCoordinates cartesianCoordinates = projection.apply(horizontalCoordinates);
            Point2D point2DC = planeToCanvas.transform(cartesianCoordinates.x(), cartesianCoordinates.y());
            String s = horizontalCoordinates.azOctantName("N", "E", "S", "O");

            ctx.setFill(Color.RED);
            ctx.setTextBaseline(VPos.TOP);
            ctx.setTextAlign(TextAlignment.CENTER);
            ctx.fillText(s, point2DC.getX(), point2DC.getY());
        }
    }

    /**
     * Dessin et affichage des etoiles.
     *
     * @param sky
     * @param projection
     * @param planeToCanvas
     */
    public void drawStars(ObservedSky sky, StereographicProjection projection, Transform planeToCanvas) {

        ctx.setLineWidth(1.0);
        ctx.setStroke(Color.BLUE);
        double[] positionsEtoiles = sky.starPosition(); // Creation d un tableau avec les position des etoiles du catalogue
        planeToCanvas.transform2DPoints(positionsEtoiles, 0, positionsEtoiles, 0, positionsEtoiles.length / 2);

        var dansLesBornesDuCanvas = canvas.getBoundsInLocal();

        // Dessin des asterisms.
        for (Asterism a : sky.asterisms()) {

            List<Integer> listIndiceEtoile = sky.starsIndexOfAsterism(a); // Pour chaque asterism, creation d une liste contenant les indices
            // des etoiles de l asterism dans la liste stars()

            Integer etoile0 = listIndiceEtoile.get(0);
            double x0 = positionsEtoiles[2 * etoile0];
            double y0 = positionsEtoiles[2 * etoile0 + 1];

            //variable faisant office de "previous"
            boolean avant = dansLesBornesDuCanvas.contains(x0, y0);

            ctx.beginPath();
            ctx.moveTo(x0, y0);

            for (int i = 1; i < listIndiceEtoile.size(); i++) {
                int indiceEtoile = listIndiceEtoile.get(i);

                // Star s = sky.stars().get(indexEtoile);

                double x = positionsEtoiles[2 * indiceEtoile];
                double y = positionsEtoiles[(2 * indiceEtoile) + 1];


                //variable faisant office de "courant"
                boolean courant = dansLesBornesDuCanvas.contains(x, y);

                if (avant || courant) {
                    ctx.lineTo(x, y);
                } else {
                    ctx.moveTo(x, y);
                }

                avant = courant;

            }

            ctx.stroke();

        }

        //Dessin des etoiles.
        int i = 0;
        for (Star s : sky.stars()) {
            double diametreAvantTransformation = diametreDisqueAffiche(s.magnitude(), Angle.ofDeg(0.5), projection); // verifier angle.ofDeg
            double diametreApresTransformation = planeToCanvas.deltaTransform(0, diametreAvantTransformation).magnitude();

            double x = positionsEtoiles[i];
            double y = positionsEtoiles[i + 1];

            Point2D point2D = new Point2D(positionsEtoiles[i], positionsEtoiles[i+1]);

            dessin(BlackBodyColor.colorForTemperature(s.colorTemperature()),point2D,diametreApresTransformation );
            i += 2;
        }
    }


    /**
     * Calcul de la taille du disque représentant d'un objet céleste basée sur sa magnitude.
     *
     * @param magnitude
     * @param tailleAngulaire
     * @param sp
     * @return
     */
    public double diametreDisqueAffiche(double magnitude, double tailleAngulaire, StereographicProjection sp) {
        double diametre = sp.applyToAngle(tailleAngulaire);
        // Ecretage
        double magnitudeEcret = MAGNITUDE_ECRETAGE.clip(magnitude);
        double f = (99 - 17 * magnitudeEcret) / 140;
        return f * diametre;
    }

    /**
     * Méthode auxiliaire pour le dessin des objets célestes (portion de code commune a beaucoup de methodes)
     *
     * @param color
     * @param centre
     * @param diametreVrai
     */
    private void dessin(Color color, Point2D centre, double diametreVrai) {

        double rayon = diametreVrai / 2;
        ctx.setFill(color);
        ctx.fillOval(centre.getX() - (rayon), centre.getY() - (rayon), diametreVrai, diametreVrai);
    }
}




