package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableObjectValue;

/**
 * ObserverLocationBean
 *
 * Bean Java FX contenant la position de l'observateur, en degrés.
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
public final class ObserverLocationBean {

    // Longitude de la position de l'observateur, en degrés
    private final DoubleProperty lonDeg = new SimpleDoubleProperty();
    // Latitude de la position de l'observateur, en degrés
    private final DoubleProperty  latDeg = new SimpleDoubleProperty();
    // Longitude et latitude combinées en une instance de GeographicCoordinates.
    private final ObservableObjectValue <GeographicCoordinates> coordinates = Bindings.createObjectBinding(
            () -> GeographicCoordinates.ofDeg(lonDeg.getValue(), latDeg.getValue()),
            lonDeg, latDeg);


    public DoubleProperty lonDegProperty() {
        return this.lonDeg;
    }

    /**
     * Getteur de la longitude en degrés.
     * @return
     */
    public Double getLonDeg() {
        return this.lonDeg.get();
    }

    /**
     * Setteur de la longitude.
     * @param d
     */
    public void setLonDeg(double d) {
        this.lonDeg.setValue(d);
    }

    public DoubleProperty latDegProperty() {
        return this.latDeg;
    }

    /**
     * Getteur de la latitude en degrés.
     * @return
     */
    public Double getLatDeg() {
        return this.latDeg.get();
    }

    /**
     * Setteur de la latitude.
     * @param d
     */
    public void setLatDeg(double d) {
        this.latDeg.setValue(d);
    }

    public ObservableObjectValue<GeographicCoordinates> coordinatesProperty() {
        return coordinates;
    }

    /**
     * Getteur de la latitude et longitude sous forme de coordonnées géographiques.
     * @return
     */
    public GeographicCoordinates getCoordinates() {
        return this.coordinates.get();
    }

    /**
     * Setteur de la latitude et longitude sous forme de coordonnées géographiques.
     * @param gc
     */
    public void setCoordinates(GeographicCoordinates gc) {
        this.lonDeg.setValue(gc.lonDeg());
        this.latDeg.setValue(gc.latDeg());
    }
}

