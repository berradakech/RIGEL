/**
 * ClosedInterval
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.math;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkArgument;

public final class ClosedInterval extends Interval {

    private ClosedInterval(double cL, double cH) {
        super(cL, cH);
    }

    /**
     * Construction d'un intervalle fermé.
     *
     * @param low  Borne supérieure.
     * @param high Borne inférieure.
     * @return - IllegalArgumentException si la borne inférieure est (strictement) supérieure à la borne supérieure.
     *         - sinon, Un intervalle fermé ayant low and high pour bornes.
     */
    public static ClosedInterval of(double low, double high) {
        checkArgument(low < high);
        ClosedInterval intervalleFerme = new ClosedInterval(low, high);
        return intervalleFerme;

    }

    /**
     * Construction d'un intervalle fermé, symétrique centré en 0.
     *
     * @param size La taille de l'intervalle.
     * @return - IllegalArgumentException si la taille entrée est inférieure ou égale à 0.
     *         - sinon, Un intervalle fermé et symétrique.
     */
    public static ClosedInterval symmetric(double size) {

        checkArgument(size > 0);

        double high = size / 2;
        double low = -size / 2;

        ClosedInterval intervalleFerme = new ClosedInterval(low, high);

        return intervalleFerme;

    }

    /**
     * @see Interval#contains(double) 
     */
    @Override
    public boolean contains(double v) {
        if (v >= this.low() && v <= this.high()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Ecrête une valeur v à l'intervalle.
     *
     * @param v La valeur
     * @return
     */
    public double clip(double v) {

        if (v <= this.low()) {
            return this.low();
        } else if (v >= this.high()) {
            return this.high();
        } else {
            return v;
        }

    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[%.2f,%.2f]", this.low(), this.high());
    }
}

