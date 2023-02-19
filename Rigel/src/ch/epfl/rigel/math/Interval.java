/**
 * Interval classe mère
 *
 * @author BERRADA EL AZIZ Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.math;

public abstract class Interval {

    private final double LOW; // Borne inférieure
    private final double HIGH; // Borne supérieure

    protected Interval(double LOW, double high) {
        this.LOW = LOW;
        this.HIGH = high;
    }

    /**
     * @return Borne inférieure de l'intervalle.
     */
    public double low() {
        return this.LOW;
    }

    /**
     * @return Borne supérieure de l'intervalle.
     */
    public double high() {
        return this.HIGH;
    }

    /**
     * @return La taille de l'intervalle.
     */
    public double size() {
        return (HIGH - LOW);
    }

    /**
     * Vérifie si la valeur v appartient à l'intervalle
     *
     * @param v La valeur
     */
    public abstract boolean contains(double v);

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}
