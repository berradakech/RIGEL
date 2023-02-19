/**
 * Star
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.ClosedInterval;

import static ch.epfl.rigel.Preconditions.checkArgument;
import static ch.epfl.rigel.Preconditions.checkInInterval;

public final class Star extends CelestialObject {

    private final int hipparcosId;
    private final  float colorIndex;
    private static final ClosedInterval INTERVALLE_INDICE_COULEUR = ClosedInterval.of(-0.5 , 5.5);

    public Star (int hipparcosId, String name, EquatorialCoordinates equatorialPos, float magnitude, float colorIndex){
        super(name, equatorialPos,0.0f, magnitude);

        checkArgument(hipparcosId >= 0);
        checkInInterval(INTERVALLE_INDICE_COULEUR,  colorIndex);

        this.hipparcosId = hipparcosId;
        this.colorIndex = colorIndex;
    }

    /**
     * Getter du numéro Hipparcos.
     * @return
     */
    public int hipparcosId() {
        return this.hipparcosId;
    }

    /**
     * Détermination de la température de couleur.
     * @return
     */
    public int colorTemperature(){
        double temperatureCouleur = 4600.0 * ( ( 1.0 / (0.92 * this.colorIndex + 1.7)) +
                (1.0 / (0.92 * this.colorIndex + 0.62 )));

        return (int) temperatureCouleur; // Arrondie par défaut
    }
}