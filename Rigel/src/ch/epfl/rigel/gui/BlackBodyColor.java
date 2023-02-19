
package ch.epfl.rigel.gui;

import ch.epfl.rigel.Preconditions;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.*;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * BlackBodyColor : Classe permettant de lier la temperature des etoiles a leur couleur
 *
 * @author BERRADA EL AZIZ Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
public class BlackBodyColor {

    private BlackBodyColor() {}

    private final static List<String> LIST_COULEUR_F = extraction();

    // Intervalle représentant la plage des températures.
    private final static ClosedInterval PLAGE_TEMP = ClosedInterval.of(1000.0, 40000.0);

    /**
     * Extraction de données
     */
    static List <String> extraction() {

        InputStream dossier = BlackBodyColor.class.getClassLoader().getResourceAsStream("bbr_color.txt");
        List<String> listeCouleur = new ArrayList<>();
        try (InputStreamReader fichier = new InputStreamReader(dossier, US_ASCII);
             BufferedReader buff = new BufferedReader(fichier)) {

            String ligne;
            while ((ligne = buff.readLine()) != null) {
                if (ligne.charAt(0) != '#' && ligne.charAt(10) == '1') { // Si la ligne ne contient pas de # et si 10dec
                    listeCouleur.add(ligne.substring(80, 87));
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return listeCouleur;
    }

    /**
     * Détermination de la couleur à partir de la température.
     * @param temp
     * @return la couleur
     */
    public static Color colorForTemperature(int temp) {
        Preconditions.checkInInterval(PLAGE_TEMP, temp);
        // lit le dossier et initialise la liste de la methode au dessus pour le dossier
        // que je prends en argument
        int indice = (int) indiceAccesseur(temp);
        return Color.web(LIST_COULEUR_F.get(indice));
    }

    /**
     * Retourne l'indice de la température dans le fichier.
     * @param temperature temperature souhaité
     * @return retourne l'indice de la ligne (dans le dossier) correspondant a la temperature associée
     */
    public static long indiceAccesseur(double temperature) {
        return Math.round((temperature - 1000) / 100.0);
    }
}

