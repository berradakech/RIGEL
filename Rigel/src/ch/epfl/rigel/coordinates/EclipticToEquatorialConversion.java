/**
 * EclipticToEquatorialConversion
 *
 * @author BERRADA EL AZIZ Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.util.function.Function;

import static ch.epfl.rigel.astronomy.Epoch.J2000;
import static java.lang.Math.*;

public final class EclipticToEquatorialConversion implements Function<EclipticCoordinates, EquatorialCoordinates> {

    private final double cosEpsilon; // cosinus de l'obliquité de l'écliptique
    private final double sinEpsilon; // sinus de l'obliquité de l'écliptique

    // Utilisation de la méthode of de polynomial pour stocker les coefficients du polynome de la formule
    // en dehors du constructeur et déclarée comme static
    static Polynomial epsilonPoly = Polynomial.of(Angle.ofDMS(0, 0, 0.00181),
                                                    -Angle.ofDMS(0, 0, 0.0006),
                                                      -Angle.ofDMS(0, 0, 46.815),
                                                        Angle.ofDMS(23, 26, 21.45));


    public EclipticToEquatorialConversion(ZonedDateTime when) {
        double obliquiteEcliptique; // l'obliquité de l'écliptique notée ε
        double T = J2000.julianCenturiesUntil(when); // T

        obliquiteEcliptique = epsilonPoly.at(T); // calcul de la valeur du polynome pour T.
        cosEpsilon = cos(obliquiteEcliptique);
        sinEpsilon = sin(obliquiteEcliptique);
    }

    /**
     * Retourne les coordonnées équatoriales correspondant aux coordonnées écliptiques.
     *
     * @param ecl Les coordonnées écliptiques.
     * @return Les coordonnées équatoriales.
     */
    public EquatorialCoordinates apply(EclipticCoordinates ecl) {

        double longitudeEcliptique = ecl.lon(); // Longitude écliptique
        double latitudeEcliptique = ecl.lat(); // Latitude écliptique

        // Ascension droite
        double ascensionDroite = Math.atan2((sin(longitudeEcliptique) * cosEpsilon) - (tan(latitudeEcliptique) * sinEpsilon) , cos(longitudeEcliptique));
        // Déclinaison
        double declinaison = Math.asin((sin(latitudeEcliptique) * cosEpsilon) + (cos(latitudeEcliptique) * sinEpsilon) * sin(longitudeEcliptique));

        return EquatorialCoordinates.of(Angle.normalizePositive(ascensionDroite), declinaison);
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
