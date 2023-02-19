/**
 * GeographicCoordinates
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkInInterval;

public final class GeographicCoordinates extends SphericalCoordinates {

    // la latitude doit être comprise entre [-90°,90°] = [-π/2,π/2]
    private final static ClosedInterval LAT_GEOGRAPHIC_INTERVAL = ClosedInterval.of(Angle.ofDeg(-90), Angle.ofDeg(90));

    // la longitude doit être comprise entre [-180°,180°[ = [-π,π[
    private final static RightOpenInterval LONG_GEOGRAPHIC_INTERVAL = RightOpenInterval.of(Angle.ofDeg(-180), Angle.ofDeg(180));


    private GeographicCoordinates(double longitude, double latitude) {
        super(longitude, latitude);
    }

    /**
     * Retourne les coordonnées géographiques
     *
     * @param lonDeg longitude en degrés
     * @param latDeg latide en degrés
     * @return - IllegalArgumentException si composantes invalides
     * - Sinon, coordonnées géographiques
     */
    public static GeographicCoordinates ofDeg(double lonDeg, double latDeg) {
        checkInInterval(LONG_GEOGRAPHIC_INTERVAL, Angle.ofDeg(lonDeg));
        checkInInterval(LAT_GEOGRAPHIC_INTERVAL, Angle.ofDeg(latDeg));

        return (new GeographicCoordinates(Angle.ofDeg(lonDeg), Angle.ofDeg(latDeg)));
    }

    /**
     * retourne vrai si et seulement si l'angle qui lui est passé représente une longitude valide en degrés
     *
     * @param lonDeg
     * @return boolean
     */
    public static boolean isValidLonDeg(double lonDeg) {
        return LONG_GEOGRAPHIC_INTERVAL.contains(Angle.ofDeg(lonDeg));
    }

    /**
     * retourne vrai si et seulement si l'angle qui lui est passé représente une latitude valide en degrés
     *
     * @param latDeg
     * @return boolean
     */
    public static boolean isValidLatDeg(double latDeg) {
        return LAT_GEOGRAPHIC_INTERVAL.contains(Angle.ofDeg(latDeg));

    }

    @Override
    public double lon() {
        return super.lon();
    }

    @Override
    public double lat() {
        return super.lat();
    }

    @Override
    public double lonDeg() {
        return super.lonDeg();
    }

    @Override
    public double latDeg() {
        return super.latDeg();
    }

    /**
     * Redefinition de toString affichant la longitude et la latitude avec une precision de 4 decimaux
     *
     * @return
     */
    @Override
    public String toString() {
        return (String.format(Locale.ROOT, "(lon=%.4f°, lat=%.4f°)", lonDeg(), latDeg()));
    }
}




