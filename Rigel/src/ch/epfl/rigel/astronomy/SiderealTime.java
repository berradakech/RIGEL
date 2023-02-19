/**
 * SiderealTime
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.math.Polynomial;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import static ch.epfl.rigel.astronomy.Epoch.J2000;
import static ch.epfl.rigel.math.Angle.normalizePositive;
import static ch.epfl.rigel.math.Angle.ofHr;
import static java.time.ZoneOffset.UTC;

public final class SiderealTime {

    public static final int jourSideralEnSeconde = 23*3600 + 56*60 + 4;
    /**
     * Retourne le temps sidéral de Greenwich.
     *
     * @param when Couple date/heure.
     * @return Résultat compris entre [0, 2π].
     */
    public static double greenwich(ZonedDateTime when) {

        ZonedDateTime whenInGMT = when.withZoneSameInstant(UTC); // Conversion en GMT

        ZonedDateTime truncatedWhenInGMT = whenInGMT.truncatedTo(ChronoUnit.DAYS);

        double siecleJulienEntreJ2000EtInstant = J2000.julianCenturiesUntil(truncatedWhenInGMT);
        double heuresEntreDebutJourEtInstant = (truncatedWhenInGMT.until(when, ChronoUnit.MILLIS) /
                (1000.0 * 60.0 * 60.0));

        Polynomial s0 = Polynomial.of(0.000025862, 2400.051336, 6.697374558);
        double S0 = s0.at(siecleJulienEntreJ2000EtInstant);
        double S1 = 1.002737909 * heuresEntreDebutJourEtInstant;

        // Conversion en radians et normalisation dans l'intervalle [0,2π]
        double tempsSideralGreenwich = normalizePositive(ofHr(S0 + S1));

        return tempsSideralGreenwich; // Le temps sidéral de Greenwich
    }

    /**
     * Retourne le temps sidéral local, pour un couple date/heure et une position.
     *
     * @param when  Le couple date/heure.
     * @param where La position.
     * @return résultat en radians et compris dans l'intervalle [0, 2π[.
     */
    public static double local(ZonedDateTime when, GeographicCoordinates where) {
        double SgWhenInGMT = greenwich(when); // méthode greenwich pour retrouver le Sg (temps sidéral).

        double Sl = normalizePositive(SgWhenInGMT + where.lon()); // addition de la longitude,puis normalisation

        return Sl; // Le temps sidéral local
    }
}