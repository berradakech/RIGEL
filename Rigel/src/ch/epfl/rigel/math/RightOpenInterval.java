/**
 * RightOpenInterval
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

package ch.epfl.rigel.math;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkArgument;

public final class RightOpenInterval extends Interval {

    private RightOpenInterval(double roL, double roH) {
        super(roL, roH);
    }

    /**
     * Construction d'un intervalle semi ouvert à droite.
     *
     * @param low  Borne supérieure.
     * @param high Borne inférieure.
     * @return - IllegalArgumentException si la borne inférieure est (strictement) supérieure à la borne supérieure.
     *          - sinon, Un intervalle semi ouvert à droite ayant low and high pour bornes.
     */
    public static RightOpenInterval of(double low, double high) {
        checkArgument(low < high);
        RightOpenInterval intervalleSemiOuvert = new RightOpenInterval(low, high);

        return intervalleSemiOuvert;
    }

    /**
     * Construction d'un intervalle semi ouvert à droite, symétrique centré en 0.
     *
     * @param size La taille de l'intervalle.
     * @return - IllegalArgumentException si la taille entrée est inférieure ou égale a 0.
     *         - sinon, Un intervalle semi ouvert à droite et symétrique.
     */
    public static RightOpenInterval symmetric(double size) {
        checkArgument(size > 0);

        double high = size / 2;
        double low = -size / 2;

        RightOpenInterval intervalleSemiOuvert = new RightOpenInterval(low, high);

        return intervalleSemiOuvert;
    }

    /**
     * @see Interval#contains(double)
     */
    @Override
    public boolean contains(double v) {
        return (v >= low() && v < high());
    }

    /**
     * Réduction de l'argument à l'intervalle.
     *
     * @param v Valeur à réduire.
     * @return Valeur dans l'intervalle.
     */
    public double reduce(double v) {
        double fm = (v - low()) - (this.high() - this.low()) * Math.floor((v - this.low()) / (this.high() - this.low()));

        return this.low() + fm;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%.2f,%.2f[", this.low(), this.high());
    }
}