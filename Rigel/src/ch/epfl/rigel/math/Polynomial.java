/**
 *Polynomial
 *
 * @author BERRADA ELAZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi (297297)
 */
package ch.epfl.rigel.math;

import static ch.epfl.rigel.Preconditions.checkArgument;

public final class Polynomial {

     private final double[] coefficients;

    private Polynomial(double[] tableau) {
        this.coefficients = tableau;
    }

    /**
     *  Retourne la fonction polynomiale avec les coefficients donnés, ordonnés par degré en ordre décroissant.
     *
     * @param coefficientN Coefficient de plus haut degré.
     * @param coefficients
     * @return -IllegalArgumentException si le coefficient de plus haut degré vaut 0.
     *          -sinon, retourne un polynome.
     */
    public static Polynomial of(double coefficientN, double... coefficients) {

        checkArgument(coefficientN != 0);
        // Tableau contenant les coefficients + le coefficientN.
        double[] tableauCoeff = new double[coefficients.length + 1];

        tableauCoeff[0] = coefficientN; //Première case contient le coefficient de plus haut degré.

        // effectuer la copie des éléments du tableau "coefficients" reçu dans le nouveau tableau "tableauCoeff"
        System.arraycopy(coefficients, 0, tableauCoeff, 1, coefficients.length);

        return new Polynomial(tableauCoeff);
    }

    /**
     * Retourne la valeur de la fonction polynomiale pour l'argument donné.
     * @param x
     * @return polynome évalué en x.
     */
    public double at(double x) {
        double somme = coefficients[0];
        for (int i = 1; i < coefficients.length; i++) {
            somme = somme * x + coefficients[i];
        }
        return somme;
    }

    /**
     * Retourne la représentation textuelle de la fonction polynomiale sans signes superflus.
     * @return représentation textuelle du polynome.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coefficients.length ; i++){
            if (coefficients[i] == 0){
                continue; // Ne pas afficher un terme nul.
            }
            if (coefficients[i] > 0 && i > 0){
                sb.append('+'); // Affichage du signe d'un coefficient positif sans compter le coefficient du plus haut degré.
            }
            if (coefficients[i] == -1){
                sb.append('-'); // Cas particulier du coefficient -1 : Affichage de -x au lieu de -1x.
            }
            else if (coefficients[i] != 1){
                sb.append(coefficients[i]); // Affichage du coefficient.
            }

            int degreDuPolynome = coefficients.length - i -1;
            if (degreDuPolynome > 0){
                sb.append('x');
            }
            if (degreDuPolynome > 1){
                sb.append('^');
                sb.append(degreDuPolynome);
            }
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}



