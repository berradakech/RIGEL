/**
 * EclipticCoordinates
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


public final class EclipticCoordinates extends SphericalCoordinates {

    // La longitude écliptique est comprise dans l'intervalle [0°, 360°[
    private static final RightOpenInterval LON_ECLIPTIC_INTERVAL = RightOpenInterval.of(Angle.ofDeg(0.0) , Angle.ofDeg(360));

    // La latitude écliptique est comprise dans l'intervalle [–90°, 90°].
    private static final ClosedInterval LAT_ECLIPTIC_INTERVAL = ClosedInterval.of(Angle.ofDeg(-90.0) , Angle.ofDeg(90));


    private EclipticCoordinates(double lon, double lat){
        super(lon,lat);
    }

    /**
     * Retourne les coordonnées écliptiques.
     * @param lon
     *        la longitude écliptique
     * @param lat
     *        la latitde écliptique
     * @return - IllegalArgumentException si coordonnées invalides.
     *         - Sinon, les coordonnées écliptiques.
     */
    public static EclipticCoordinates of (double lon, double lat){
        checkInInterval (LON_ECLIPTIC_INTERVAL, lon);
        checkInInterval (LAT_ECLIPTIC_INTERVAL, lat);

        return (new EclipticCoordinates(lon,lat));
    }

    /**
     * getter de la longitude écliptique
     * return longitude écliptique
     */
    public double lon() { return super.lon();
    }


    /**
     * getter de la longitude écliptique en degrés
     * @return longitude écliptique en degrés
     */
    public double lonDeg() {
        return super.lonDeg();
    }


    /**
     * getter de la latitude écliptique
     * @return latitude écliptique
     */
    public double lat() { return super.lat();
    }

    /**
     * getter de la latitude écliptique en degrés
     * @return latitude écliptique en degrés
     */
    public double latDeg() {
        return super.latDeg();
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return( String.format(Locale.ROOT,"(λ=%.4f°, β=%.4f°)",lonDeg(), latDeg()));
    }


}




