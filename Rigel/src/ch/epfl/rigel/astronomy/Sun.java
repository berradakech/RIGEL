/**
 * Sun
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.util.Objects;

public final class Sun extends CelestialObject {

    private final EclipticCoordinates eclipticPos;
    private final float meanAnomaly;

    /**
     * Construction du soleil, auquelle on a rajouté les attributs phase et eclipticPos en plus de ceux de sa classe mère.
     *
     * @param eclipticPos   : Position ecliptique
     * @param equatorialPos
     * @param angularSize
     * @param meanAnomaly
     */
    public Sun(EclipticCoordinates eclipticPos, EquatorialCoordinates equatorialPos, float angularSize, float meanAnomaly) {
        super("Soleil", equatorialPos, angularSize, -26.7f);
        this.eclipticPos = Objects.requireNonNull(eclipticPos);
        this.meanAnomaly = meanAnomaly;

    }

    /**
     * Getteur du nom de la position ecliptique
     *
     * @return eclipticPos
     */
    public EclipticCoordinates eclipticPos() {
        return eclipticPos;
    }

    /**
     * Getteur de l anomalie moyenne
     *
     * @return meanAnomaly
     */
    public double meanAnomaly() {
        return meanAnomaly;
    }


}


