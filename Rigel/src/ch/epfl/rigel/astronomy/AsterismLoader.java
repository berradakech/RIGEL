/**
 * AsterismLoader
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.astronomy.StarCatalogue.Builder;
import ch.epfl.rigel.astronomy.StarCatalogue.Loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;


public enum AsterismLoader implements Loader {
    INSTANCE;

    /**
     * @see ch.epfl.rigel.astronomy.StarCatalogue.Loader#load(InputStream, StarCatalogue.Builder)
     */
    @Override
    public void load(InputStream inputStream, Builder builder) throws IOException {
        try (InputStreamReader file = new InputStreamReader(inputStream, US_ASCII);
             BufferedReader buff = new BufferedReader(file)) {

            String line;

            List<Star> etoilesDuBatisseur = builder.stars();

            Map<Integer, Star> indiceEtoileMap = new HashMap<>();

            // Remplissage de la map qui associe un indice hiparcos à l'étoile correspondante
            for (Star s : etoilesDuBatisseur) {
                indiceEtoileMap.put(s.hipparcosId(), s);
            }

            while ((line = buff.readLine()) != null) {

                String[] elemDeLaLigne = line.split(",");  // stocke les éléments de la ligne séparés par une virgule

                // Liste d'etoiles correspondant a un asterism
                List<Star> starsAsterism = new ArrayList<>();

                //Pour chaque ligne du dossier (correspondant a un asterism), construire l asterism
                //En iterant sur les etoiles pouvant des lors être identifiées par leur hipparcosId grâce à indiceEtoileMap
                for (String s : elemDeLaLigne) {
                    int hipparcosId = Integer.parseInt(s);
                    starsAsterism.add(indiceEtoileMap.get(hipparcosId));
                }
                builder.addAsterism(new Asterism(starsAsterism));
            }
        }

    }
}





