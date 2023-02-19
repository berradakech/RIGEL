package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EclipticCoordinates;
import ch.epfl.rigel.coordinates.EclipticToEquatorialConversion;
import ch.epfl.rigel.coordinates.EquatorialCoordinates;
import ch.epfl.rigel.math.Angle;

import static ch.epfl.rigel.astronomy.SunModel.SUN;
import static ch.epfl.rigel.coordinates.EclipticCoordinates.of;
import static java.lang.Math.*;

public enum MoonModel implements CelestialObjectModel<Moon> {

    MOON;
    private final double lONG_MOY = Angle.ofDeg(91.929336);
    private final double lONG_MOY_PER = Angle.ofDeg(130.143076);
    private final double LONG_NOEUD_ASC = Angle.ofDeg(291.682547);
    private final double INCLINAISON_ORBITE = Angle.ofDeg(5.145396);
    private final double COS_INCLINAISON_ORBITE = Math.cos(INCLINAISON_ORBITE);
    private final double SIN_INCLINAISON_ORBITE = Math.sin(INCLINAISON_ORBITE);
    private final double EXCENTRICITE_ORBITE = 0.0549;
    private final double TAILLE_ANGULAIRE_LUNE_DEPUIS_TERRE = Angle.ofDeg(0.5181);


    /**
     * @see CelestialObjectModel#at(double, EclipticToEquatorialConversion).
     */
    @Override
    public Moon at(double daysSinceJ2010, EclipticToEquatorialConversion eclipticToEquatorialConversion) {

        Sun sun = SUN.at(daysSinceJ2010, eclipticToEquatorialConversion);
        double anomalieMoySol = sun.meanAnomaly();
        double lonEclSol = sun.eclipticPos().lon();

        // Calcul coordonnees equatoriales Lune
        double longOrbMoy = Angle.ofDeg(13.1763966) * daysSinceJ2010 + lONG_MOY;
        double anomalieMoyLune = longOrbMoy - (Angle.ofDeg(0.1114041) * daysSinceJ2010) - lONG_MOY_PER;
        double Ev = Angle.ofDeg(1.2739) * sin(2 * (longOrbMoy - lonEclSol) - anomalieMoyLune);
        double Ae = Angle.ofDeg(0.1858) * sin(anomalieMoySol);
        double A3 = Angle.ofDeg(0.37) * sin(anomalieMoySol);
        double anomalieCorLune = anomalieMoyLune + Ev - Ae - A3;
        double Ec = Angle.ofDeg(6.2886) * sin(anomalieCorLune);
        double A4 = Angle.ofDeg(0.214) * sin(2 * anomalieCorLune);
        double lonOrbCor = longOrbMoy + Ev + Ec - Ae + A4;
        double variation = Angle.ofDeg(0.6583) * sin(2 * (lonOrbCor - lonEclSol));
        double longOrbVraie = lonOrbCor + variation;
        double longMoyNoeudAsc = LONG_NOEUD_ASC - (Angle.ofDeg(0.0529539) * daysSinceJ2010);
        double longCorNoeudAsc = longMoyNoeudAsc - (Angle.ofDeg(0.16) * sin(anomalieMoySol));
        double lonEclLune = atan2(sin(longOrbVraie - longCorNoeudAsc) * COS_INCLINAISON_ORBITE, cos(longOrbVraie - longCorNoeudAsc)) + longCorNoeudAsc;
        double latEclLune = asin(sin(longOrbVraie - longCorNoeudAsc) * SIN_INCLINAISON_ORBITE);
        EclipticCoordinates ec = of(Angle.normalizePositive(lonEclLune), latEclLune);
        EquatorialCoordinates eq = eclipticToEquatorialConversion.apply(ec);

        // Calcul taille angulaire Lune
        double distanceTerreLune = (1.0 - Math.pow(EXCENTRICITE_ORBITE, 2)) / (1.0 + EXCENTRICITE_ORBITE * cos(anomalieCorLune + Ec));
        double tailleAngulaireLune = (TAILLE_ANGULAIRE_LUNE_DEPUIS_TERRE / distanceTerreLune);

        // Calcul phase
        double phase = (1.0 - cos(longOrbVraie - lonEclSol)) / 2.0;


        return new Moon(eq, (float) tailleAngulaireLune, 0.0f, (float) phase);
    }
}


