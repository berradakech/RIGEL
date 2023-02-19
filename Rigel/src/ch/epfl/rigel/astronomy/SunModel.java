/**
 * SunModel
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import static ch.epfl.rigel.coordinates.EclipticCoordinates.of;
import static java.lang.StrictMath.cos;
import static java.lang.StrictMath.sin;

public enum SunModel implements CelestialObjectModel<Sun> {

    SUN;
    private static final double LONGITUDE_SOL_J2010 = Angle.ofDeg(279.557208); // Longitude du Soleil à J2010
    private static final double LONGITUDE_SOL_PERIG = Angle.ofDeg(283.112438); // Longitude du Soleil au périgée
    private static final double EXCENTRICITE_ORBITE_ST = 0.016705; // Excentricité de l'orbite Soleil/Terre


    /**
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion).
     */
    @Override
    public Sun at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        // double au lieu de float pour ne pas perdre de précision dans les calculs.
        double anomalieMoyenne = (((Angle.TAU / 365.242191) * daysSinceJ2010) + LONGITUDE_SOL_J2010 - LONGITUDE_SOL_PERIG);
        double anomalieVraie = (anomalieMoyenne + 2.0 * EXCENTRICITE_ORBITE_ST * sin(anomalieMoyenne));
        double longitudeEcliptique = anomalieVraie + LONGITUDE_SOL_PERIG;
        double tailleAngulaireSoleil = Angle.ofDeg(0.533128) * (((1.0 + EXCENTRICITE_ORBITE_ST * cos(anomalieVraie))
                / (1.0 - Math.pow(EXCENTRICITE_ORBITE_ST, 2))));

        EclipticCoordinates ec = of(Angle.normalizePositive(longitudeEcliptique), 0.0);
        // Conversion de coordonnées écliptiques à horizontales
        EquatorialCoordinates eq = eclipticToEquatorialConversion.apply(ec);

        // cast float pour anomalieMoyenne comme demandé
        return new Sun(ec, eq, (float) tailleAngulaireSoleil, (float) anomalieMoyenne);
    }
}