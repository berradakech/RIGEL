/**
 * StereographicProjection
 *
 * @author BERRADA EL AZIZ Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
package ch.epfl.rigel.coordinates;

import ch.epfl.rigel.math.Angle;

import java.util.Locale;
import java.util.function.Function;

import static java.lang.Math.*;

public final class StereographicProjection implements Function<HorizontalCoordinates, CartesianCoordinates> {

    private final double azCentre; //lambda0
    private final double altCentre; // phi1
    private final double cosAltCemtre;
    private final double sinAltCentre;
    private final CartesianCoordinates ORIGINE = CartesianCoordinates.of(0,0);

    /**
     * Constructeur de la classe
     *
     * @param center
     */
    public StereographicProjection(HorizontalCoordinates center) {
        azCentre = center.az();
        altCentre = center.alt();
        cosAltCemtre = cos(altCentre);
        sinAltCentre = sin(altCentre);


    }

    /**
     * Retourne les coordonnées du centre du cercle correspondant à la projection du parallèle passant par le point hor
     *
     * @param hor
     * @return coordonnees du centre du cerlce decris ci dessus
     */
    public CartesianCoordinates circleCenterForParallel(HorizontalCoordinates hor) {
        double y = (cosAltCemtre / (sin(hor.alt()) + sinAltCentre)); //Calcul de l ordonnée des coordonnées à retourner
        return CartesianCoordinates.of(0.0, y);

    }

    /**
     * Retourne le rayon du cercle correspondant à la projection du parallèle passant par le point de coordonnées hor
     *
     * @param parallel
     * @return rayon du cerlce decris ci dessus
     */
    public double circleRadiusForParallel(HorizontalCoordinates parallel) {
        return ((cos(parallel.alt())) / (sin(parallel.alt()) + sinAltCentre));
    }

    /**
     * retourne le diamètre projeté d'une sphère de taille angulaire rad centrée au centre de projection, en admettant que celui-ci soit sur l'horizon
     *
     * @param rad
     * @return Le diametre d un onjet de taille angulaire rad
     */
    public double applyToAngle(double rad) {
        return (2 * tan(rad / 4.0));

    }


    /**
     * Projection stereographique d un point de coordonnees horizontales en un point de coordonnes cartesiennes
     *
     * @param horizontalCoordinates
     * @return les coordonnees cartesiennes apres la projection stereographique du point
     */
    @Override
    public CartesianCoordinates apply(HorizontalCoordinates horizontalCoordinates) {
        double delta = (horizontalCoordinates.az() - azCentre); // difference de longitudes entre les deux coordonnées
        double d = 1.0 / (1.0 + (sin(horizontalCoordinates.alt()) * sinAltCentre) + (cos(horizontalCoordinates.alt()) * cosAltCemtre * cos(delta)));

        double x = (d * cos(horizontalCoordinates.alt()) * sin(delta));
        double y = d * ((sin(horizontalCoordinates.alt()) * cosAltCemtre) - (cos(horizontalCoordinates.alt()) * sinAltCentre * cos(delta)));

        return CartesianCoordinates.of(x, y);
    }

    /**
     * Inverse projection stereographique
     *
     * @param xy
     * @return les coordonnees horizontales du point xy.
     */
    public HorizontalCoordinates inverseApply(CartesianCoordinates xy) {

        if(xy.x() == ORIGINE.x() && xy.y() == ORIGINE.y()){
            return HorizontalCoordinates.of(azCentre,altCentre);
        }

        double ro = Math.sqrt(Math.pow(xy.x(), 2.0) + Math.pow(xy.y(), 2.0));
        double sinc = ((2 * ro) / (Math.pow(ro, 2) + 1));
        double cosc = (1 - Math.pow(ro, 2)) / (Math.pow(ro, 2) + 1);

        double az = atan2(xy.x() * sinc, (ro * cosAltCemtre * cosc) - (xy.y() * sinAltCentre * sinc)) + azCentre;
        double alt = asin((cosc * sinAltCentre) + ((xy.y() * sinc * cosAltCemtre) / ro));

        return HorizontalCoordinates.of(Angle.normalizePositive(az), alt);

    }


    @Override
    public final int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "StereographicProjection(%.4f,%.4f)", azCentre, altCentre);
    }
}


