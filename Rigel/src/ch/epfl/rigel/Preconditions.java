/**
 * Preconditions
 *
 * @author BERRADA ELAZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi (297297)
 */
package ch.epfl.rigel;

import ch.epfl.rigel.math.Interval;

public final class Preconditions {

    private Preconditions() {
    }

    /**
     * Vérifie une condition booléenne.
     *
     * @param isTrue condition à tester.
     * @throws IllegalArgumentException si la condition n'est pas respectée.
     */
    public static void checkArgument(boolean isTrue) {
        if (!isTrue) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Vérifie si une valeur appartient a un intervalle.
     *
     * @param interval l'intervalle.
     * @param value    valeur à tester.
     * @return value si elle appartient à l'intervalle.
     * @throws IllegalArgumentException si la valeur n'appartient pas à l'intervalle.
     */
    public static double checkInInterval(Interval interval, double value) {
        if (!interval.contains(value)) {
            throw new IllegalArgumentException();
        } else {
            return value;
        }
    }
}




