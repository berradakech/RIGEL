/**
 * CelestialObjectModel
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;

public interface CelestialObjectModel<O> {

    /**
     * Retourne l'objet modélisé pour le nombre de jours après l'époque J2010 en coordonnées équatoriales
     *
     * @param daysSinceJ2010
     * @param eclipticToEquatorialConversion
     * @return
     */
    O at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion);
}