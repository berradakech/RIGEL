/**
 * HorizontalCoordinates
 *
 * @author BERRADA EL AZIZ Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.ClosedInterval;
import ch.epfl.rigel.math.RightOpenInterval;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkInInterval;
import static java.lang.Math.*;


public final class HorizontalCoordinates extends SphericalCoordinates {

    // l'azimut est compris entre [0°,360°[ = [0,2π[
    private final static RightOpenInterval AZ_HORIZONTAL_INTERVAL = RightOpenInterval.of(Angle.ofDeg(0.0), Angle.ofDeg(360));

    // la hauteur est comprise entre [-90°,90°] = [-π/2,π/2]
    private final static ClosedInterval ALT_HORIZONTAL_INTERVAL = ClosedInterval.of(Angle.ofDeg(-90.0), Angle.ofDeg(90));

    private final static double DEMI_TAILLE_OCTANT = 22.5;

    private HorizontalCoordinates(double az, double alt) {
        super(az, alt);
    }

    /**
     * Retourne les coordonnées horizontales.
     *
     * @param az  l'azimut
     * @param alt la hauteur
     * @return - IllegalArgumentException si coordonnées invalides.
     * - Sinon, les coordonnées horizontales.
     */
    public static HorizontalCoordinates of(double az, double alt) {
        checkInInterval(AZ_HORIZONTAL_INTERVAL, az);
        checkInInterval(ALT_HORIZONTAL_INTERVAL, alt);

        return (new HorizontalCoordinates(az, alt));
    }

    /**
     * Retourne les coordonnées horizontales.
     *
     * @param azDeg  l'azimut en degrés
     * @param altDeg la hauteur en degrés
     * @return - IllegalArgumentException si coordonnées invalides.
     * - Sinon, les coordonnées horizontales.
     */
    public static HorizontalCoordinates ofDeg(double azDeg, double altDeg) {
        checkInInterval(AZ_HORIZONTAL_INTERVAL, Angle.ofDeg(azDeg));
        checkInInterval(ALT_HORIZONTAL_INTERVAL, Angle.ofDeg(altDeg));

        return (new HorizontalCoordinates(Angle.ofDeg(azDeg), Angle.ofDeg(altDeg)));
    }

    /**
     * Retourne l'azimut (longitude dans les coordonnees horizontales)
     *
     * @return azimut en radian
     */
    public double az() {
        return super.lon();
    }

    /**
     * Retourne l'azimut en degrés
     *
     * @return azimut en deg
     */
    public double azDeg() {
        return super.lonDeg();
    }

    /**
     * retourne une chaîne correspondant à l'octant dans lequel se trouve l'azimut
     *
     * @param n
     * @param e
     * @param s
     * @param w
     * @return
     */
    public String azOctantName(String n, String e, String s, String w) {
        double angleInit = 45.0; // On commence par l octant n+e car celui du nord est traité à part.
        double azDeg = azDeg();

        String[] pointCardinauxEtIntercadinaux = {n, n + e, e, s + e, s, s + w, w, n + w};

        for (int i = 1; i < pointCardinauxEtIntercadinaux.length; i++) {
            if ((azDeg >= angleInit - DEMI_TAILLE_OCTANT) && (azDeg < angleInit + DEMI_TAILLE_OCTANT)) {
                return pointCardinauxEtIntercadinaux[i];
            }
            angleInit += 45.0;

        }
        // Cas de l octant correspondant au nord traité separément.
        return pointCardinauxEtIntercadinaux[0];

    }


    /**
     * retourne la hauteur
     *
     * @return hauteur
     */
    public double alt() {
        return super.lat();
    }

    /**
     * retourne la hauteur en degres
     *
     * @return hauteur en degres
     */
    public double altDeg() {
        return super.latDeg();
    }

    /**
     * qui retourne la distance angulaire
     * entre le récepteur (this) et le point donné en argument (that).
     *
     * @param that
     * @return distance angulaire
     */
    public double angularDistanceTo(HorizontalCoordinates that) {
        double azimut1 = this.az();
        double hauteur1 = this.alt();
        double azimut2 = that.az();
        double hauteur2 = that.alt();

        // formule de la distance comme indiquée dans l'énoncé.
        double distance = acos((sin(hauteur1) * sin(hauteur2)) + (cos(hauteur1) * cos(hauteur2) * cos(azimut1 - azimut2)));
        return distance;
    }

    /**
     * @see Object#toString()
     */
    public String toString() {
        return (String.format(Locale.ROOT, "(az=%.4f°, alt=%.4f°)", azDeg(), altDeg()));
    }


}




