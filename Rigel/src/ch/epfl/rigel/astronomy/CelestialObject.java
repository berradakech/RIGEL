/**
 * CelestialObject classe mère des objets celestes
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

import static ch.epfl.rigel.Preconditions.checkArgument;

public abstract class CelestialObject {

    private final String name;
    private final EquatorialCoordinates equatorialPos;
    private final float angularSize;
    private final float magnitude;


    /**
     * Construit un objet céleste portant le nom name, situé aux coordonnées équatoriales equatorialPos, de taille angulaire angularSize et de magnitude magnitude
     * Lève IllegalArgumentException si la taille angulaire est négative, ou NullPointerException si le nom ou la position équatoriale sont nuls
     *
     * @param name
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     */
    CelestialObject(String name, EquatorialCoordinates equatorialPos, float angularSize, float magnitude) {
        checkArgument(angularSize >= 0);

        this.name = Objects.requireNonNull(name);
        this.equatorialPos = Objects.requireNonNull(equatorialPos);
        this.angularSize = angularSize;
        this.magnitude = magnitude;

    }

    /**
     * Getteur du nom de l objet celeste
     *
     * @return name
     */
    public String name() {
        return name;
    }

    /**
     * Getteur de la position equatorial de l objet celeste
     *
     * @return equatorialPos
     */
    public EquatorialCoordinates equatorialPos() {
        return equatorialPos;
    }

    /**
     * Getteur de la taille angulaire de l objet celeste
     *
     * @return angularSize
     */
    public double angularSize() {
        return angularSize;
    }

    /**
     * Getteur de la magnitude de l objet celeste
     *
     * @return magnitude
     */
    public double magnitude() {
        return magnitude;
    }

    public String info() {
        return name();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return info();
    }

}


