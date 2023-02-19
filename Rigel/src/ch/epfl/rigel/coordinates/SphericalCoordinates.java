/**
 * SphericalCoordinates: Classe mère à toutes
 * les classes représentant des coordonnées sphériques
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */


package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

abstract class SphericalCoordinates {

    private double longitude;
    private double latitude;


    SphericalCoordinates(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * @return la longitude en radian
     */
    double lon() {
        return this.longitude;
    }

    /**
     * @return la latitude en radian
     */
    double lat() {
        return this.latitude;
    }

    /**
     * @return la longitude en degré
     */
    double lonDeg() {
        return Angle.toDeg(this.longitude);
    }

    /**
     * @return La latitude en degré
     */
    double latDeg() {
        return Angle.toDeg(this.latitude);
    }

    @Override
    public final int hashCode() {

        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}











