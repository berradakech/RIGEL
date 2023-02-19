/**
 * Asterism
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

package ch.epfl.rigel.astronomy;

import java.util.List;

import static ch.epfl.rigel.Preconditions.checkArgument;

public final class Asterism {

    private final List<Star> stars;

    /**
     * Construit un astérisme composé de la liste d'étoiles données,
     * ou lève IllegalArgumentException si celle-ci est vide
     * @param stars Liste d'étoiles.
     */
    public Asterism(List<Star> stars){
        checkArgument(!(stars.isEmpty()));
        this.stars = List.copyOf(stars);
    }

    /**
     * Getteur de la liste des étoiles
     * @return
     */
    public List<Star> stars(){
        return this.stars;
    }
}



