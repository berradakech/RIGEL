/**
 * CartesianCoordinates
 *
 * @author BERRADA EL AZIZ Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.coordinates;

import java.util.Locale;

public final class CartesianCoordinates {

    private final double x;
    private final double y;


    private CartesianCoordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Methode publique of pour construire un objet de type CartesianCoordinates
     *
     * @param x
     * @param y
     * @return un objet de type CartesianCoordinates ayant x pour abcisse et y pour ordonnee
     */
    public static CartesianCoordinates of(double x, double y) {
        return new CartesianCoordinates(x, y);
    }

    /**
     * Retourne l abcisse x
     *
     * @return x
     */
    public double x() {
        return this.x;
    }

    /**
     * Retourne l ordonnee y
     *
     * @return y
     */
    public double y() {
        return this.y;
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "(%.4f.,%.4f)", x, y);
    }
}


