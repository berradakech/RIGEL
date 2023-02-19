/**
 * Moon
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import java.util.Locale;

import static ch.epfl.rigel.Preconditions.checkInInterval;

public final class Moon extends CelestialObject {

    private final float phase;
    private final static ClosedInterval PHASE_INTERVAL = ClosedInterval.of(0, 1);

    /**
     * Construction de la Lune, a laquelle on a rajouté l attribut phase en plus de ceux de sa classe mère.
     *
     * @param equatorialPos
     * @param angularSize
     * @param magnitude
     * @param phase
     */
    public Moon(EquatorialCoordinates equatorialPos, float angularSize, float magnitude, float phase) {
        super("Lune", equatorialPos, angularSize, magnitude);
        checkInInterval(PHASE_INTERVAL, phase);
        this.phase = phase;
    }


    /**
     * redéfinition de la méthode info pour que la phase apparaisse après le nom, entre parenthèses et exprimé en pourcent, avec une décimale.
     *
     * @return format textuel comme exigé dans l énoncé
     */
    @Override
    public String info() {
        return String.format(Locale.ROOT, "Lune (%.1f%%)", this.phase * 100);
    }


}


