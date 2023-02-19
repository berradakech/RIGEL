/**
 * Planet
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

public final class Planet extends CelestialObject {


    /**
     * Construit une planete portant le nom name, situé aux coordonnées équatoriales equatorialPos, de taille angulaire angularSize et de magnitude magnitude
     * Lève IllegalArgumentException si la taille angulaire est négative, ou NullPointerException si le nom ou la position équatoriale sont nuls
     *
     * @param name
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     */
    public Planet(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        super(name, equatorialPos, angularSize, magnitude);

    }

}


