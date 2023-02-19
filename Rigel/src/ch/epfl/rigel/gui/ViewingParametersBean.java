package ch.epfl.rigel.gui;

import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * ViewingParametersBean
 *
 * Bean Java FX contenant les paramètres déterminant la portion du ciel visible sur l'image
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
public final class ViewingParametersBean {

    // Le champ de vue (en degrés).
    private final DoubleProperty fieldOfViewDeg = new SimpleDoubleProperty();
    // Les coordonnées du centre de projection.
    private final ObjectProperty<HorizontalCoordinates> center = new SimpleObjectProperty<>();

    public DoubleProperty fieldOfViewDegProperty(){
        return this.fieldOfViewDeg;
    }

    /**
     * Getteur du champ de vue.
     * @return
     */
    public Double getFieldOfViewDeg(){
        return this.fieldOfViewDeg.get();
    }

    /**
     * Setteur du champ de vue.
     * @param i
     */
    public void setFieldOfViewDeg(double i){
        this.fieldOfViewDeg.setValue(i);
    }

    public ObjectProperty<HorizontalCoordinates> centerProperty(){
        return this.center;
    }

    /**
     * Getteur des coordonnées du centre.
     * @return
     */
    public HorizontalCoordinates getCenter(){
        return this.center.get();
    }

    /**
     * Setteur des coordonnées du centre.
     * @param cc
     */
    public void setCenter(HorizontalCoordinates cc){
        this.center.setValue(cc);
    }
}



