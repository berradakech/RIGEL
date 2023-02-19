package ch.epfl.rigel.gui;


import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static javafx.beans.binding.Bindings.*;
import static javafx.collections.FXCollections.observableList;

/**
 * Main :  classe contenant le programme principal.
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
public final class Main extends Application {

    // Constante pour les boutons
    private final static String BOUTON_RESET = "\uf0e2";
    private final static String BOUTON_PLAY = "\uf04b";
    private final static String BOUTON_PAUSE = "\uf04c";

    //Attributs utilisables a la fois dans l'instant d'observation et dans l'ecoulement du temps.
    private final static DatePicker DATE_PICKER = new DatePicker(LocalDate.now());
    private final static TextField HEURE_TEXT_FIELD = new TextField();
    private final static List<ZoneId> ZONE_IDS = ZoneId.getAvailableZoneIds().stream().sorted().map(ZoneId::of).collect(Collectors.toUnmodifiableList());
    private final static ComboBox<ZoneId> ZONE_ID_COMBO_BOX = new ComboBox<>(observableList(ZONE_IDS));

    /**
     * Methode main
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Methode start
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        try (InputStream hyg = resourceStream("/hygdata_v3.csv");InputStream hs2 = resourceStream("/asterisms.txt")) {
            StarCatalogue catalogue = new StarCatalogue.Builder()
                    .loadFrom(hyg, HygDatabaseLoader.INSTANCE)
                    .loadFrom(hs2, AsterismLoader.INSTANCE)
                    .build();

            //Declaration des Beans
            ObserverLocationBean observerLocationBean = new ObserverLocationBean();
            DateTimeBean dateTimeBean = new DateTimeBean();
            ViewingParametersBean viewingParametersBean = new ViewingParametersBean();

            //Initialisation des propriétés avec les valeurs de l'énoncé
            observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(6.57, 46.52));
            dateTimeBean.setZonedDateTime(ZonedDateTime.now());
            viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 15));
            viewingParametersBean.setFieldOfViewDeg(100);


            //Initialisation de notre stage
            primaryStage.setTitle("Rigel");
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            //Creation de la barre de controle au moyen de la methode auxiliaire barreControle
            HBox barreDeControle = barreControle(dateTimeBean, observerLocationBean);


            //Dessin du ciel
            SkyCanvasManager manager = new SkyCanvasManager(catalogue, dateTimeBean, observerLocationBean, viewingParametersBean);
            Canvas canvas = manager.canvas();
            Pane paneCiel = new Pane(canvas);
            canvas.heightProperty().bind(paneCiel.heightProperty());
            canvas.widthProperty().bind(paneCiel.widthProperty());


            //Creation de la barre d'information au moyen de la methode auxiliaire barreInfo
            BorderPane barreInfo = barreInfo(viewingParametersBean.fieldOfViewDegProperty(), manager);

            //Creation du pane principal, prenant ses enfants dans son constructeur
            BorderPane panePrincipal = new BorderPane(paneCiel, barreDeControle, null, barreInfo, null);


            primaryStage.setScene(new Scene(panePrincipal));
            primaryStage.show();
            canvas.requestFocus();
        }
    }

    /**
     * Permet l'extraction d'un fichier
     *
     * @param resourceName Nom du fichier
     * @return lecture du fichier
     */
    private InputStream resourceStream(String resourceName) {
        return getClass().getResourceAsStream(resourceName);
    }

    /**
     * Methode qui permet de creer  notre barre d'information.
     *
     * @param champDeVuePropriete Propriete champ de vue du viewingParametersBean
     * @param manager             SkyCanvasManager
     * @return la barre d information
     */
    private BorderPane barreInfo(DoubleProperty champDeVuePropriete, SkyCanvasManager manager) {

        Text champDeVue = new Text();
        champDeVue.textProperty().bind(format(Locale.ROOT, "Champ de vue :%.1f", champDeVuePropriete));

        Text objetCelesteLePlusProche = new Text();
        objetCelesteLePlusProche.textProperty().bind(Bindings.createStringBinding(() -> {
            var objetLePlusProche = manager.objectUnderMouseProperty().get();
            return objetLePlusProche == null ? "" : objetLePlusProche.info();

        }, manager.objectUnderMouseProperty()));

        Text sourisCoord = new Text();
        sourisCoord.textProperty().bind(format(Locale.ROOT, "Azimut : %.2f ° , hauteur : %.2f °", manager.mouseAzDegProperty(), manager.mouseAltDegProperty()));


        BorderPane barreInfo = new BorderPane(objetCelesteLePlusProche, null, sourisCoord, null, champDeVue);
        barreInfo.setStyle("-fx-padding: 4; -fx-background-color: white;");

        return barreInfo;
    }

    /**
     * Methode qui permet de créer le prenier enfant de notre barre de contrôle : la position d'observation
     *
     * @param longitudePropriete Propriete longitude  de observationLocationBean
     * @param latitudePropriete  Propriete latitude de observationLocationBean
     * @return la  position d'observation
     */
    private HBox positionObs(DoubleProperty longitudePropriete, DoubleProperty latitudePropriete) {
        // Définition des labels
        Label lonLabel = new Label("Longitude (°):");
        Label latLabel = new Label("Latitude (°):");

        //Définition des champs de text où seront saisies les coordonnées géographiques
        TextFormatter<Number> lonFormatter = convertisseurLonLat(new NumberStringConverter("#0.00"), GeographicCoordinates::isValidLonDeg);
        TextField lonTextField = new TextField();
        lonTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        lonTextField.setTextFormatter(lonFormatter);

        // Lien entre olb et lon
        lonFormatter.valueProperty().bindBidirectional(longitudePropriete);


        TextFormatter<Number> latFormatter = convertisseurLonLat(new NumberStringConverter("#0.00"), GeographicCoordinates::isValidLatDeg);
        TextField latTextField = new TextField();
        latTextField.setStyle("-fx-pref-width: 60; -fx-alignment: baseline-right;");
        latTextField.setTextFormatter(latFormatter);

        // Lien entre olb et lat
        latFormatter.valueProperty().bindBidirectional(latitudePropriete);

        // Définition du panneau 'Position d'observation' : enfant 1
        HBox positionObservation = new HBox();
        positionObservation.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");
        positionObservation.getChildren().addAll(lonLabel, lonTextField, latLabel, latTextField);

        return positionObservation;
    }

    /**
     * Methode qui permet de créer le deuxieme enfant de notre barre de contrôle : l'instant d'observation
     *
     * @param datePropriete   Propriete date  de dateTimeBean
     * @param tempsPropriete  Propriete time  de dateTimeBean
     * @param fuseauPropriete Propriete zone  de dateTimeBean
     * @return l'instant d'observation
     */
    private HBox instantObs(ObjectProperty<LocalDate> datePropriete,
                            ObjectProperty<LocalTime> tempsPropriete,
                            ObjectProperty<ZoneId> fuseauPropriete) {

        //Définition des labels
        Label dateLabel = new Label("Date:");
        Label heureLabel = new Label("Heure:");

        //Sélectionneur de la date : Calendrier
        DATE_PICKER.setStyle("-fx-pref-width: 120;");

        // Lien entre dtb et datepicker
        DATE_PICKER.valueProperty().bindBidirectional(datePropriete);


        //Champ de text pour saisir l'heure
        TextFormatter<LocalTime> timeFormatter = HeureConverter(DateTimeFormatter.ofPattern("HH:mm:ss"));
        HEURE_TEXT_FIELD.setStyle("-fx-pref-width: 75; -fx-alignment: baseline-right;");
        HEURE_TEXT_FIELD.setTextFormatter(timeFormatter);
        HEURE_TEXT_FIELD.setTextFormatter(timeFormatter);

        // Lien entre dtb et time
        timeFormatter.valueProperty().bindBidirectional(tempsPropriete);

        //Style fuseau horraire
        ZONE_ID_COMBO_BOX.setStyle("-fx-pref-width: 180;");

        //lien fuseau et la propriete fuseau du dateTineBean
        ZONE_ID_COMBO_BOX.valueProperty().bindBidirectional(fuseauPropriete);

        //Définition du panneau 'Instant d'observation'
        HBox instantObservation = new HBox(dateLabel, DATE_PICKER, heureLabel, HEURE_TEXT_FIELD, ZONE_ID_COMBO_BOX);
        instantObservation.setStyle("-fx-spacing: inherit; -fx-alignment: baseline-left;");

        return instantObservation;

    }

    /**
     * Methode qui permet de créer le troisieme enfant de notre barre de contrôle : l'ecoulement du temps
     *
     * @param dtb Notre dateTimeBean
     * @return l'ecoulement du temps
     */
    private HBox ecoulementTemps(DateTimeBean dtb) {
        try (InputStream fontStream = resourceStream("/Font Awesome 5 Free-Solid-900.otf")) {

            //Définition de l'accélérateur de temps
            List<NamedTimeAccelerator> listAccelerateurs = new ArrayList<>();
            Collections.addAll(listAccelerateurs, NamedTimeAccelerator.values());

            ChoiceBox<NamedTimeAccelerator> accelerateur = new ChoiceBox<>();
            accelerateur.setItems(FXCollections.observableArrayList(listAccelerateurs));

            accelerateur.setValue(NamedTimeAccelerator.TIMES_300);

            TimeAnimator animateurTemps = new TimeAnimator(dtb);

            animateurTemps.acceleratorProperty().bind(select(accelerateur.valueProperty(), "accelerateur"));


            //Bouton de réinitialisation
            Font policeBouton = Font.loadFont(fontStream, 15);
            Button resetButton = new Button(BOUTON_RESET);

            resetButton.setOnAction(e -> {
                dtb.setZonedDateTime(ZonedDateTime.now());
            });
            resetButton.setFont(policeBouton);

            //Bouton de démarrage
            Button playButton = new Button();
            playButton.textProperty().setValue(BOUTON_PLAY);
            playButton.setFont(policeBouton);

            playButton.textProperty().bind(
                    when(animateurTemps.runningProperty())
                            .then(BOUTON_PAUSE)
                            .otherwise(BOUTON_PLAY)
            );

            DATE_PICKER.disableProperty().bind(animateurTemps.runningProperty());
            HEURE_TEXT_FIELD.disableProperty().bind(animateurTemps.runningProperty());
            ZONE_ID_COMBO_BOX.disableProperty().bind(animateurTemps.runningProperty());
            resetButton.disableProperty().bind(animateurTemps.runningProperty());


            playButton.setOnAction(e -> {


                if (animateurTemps.isRunning()) {
                    animateurTemps.stop();

                } else {
                    animateurTemps.start();
                }

            });

            //Définition du panneau Ecoulement
            HBox ecoulement = new HBox(accelerateur, resetButton, playButton);
            ecoulement.setStyle("-fx-spacing: inherit;");

            return ecoulement;


        } catch (Exception e) {
            return null;
        }

    }

    /**
     * Methode qui construit la  barre de controle en apellant les 3 methodes auxiliaires construisant ses enfants
     *
     * @param dtb Notre dateTimeBean
     * @param olb Notre ObserverLocationBean
     * @return la barre  de controle
     */
    private HBox barreControle(DateTimeBean dtb, ObserverLocationBean olb) {
        HBox positionObservation = positionObs(olb.lonDegProperty(), olb.latDegProperty());
        HBox instantObservation = instantObs(dtb.dateProperty(), dtb.timeProperty(), dtb.zoneProperty());
        HBox ecoulement = ecoulementTemps(dtb);
        Separator separator1 = new Separator(Orientation.VERTICAL);
        Separator separator2 = new Separator(Orientation.VERTICAL);

        HBox barreDeControle = new HBox(positionObservation, separator1, instantObservation, separator2, ecoulement);
        barreDeControle.setStyle("-fx-spacing: 4; -fx-padding: 4;");

        return barreDeControle;

    }

    /**
     * Methode pernettant de verifier la validité des valeurs  : nombres à deux décimales et compris dans les bons intervalles
     *
     * @param stringConverter
     * @param condition       Predicat permettant d'utiliser isValidLonDeg pour la longitude et  isValidLatDeg pour la latitude
     * @return
     */
    private TextFormatter<Number> convertisseurLonLat(NumberStringConverter stringConverter, Predicate<Double> condition) {
        UnaryOperator<TextFormatter.Change> coordinateFilter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newCoordinate = stringConverter.fromString(newText).doubleValue();
                return condition.test(newCoordinate)
                        ? change
                        : null;
            } catch (Exception e) {
                return null;
            }
        });
        TextFormatter<Number> coordinateTextFormatter = new TextFormatter<>(stringConverter, 0, coordinateFilter);
        return coordinateTextFormatter;
    }


    /**
     * Formatteur de texte pour verifier la validité des heures.
     *
     * @param hmsFormatter
     * @return
     */
    private TextFormatter<LocalTime> HeureConverter(DateTimeFormatter hmsFormatter) {
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        TextFormatter<LocalTime> timeFormatter = new TextFormatter<>(stringConverter);
        return timeFormatter;
    }
}