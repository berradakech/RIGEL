/**
 * EquatorialToHorizontalConversion
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.astronomy.SiderealTime;
import ch.epfl.rigel.math.Angle;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public final class EquatorialToHorizontalConversion implements Function<EquatorialCoordinates, HorizontalCoordinates> {

    private double tempsSideralLocal; // Temps sidéral local propre à un endroit donné
    private ZonedDateTime when;
    private GeographicCoordinates where;

    public EquatorialToHorizontalConversion(ZonedDateTime when, GeographicCoordinates where) {
        this.when = when;
        this.where = where;
        tempsSideralLocal = SiderealTime.local(when, where);
    }

    /**
     * Retourne les coordonnées horizontales correspondant aux coordonnées équatoriales.
     *
     * @param equ Coordonnées équatoriales.
     * @return
     */
    public HorizontalCoordinates apply(EquatorialCoordinates equ) {

        double latitudePositionObservateur = where.lat(); // La latitude de la position de l'observateur.
        double declinaisonObservateur = equ.dec(); // La déclinaison de la position de l'observateur.

        double angleHoraire = tempsSideralLocal - equ.ra(); // L'angle horaire.

        double hauteur = Math.asin(sin(declinaisonObservateur) * sin(latitudePositionObservateur) +
                cos(declinaisonObservateur) * cos(latitudePositionObservateur) * cos(angleHoraire)); // La hauteur.

        double azimut = Math.atan2(-cos(declinaisonObservateur) * cos(latitudePositionObservateur) * sin(angleHoraire),
                sin(declinaisonObservateur) - (sin(latitudePositionObservateur) * sin(hauteur))); // L'azimut

        double azimutNormalise = Angle.normalizePositive(azimut); // Normalisation.
        HorizontalCoordinates coordonneesHorizontales = HorizontalCoordinates.of(azimutNormalise, hauteur);

        return coordonneesHorizontales;
    }

    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }
}