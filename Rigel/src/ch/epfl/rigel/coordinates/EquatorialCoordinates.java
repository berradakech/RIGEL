/**
 * EquatorialCoordinates
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
import static ch.epfl.rigel.math.Angle.ofHr;


public final class EquatorialCoordinates extends SphericalCoordinates {

    // L'ascension droite est comprise dans l'intervalle [0°, 360°[ = [0h, 24h[.
    private static final RightOpenInterval RA_EQUATORIAL_INTERVAL = RightOpenInterval.of(ofHr(0.0), ofHr(24.0));

    // La déclinaison est comprise dans l'intervalle [–90°, +90°].
    private static final ClosedInterval DEC_EQUATORIAL_INTERVAL = ClosedInterval.of(Angle.ofDeg(-90.0), Angle.ofDeg(90));

    private EquatorialCoordinates(double ra, double dec) {
        super(ra, dec);
    }

    /**
     * Retourne les coordonnées horizontales.
     *
     * @param ra  l'ascencion droite
     * @param dec la déclinaison
     * @return - IllegalArgumentException si coordonnées invalides.
     * - Sinon, les coordonnées équatoriales.
     */
    public static EquatorialCoordinates of(double ra, double dec) {
        checkInInterval(RA_EQUATORIAL_INTERVAL, ra);
        checkInInterval(DEC_EQUATORIAL_INTERVAL, dec);

        return (new EquatorialCoordinates(ra, dec));
    }

    /**
     * getter de l'ascension droite
     *
     * @return ascension droite en rad
     */
    public double ra() {
        return super.lon();
    }

    /**
     * getter de l'ascension droite en degrés
     *
     * @return ascension droite en deg
     */
    public double raDeg() {
        return super.lonDeg();
    }

    /**
     * getter de l'ascension droite en heure
     *
     * @return ascension droite en heure
     */
    public double raHr() {
        return Angle.toHr(super.lon());
    }

    /**
     * getter de la déclinaison
     *
     * @return declinaison en rad
     */
    public double dec() {
        return super.lat();
    }

    /**
     * getter de la déclinaison en degrés
     *
     * @return declinaison en deg
     */
    public double decDeg() {
        return super.latDeg();
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return (String.format(Locale.ROOT, "(ra=%.4fh, dec=%.4f°)", raHr(), decDeg()));
    }


}




