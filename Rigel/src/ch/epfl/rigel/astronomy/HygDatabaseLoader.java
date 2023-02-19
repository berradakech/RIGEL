/**
 * HygDataBaseLoader
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.nio.charset.StandardCharsets.US_ASCII;

public enum HygDatabaseLoader implements StarCatalogue.Loader {

    INSTANCE;

    /**
     * @see ch.epfl.rigel.astronomy.StarCatalogue.Loader#load(InputStream, StarCatalogue.Builder)
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {
        try (InputStreamReader file = new InputStreamReader(inputStream, US_ASCII); // lit tous les bytes de la ressource et les transforme en caractères
             BufferedReader buff = new BufferedReader(file))  // Pour plus d'efficacité
        {
            String line;
            buff.readLine(); // saute la première ligne qui contient les entêtes
            while ((line = buff.readLine()) != null) {

                String[] elemDeLaLigne = line.split(","); // stocke les éléments de la ligne séparés par une virgule

                // Extraction numéro Hipparcos, sinon 0 la valeur par défaut si la colonne est vide
                int hipparcosId = colonne.HIP.extractionInt(elemDeLaLigne, 0);

                // Extraction numéro désignation de Bayer, sinon ?, la valeur par défaut, si la colonne est vide
                String bayer = colonne.BAYER.extractionString(elemDeLaLigne, "?");

                // Extraction numéro du nom abrégé de la constellation
                String con = colonne.CON.extractionString(elemDeLaLigne);

                // Extraction numéro du nom propore, sinon la valeur par défaut si la colonne est vide
                String name = colonne.PROPER.extractionString(elemDeLaLigne, bayer + " " + con);

                // Extraction ascension droite et déclinaison pour les coordonnées équatoriales
                double ra = colonne.RARAD.extractionDouble(elemDeLaLigne);
                double dec = colonne.DECRAD.extractionDouble(elemDeLaLigne);

                // Extraction magnitude, sinon 0.0 la valeur par défaut si la colonne est vide
                double magnitude = colonne.MAG.extractionDouble(elemDeLaLigne, 0.0);

                // Extraction indice de couleur, sinon 0.0 la valeur par défaut si la colonne est vide
                double colorIndex = colonne.CI.extractionDouble(elemDeLaLigne, 0.0);

                builder.addStar(new Star(hipparcosId, name, EquatorialCoordinates.of(ra, dec), (float) magnitude, (float) colorIndex));
            }
        }
    }

    /**
     * colonne : Type enumere imbriqué
     *
     * @author BERRADA EL AZIZI Mehdi (300339)
     * @author LABIDI Mohamed Helmi  (297297)
     */
    private enum colonne {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC, RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON, COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;


        /**
         * Méthode auxiliaire : Retourne l'élément à l'index ordinal dans le tableau passé en argument
         *
         * @param elemDeLaLigne
         * @return String
         */
        String extractionString(String[] elemDeLaLigne) {
            return elemDeLaLigne[ordinal()];
        }

        /**
         * Méthode auxiliaire :Surcharge de extractionS, Si l'élément à extraire est vide on retourne String passée en argument
         *
         * @param elemDeLaLigne l'élément à extraire
         * @param s             String à retourner si l'élément est vide
         * @return String
         */
        String extractionString(String[] elemDeLaLigne, String s) {
            String element = extractionString(elemDeLaLigne);
            if (element.isBlank()) {
                return s;
            } else {
                return element;
            }
        }

        /**
         * Retourne l'élément à l'index ordinal dans le tableau passé en argument
         *
         * @param col le tableau
         * @param i   la valeur par défaut
         * @return - Retourne int, l'élément qu'on veut extraire
         * - Si c'est vide, retourne la valeur par défaut (i)
         */
        Integer extractionInt(String[] col, int i) {
            String element = extractionString(col);
            if (element.isBlank()) {
                return i;
            } else {
                return Integer.parseInt(element); // Transformer des String contenant des nombres en Integer
            }

        }

        /**
         * Retourne l'élément à l'index ordinal dans le tableau passé en argument
         *
         * @param col le tableau
         * @param d   la valeur par défaut
         * @return - Retourne int, l'élément qu'on veut extraire
         * - Si c'est vide, retourne la valeur par défaut (i)
         */
        Double extractionDouble(String[] col, double d) {
            String element = extractionString(col);
            if (element.isBlank()) {
                return d;
            } else {
                return Double.parseDouble(element); // Transformer des String contenant des nombres en Double
            }

        }

        /**
         * Retourne l'élément à l'index ordinal dans le tableau passé en argument
         *
         * @param col
         * @return
         */
        Double extractionDouble(String[] col) {
            return Double.parseDouble(extractionString(col));
        }

    }


}





