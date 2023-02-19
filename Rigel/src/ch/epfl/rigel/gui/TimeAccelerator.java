
package ch.epfl.rigel.gui;

import java.time.Duration;
import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.NANOS;

/**
 * TimeAccelerator
 *
 * Représente un « accélérateur de temps », c'est-à-dire une fonction permettant de calculer le temps simulé — 
 * et généralement accéléré — en fonction du temps réel.
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
@FunctionalInterface
public interface TimeAccelerator {

    /**
     * Calcule le temps simulé T.
     * @param T0 Le temps simulé initial
     * @param dt Le temps réel écoulé depuis le début de l'animation (t-t0) en NANOSECONDES.
     * @return Temps simulé
     */
    ZonedDateTime adjust (ZonedDateTime T0, long dt);

    /**
     * Retourne un accélérateur continu en fonction du facteur d'accélération.
     * @param alpha Facteur d'accélération.
     * @return
     */
    static TimeAccelerator continuous (int alpha){
        return (T0, tempsEcouleDepuisDebutAnim) -> T0.plus(alpha * tempsEcouleDepuisDebutAnim, NANOS);
    }

    /**
     * Retourne un accélérateur discret en fonction de la fréquence d'avancement
     * @param frequence Fréquence d'avancement.
     * @param S Le pas discret de temps simulé.
     * @return
     */
    static TimeAccelerator discrete (long frequence, Duration S){
        return (T0, tempsEcouleDepuisDebutAnim) -> T0.plus(S.multipliedBy( frequence * tempsEcouleDepuisDebutAnim  / 1000000000L) );

    }
}



