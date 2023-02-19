/**
 * Epoch
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

package ch.epfl.rigel.astronomy;

import java.time.*;
import java.time.temporal.ChronoUnit;

public enum Epoch {
    // Epoque du 1 janvier 2000 à 12h00 UTC.
    J2000(ZonedDateTime.of(LocalDate.of(2000, Month.JANUARY, 1), LocalTime.of(12, 0), ZoneOffset.UTC)),
    // Epoque du 31 décembre 2009 à 0h00 UTC
    J2010(ZonedDateTime.of(LocalDate.of(2010, Month.JANUARY, 1).minusDays(1), LocalTime.of(0, 0), ZoneOffset.UTC));

    private ZonedDateTime date;

    Epoch(ZonedDateTime of) {
        this.date = of;
    }

    /**
     * Retourne le nombre de jours entre une époque et un instant
     *
     * @param when - l'instant
     * @return - valeur positive correspond à date postérieure à l'époque
     *         - valeur négative correspond à date antérieure à l'époque
     */
    public double daysUntil(ZonedDateTime when) {
        double nombreMilli = this.date.until(when, ChronoUnit.MILLIS); // nombre de millisecondes séparant deux dates
        double  nombreJour = nombreMilli / (1000 * 60 * 60 * 24); // Conversion de millisecondes en jours, pour plus de précision

        return nombreJour;

    }

    /**
     * Retourne le nombre de siècles juliens entre l'époque à laquelle on l'applique et un instant donné
     *
     * @param when - l'instant donné
     * @return nombre de siècles
     */
    public double julianCenturiesUntil(ZonedDateTime when) {
        double nombreMilli = this.date.until(when, ChronoUnit.MILLIS); // nombre de millisecondes séparant deux dates
        double nombreSiecle = nombreMilli / (1000 * 60 * 60 * 24) / 36525; // Conversion de millisecondes en siécles juliens

        return nombreSiecle;

    }
}
