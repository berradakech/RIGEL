/**
 * StarCatalogue
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public final class StarCatalogue {

    private final List<Star> stars;
    private final List<Asterism> asterisms;
    private final Map<Asterism, List<Integer>> AsterismIndiceEtoileMap;

    // stars : star0 star1 star2 ... star(star.size-1)
    // Asterism0 : star9, star2, star0
    // 9, 2, 0
    //

    public StarCatalogue(List<Star> stars, List<Asterism> asterisms) {

        Map<Star, Integer> starIndiceMap = new HashMap<>();
        Map<Asterism, List<Integer>> AsterismIndiceEtoileMapTemp = new HashMap<>();

        for (Star s : stars) {        //On crée une hash map danslaquelle on associe chaque étoile présente dans la
            starIndiceMap.put(s, starIndiceMap.size());  // liste stars à son index dans la liste stars. (stars donnée en argument)
        }

        for (Asterism unAsterismeDonne : asterisms) {
            List<Integer> listIndices = new ArrayList<>(); // On crée une nouvelle liste pour chaque nouvel astérisme qu'on va
            // remplir avec l'index des étoiles à chaque fois

            for (Star etoileDeLasterismeDonne : unAsterismeDonne.stars()) {
                Integer starIndex = starIndiceMap.get(etoileDeLasterismeDonne); // on extrait l'index de l'étoile de l'astérisme
                Preconditions.checkArgument(starIndex != null); // si l'un des astérismes contient une étoile qui ne fait pas partie de la liste stars
                listIndices.add(starIndex);
            }

            AsterismIndiceEtoileMapTemp.put(unAsterismeDonne, listIndices); // On associe à chaque astérisme une liste contenant l'index des étoiles constituant l'astérisme
        }
        this.stars = List.copyOf(stars);
        this.asterisms = List.copyOf(asterisms);
        this.AsterismIndiceEtoileMap = Collections.unmodifiableMap(AsterismIndiceEtoileMapTemp);

    }

    /**
     * Retourne la liste des étoiles
     *
     * @return une liste de Star
     */
    public List<Star> stars() {
        return this.stars;
    }

    /**
     * Retourne l'ensemble des astérismes
     *
     * @return Set d asterism
     */
    public Set<Asterism> asterisms() {
        return AsterismIndiceEtoileMap.keySet();
    }

    /**
     * retourne la liste des index — dans le catalogue — des étoiles constituant l'astérisme donné
     *
     * @param asterism
     * @return IllegalArgumentException si l'astérisme donné ne fait pas partie du catalogue
     */
    public List<Integer> asterismIndices(Asterism asterism) {
        var test = List.copyOf(AsterismIndiceEtoileMap.get(asterism));
        Preconditions.checkArgument(test != null);
        return test;
    }

    /**
     * Batisseur de StarCatalogue
     *
     * @author BERRADA EL AZIZI Mehdi (300339)
     * @author LABIDI Mohamed Helmi  (297297)
     */
    public final static class Builder {

        private List<Star> starsBuild = new ArrayList<>();
        private List<Asterism> asterismsBuild = new ArrayList<>();

        /**
         * Ajoute l'étoile donnée au catalogue en cours de construction
         *
         * @return Retourne le bâtisseur
         */
        public Builder addStar(Star star) {
            starsBuild.add(star);
            return this;
        }

        /**
         * Retourne une vue non modifiable — mais pas immuable — sur les étoiles du catalogue en cours de construction
         *
         * @return
         */
        public List<Star> stars() {
            return Collections.unmodifiableList(this.starsBuild);
        }

        /**
         * Ajoute l'astérisme donné au catalogue en cours de construction
         *
         * @param asterism
         * @return Retourne le bâtisseur
         */
        public Builder addAsterism(Asterism asterism) {
            this.asterismsBuild.add(asterism);
            return this;
        }

        /**
         * Retourne une vue non modifiable — mais pas immuable — sur les astérismes du catalogue en cours de construction
         *
         * @return
         */
        public List<Asterism> asterisms() {

            return Collections.unmodifiableList(this.asterismsBuild);
        }

        /**
         * Demande au chargeur loader d'ajouter au catalogue les étoiles et/ou astérismes qu'il obtient depuis
         * inputStream
         *
         * @param inputStream
         * @param loader
         * @return -Retourne le bâtisseur
         * -Lève IOException en cas d'erreur d'entrée/sortie
         * @throws IOException
         */
        public Builder loadFrom(InputStream inputStream, Loader loader) throws IOException {
            loader.load(inputStream, this);
            return this;
        }

        /**
         * Retourne le catalogue contenant les étoiles et astérismes ajoutés jusqu'alors au bâtisseurp
         *
         * @return
         */
        public StarCatalogue build() {
            return new StarCatalogue(starsBuild, asterismsBuild);
        }

    }

    /**
     * Interface Loader
     *
     * @author BERRADA EL AZIZI Mehdi (300339)
     * @author LABIDI Mohamed Helmi  (297297)
     */
    public interface Loader {
        /**
         * Charge les étoiles et/ou astérismes de l'inputStream et les ajoute au catalogue en cours de construction du bâtisseur builder
         *
         * @param inputStream
         * @param builder
         * @throws IOException en cas d'erreur d'entrée/sortie
         */
        void load(InputStream inputStream, Builder builder) throws IOException;
    }
}




