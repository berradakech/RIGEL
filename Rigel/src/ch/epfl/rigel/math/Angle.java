/**
 * Angle
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

package ch.epfl.rigel.math;

import ch.epfl.rigel.Preconditions;

import static ch.epfl.rigel.Preconditions.checkInInterval;

public final class Angle {

    // INTERVALLE représentant les valeurs possibles pour les minutes et les secondes.
    private static final RightOpenInterval INTERVALLE = RightOpenInterval.of(0, 60);
    public final static double TAU = 2 * Math.PI;
    private static final RightOpenInterval INTERVALLE_MESURE_PRINCIPALE = RightOpenInterval.of(0, 2 * Math.PI);


    /**
     * Normalise l'angle rad en le réduisant à l'intervalle [0,2π[.
     *
     * @param rad Angle en radians.
     * @return Angle normalisé.
     */
    public static double normalizePositive(double rad) {
        double radNormalize = INTERVALLE_MESURE_PRINCIPALE.reduce(rad);

        return radNormalize;
    }

    /**
     * Convertit un angle en arc secondes en un angle en radian.
     *
     * @param sec Angle en secondes d'arc.
     * @return L'angle en radians.
     */
    public static double ofArcsec(double sec) {
        double secDegToDeg = sec / 3600;

        return ofDeg(secDegToDeg); // Conversion de degrés en radians.
    }

    /**
     * Convertit un angle en deg​° min​′ sec​″ en un angle en radian.
     *
     * @param deg Degrés.
     * @param min Minutes.
     * @param sec Secondes.
     * @return -IllegalArgumentException si les minutes données n'appartiennent pas à [0,60[
     *          ou si les secondes n'appartiennent pas à [0,60[.
     *         -Sinon, retourne l'angle en radians.
     */
    public static double ofDMS(int deg, int min, double sec) {
        Preconditions.checkArgument(deg >= 0);
        checkInInterval(INTERVALLE, min);
        checkInInterval(INTERVALLE, sec);

        double angleEnSec = deg * 3600 + min * 60 + sec;

        return (ofArcsec(angleEnSec)); // Conversion des secondes d'arc en radians.
    }

    /**
     * Convertit un angle en degrés en un angle en radian.
     *
     * @param deg Angle en degrés.
     * @return Angle en radians.
     */
    public static double ofDeg(double deg) {
        return Math.toRadians(deg);
    }

    /**
     * Convertit un angle en radian en un angle en degrés.
     *
     * @param rad Angle en radians.
     * @return Angle en degrés.
     */
    public static double toDeg(double rad) {
        return Math.toDegrees(rad);
    }

    /**
     * Convertit un angle en heures en angle en radian.
     *
     * @param hr Angle en heures
     * @return Angle en radians
     */
    public static double ofHr(double hr) {
        return (hr * TAU / 24.0);
    }

    /**
     * Convertit un angle en radians en angle en heures
     *
     * @param rad Angle en radians.
     * @return Angle en heures.
     */
    public static double toHr(double rad) {
        return (rad * (24.0 / TAU));
    }
}


