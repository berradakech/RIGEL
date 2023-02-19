/**
 * PlanetModel
 *
 * @author BERRADA EL AZIZ Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import java.util.List;

import static ch.epfl.rigel.coordinates.EclipticCoordinates.of;
import static java.lang.StrictMath.*;

public enum PlanetModel implements CelestialObjectModel<Planet> {

    MERCURY("Mercure", 0.24085, 75.5671, 77.612, 0.205627,
            0.387098, 7.0051, 48.449, 6.74, -0.42),
    VENUS("Vénus", 0.615207, 272.30044, 131.54, 0.006812,
            0.723329, 3.3947, 76.769, 16.92, -4.40),
    EARTH("Terre", 0.999996, 99.556772, 103.2055, 0.016671,
            0.999985, 0, 0, 0, 0),
    MARS("Mars", 1.880765, 109.09646, 336.217, 0.093348,
            1.523689, 1.8497, 49.632, 9.36, -1.52),
    JUPITER("Jupiter", 11.857911, 337.917132, 14.6633, 0.048907,
            5.20278, 1.3035, 100.595, 196.74, -9.40),
    SATURN("Saturne", 29.310579, 172.398316, 89.567, 0.053853,
            9.51134, 2.4873, 113.752, 165.60, -8.88),
    URANUS("Uranus", 84.039492, 356.135400, 172.884833, 0.046321,
            19.21814, 0.773059, 73.926961, 65.80, -7.19),
    NEPTUNE("Neptune", 165.84539, 326.895127, 23.07, 0.010483,
            30.1985, 1.7673, 131.879, 62.20, -6.87);


    private final String nomPlanet;
    private final double anneeTropique;
    private final double longitudeJ2010;
    private final double longitudeAuPerigee;
    private final double excentriciteOrbite;
    private final double demiGrandAxeOrbite;
    private final double inclinaisonOrbiteALecliptique;
    private final double longitudeNoeudAscendant;
    private final double tailleAngulaire;
    private final double magnitude;

    public static List<PlanetModel> ALL = List.of(MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE);


    /**
     * Constructeur
     *
     * @param nomPlanet
     * @param anneeTropique                 //Tp
     * @param longitudeJ2010                //epsilon
     * @param longitudeAuPerigee            //omega
     * @param excentriciteOrbite            //e
     * @param demiGrandAxeOrbite            // a
     * @param inclinaisonOrbiteALecliptique // i
     * @param longitudeNoeudAscendant       // grand Omega
     * @param tailleAngulaire
     * @param magnitude
     */
    PlanetModel(String nomPlanet, double anneeTropique, double longitudeJ2010, double longitudeAuPerigee,
                double excentriciteOrbite, double demiGrandAxeOrbite, double inclinaisonOrbiteALecliptique,
                double longitudeNoeudAscendant, double tailleAngulaire, double magnitude) {
        this.nomPlanet = nomPlanet;
        this.anneeTropique = anneeTropique;
        this.longitudeJ2010 = Angle.ofDeg(longitudeJ2010); // Conversion comme demandée
        this.longitudeAuPerigee = Angle.ofDeg(longitudeAuPerigee); // Conversion comme demandée
        this.excentriciteOrbite = excentriciteOrbite;
        this.demiGrandAxeOrbite = demiGrandAxeOrbite;
        this.inclinaisonOrbiteALecliptique = Angle.ofDeg(inclinaisonOrbiteALecliptique); // Conversion comme demandée
        this.longitudeNoeudAscendant = Angle.ofDeg(longitudeNoeudAscendant);
        this.tailleAngulaire = Angle.ofArcsec(tailleAngulaire); // Conversion comme demandée
        this.magnitude = magnitude;
    }

    @Override
    public Planet at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {
        double anomalieMoyenne = (((Angle.TAU / 365.242191) * (daysSinceJ2010 / this.anneeTropique)) +
                this.longitudeJ2010 - this.longitudeAuPerigee);

        double anomalieVraie = (anomalieMoyenne + 2.0 * this.excentriciteOrbite * sin(anomalieMoyenne)); // notée v

        double rayonOrbite = (this.demiGrandAxeOrbite * (1.0 - Math.pow(this.excentriciteOrbite, 2))) /
                (1.0 + (this.excentriciteOrbite * cos(anomalieVraie)));

        double longitudeOrbite = anomalieVraie + this.longitudeAuPerigee; // notée l énoncé

        double latitudeEcliptiqueHelio = asin(sin(longitudeOrbite - this.longitudeNoeudAscendant) *
                sin(this.inclinaisonOrbiteALecliptique));

        double rayonEcliptique = rayonOrbite * cos(latitudeEcliptiqueHelio); // notée rayonTerre'

        double longitudeEcliptique = Angle.normalizePositive(atan2(sin(longitudeOrbite -
                        this.longitudeNoeudAscendant) * cos(this.inclinaisonOrbiteALecliptique),
                cos(longitudeOrbite - this.longitudeNoeudAscendant)) + this.longitudeNoeudAscendant); // notée l'


        double anomalieMoyenneTerre = ((Angle.TAU / 365.242191) * (daysSinceJ2010 / EARTH.anneeTropique)) +
                EARTH.longitudeJ2010 - EARTH.longitudeAuPerigee;

        double anomalieVraieTerre = anomalieMoyenneTerre + 2.0 * EARTH.excentriciteOrbite * sin(anomalieMoyenneTerre);

        // rayonTerre et longitudeTerre comme dans l'énoncé
        double rayonTerre = (EARTH.demiGrandAxeOrbite * (1.0 - Math.pow(EARTH.excentriciteOrbite, 2))) /
                (1.0 + (EARTH.excentriciteOrbite * cos(anomalieVraieTerre))); // Rayon de la Terre.

        double longitudeTerre = anomalieVraieTerre + EARTH.longitudeAuPerigee; // longitude de la Terre dans le plan de son orbite.


        double longitude;
        if (demiGrandAxeOrbite < 1.0) { // Planète inférieure
            longitude = Math.PI + longitudeTerre + atan2(rayonEcliptique * sin(longitudeTerre - longitudeEcliptique),
                    rayonTerre - (rayonEcliptique * cos(longitudeTerre - longitudeEcliptique))); // La longitude écliptique géocentrique

        } else { // Planète supérieure
            longitude = longitudeEcliptique + atan2(rayonTerre * sin(longitudeEcliptique - longitudeTerre),
                    rayonEcliptique - (rayonTerre * cos(longitudeEcliptique - longitudeTerre))); // La longitude écliptique géocentrique
        }

        // La latitude
        double latitude = atan((rayonEcliptique * tan(latitudeEcliptiqueHelio) *
                sin(longitude - longitudeEcliptique)) / (rayonTerre * sin(longitudeEcliptique - longitudeTerre)));

        EclipticCoordinates ec = of(Angle.normalizePositive(longitude), latitude);

        EquatorialCoordinates eq = eclipticToEquatorialConversion.apply(ec); // Conversion

        double distancePlaneteTerre = Math.sqrt(Math.pow(rayonTerre, 2) + Math.pow(rayonOrbite, 2) - 2.0 * rayonTerre * rayonOrbite *
                cos(longitudeOrbite - longitudeTerre) * cos(latitudeEcliptiqueHelio));

        double tailleAngulairePlanete = this.tailleAngulaire / distancePlaneteTerre;

        double phase = (1.0 + cos(longitude - longitudeOrbite)) / 2.0;

        double magnitudeAproximative = this.magnitude + 5 * Math.log10((rayonOrbite * distancePlaneteTerre) / Math.sqrt(phase));

        return new Planet(this.nomPlanet, eq, (float) tailleAngulairePlanete, (float) magnitudeAproximative);
    }
}



