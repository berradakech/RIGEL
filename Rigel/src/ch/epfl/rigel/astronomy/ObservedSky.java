package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.*;

import java.time.ZonedDateTime;
import java.util.*;

import static ch.epfl.rigel.astronomy.Epoch.J2010;
import static ch.epfl.rigel.astronomy.PlanetModel.*;

/**
 * ObservedSky : Creation du ciel et tous ses objets celestes.
 *
 * @author BERRADA EL AZIZ Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */

public class ObservedSky {

    //Soleil
    private final Sun soleil;
    private final CartesianCoordinates projectionSoleil;

    //Lune
    private final Moon lune;
    private final CartesianCoordinates projectionLune;

    //Planet
    private final List<Planet> planetes = new ArrayList<>();
    private final double[] projectionPlanetes;

    //Etoiles
    private List<Star> etoiles;
    private final double[] projectionEtoiles;

    //Conversions
    private final EquatorialToHorizontalConversion equToHor;
    private final EclipticToEquatorialConversion eclToEqu;

    //Divers
    private StarCatalogue starCatalogue;


    public ObservedSky(ZonedDateTime date, GeographicCoordinates gc, StereographicProjection sp, StarCatalogue sc) {
        this.eclToEqu = new EclipticToEquatorialConversion(date);
        this.equToHor = new EquatorialToHorizontalConversion(date, gc);
        this.starCatalogue = sc;

        //Soleil
        this.soleil = SunModel.SUN.at(J2010.daysUntil(date), eclToEqu); // le soleil en coordonnes equatoriales
        HorizontalCoordinates horSoleil = equToHor.apply(soleil.equatorialPos());
        projectionSoleil = sp.apply(horSoleil);

        //Lune
        this.lune = MoonModel.MOON.at(J2010.daysUntil(date), eclToEqu);
        HorizontalCoordinates horLune = equToHor.apply(lune.equatorialPos());
        projectionLune = sp.apply(horLune);

        //Planetes

        List<PlanetModel> listSansTerre = List.of(MERCURY, VENUS, MARS, JUPITER, SATURN, URANUS, NEPTUNE);

        for (PlanetModel p : listSansTerre) {
            this.planetes.add(p.at(J2010.daysUntil(date), eclToEqu));
        }

        int i = 0;
        projectionPlanetes = new double[2 * planetes.size()];
        for (Planet p : planetes) {
            HorizontalCoordinates hor = equToHor.apply(p.equatorialPos());
            CartesianCoordinates cc = sp.apply(hor);
            projectionPlanetes[i] = cc.x();
            projectionPlanetes[i + 1] = cc.y();
            i = i + 2;
        }


        //Etoiles
        this.etoiles = sc.stars();
        projectionEtoiles = new double[2 * etoiles.size()];
        int j = 0;
        for (Star s : etoiles) {
            HorizontalCoordinates horEtoile = equToHor.apply(s.equatorialPos());
            CartesianCoordinates cartEtoile = sp.apply(horEtoile);

            projectionEtoiles[j] = cartEtoile.x();
            projectionEtoiles[j + 1] = cartEtoile.y();
            j += 2;
        }

    }

    /**
     * une m??thode d'acc??s afin d'obtenir sun sous la forme d'instance(s) de la sous-classe de CelestialObject correspondant au type d'objet
     *
     * @return
     */
    public Sun sun() {
        return this.soleil;
    }

    /**
     * une m??thode donnant la position de sun dans le plan, sous la forme d'une instance de CartesianCoordinates pour les objets individuels
     *
     * @return
     */
    public CartesianCoordinates sunPosition() {
        return CartesianCoordinates.of(projectionSoleil.x(), projectionSoleil.y());
    }

    /**
     * une m??thode d'acc??s afin d'obtenir sun sous la forme d'instance(s) de la sous-classe de CelestialObject correspondant au type d'objet
     *
     * @return
     */
    public Moon moon() {
        return this.lune;
    }

    /**
     * une m??thode donnant la position de moon dans le plan, sous la forme d'une instance de CartesianCoordinates pour les objets individuels
     *
     * @return
     */
    public CartesianCoordinates moonPosition() {
        return CartesianCoordinates.of(projectionLune.x(), projectionLune.y());
    }

    /**
     * une m??thode d'acc??s afin d'obtenir sun sous la forme d'instance(s) de la sous-classe de CelestialObject correspondant au type d'objet
     *
     * @return
     */
    public List<Planet> planets() {
        return List.copyOf(planetes);
    }

    /**
     * une m??thode donnant la position planetes sous la forme d'un tableau de double pour les objets multiples (plan??tes et ??toiles)
     *
     * @return
     */
    public double[] planetPositions() {
        return this.projectionPlanetes.clone();

    }

    /**
     * une m??thode d'acc??s afin d'obtenir sun sous la forme d'instance(s) de la sous-classe de CelestialObject correspondant au type d'objet
     *
     * @return
     */
    public List<Star> stars() {
        return List.copyOf(this.etoiles);
    }

    /**
     * une m??thode donnant la position planetes sous la forme d'un tableau de double pour les objets multiples (plan??tes et ??toiles)
     *
     * @return
     */
    public double[] starPosition() {
        return this.projectionEtoiles.clone();
    }

    /**
     * acces aux asterismes du catalogue utilis??.
     *
     * @return
     */
    public Set<Asterism> asterisms() {
        return this.starCatalogue.asterisms();
    }

    /**
     * Acc??s ?? la liste des index des ??toiles d'un ast??risme donn??.
     *
     * @param a
     * @return
     */
    public List<Integer> starsIndexOfAsterism(Asterism a) {
        return this.starCatalogue.asterismIndices(a);
    }

    /**
     * Methodes retournant l'objet le plus proche de l'objet  dont les coordonn??es sont cc (respectant une distance maximale)
     * @param cc : Coordonn??es de l'objet dont on veut l'objet le plus proche
     * @param distanceMaxi distance maximale
     * @return l'objet le plus  proche de cc
     */
    public Optional<CelestialObject> objectClosestTo(CartesianCoordinates cc, double distanceMaxi) {

        double distSoleil = distance(projectionSoleil, cc); // Distance pointeur soleil
        double distLune = distance(projectionLune, cc); // Distance pointeur Lune
        double[] output;
        double[] outputDeux;
        CelestialObject objetCelesteLePlusProche;

        double minSoleilLune = Math.min(distSoleil, distLune);

        if (minSoleilLune == distSoleil) {
            objetCelesteLePlusProche = soleil; // Si la distance minimale correspond a la distance au soleil
        } else {
            objetCelesteLePlusProche = lune; // alors le soleil est lobjet le plus proche. sinon lune.
        }

        output = methode(projectionPlanetes, cc, minSoleilLune);

        if (output != null) {
            int indiceObjetPlusProche = (int) output[0];
            objetCelesteLePlusProche = planetes.get(indiceObjetPlusProche);
            minSoleilLune = output[1];
        }
        outputDeux = methode(projectionEtoiles, cc, minSoleilLune);

        if (outputDeux != null) {
            int indexObjetPlusProche = (int) outputDeux[0];
            objetCelesteLePlusProche = etoiles.get(indexObjetPlusProche);
            minSoleilLune = outputDeux[1];
        }


        // Si la distance ?? l'objet le plus proche est inf??rieure ?? la distance maximale pass??e en argument alors on retourne
        // l'objet c??leste le plus proche correspondant ?? la distance la plus procche.
        if (minSoleilLune <= distanceMaxi) {
            return Optional.of(objetCelesteLePlusProche);
        }
        // Sinon rien.
        return Optional.empty();
    }

    /**
     * Distance entre deux points.
     *
     * @param cc1
     * @param cc2
     * @return
     */
    public double distance(CartesianCoordinates cc1, CartesianCoordinates cc2) {
        return Math.sqrt(Math.pow(cc1.x() - cc2.x(), 2) + Math.pow(cc1.y() - cc2.y(), 2));
    }

    /**
     * Methode qui nous permet d eviter de calculer la distance euclidienne entre TOUS nos objets celestes
     * <p>
     * Condition necessaire (mais pas suffisante) pour qu un objet celeste soit le plus proche de cc:
     * Si la difference entre l abcisse (ordonn??e) de la planete ou l etoile et l abcisse (ordonn??e) de notre cc est plus grande
     * qu une distance, alors forcement d apres la formule de la distance euclidienne, l objet (x,y) est forc??ment plus loin que
     * l objet dont la distance vaut distance (donn??e en argument de la methode)
     * <p>
     * Si la condition necessaire est respect??e, a ce moment la on doit calculer la distance entre cet objet et notre cc
     * Et la comparer a la distance minimal stock??e.
     *
     * @param x        : abcisse de la planete ou l etoile dont on va potentiellement calculer sa distance avec le cc en argument
     * @param y        : ordonn??e de la planete ou l etoile dont on va potentiellement calculer sa distance avec le cc en argument
     * @param cc       : coordonn??es de l objet celestes donn?? en argument de objectClosestTo
     * @param distance : La distance entre l objet le plus proche en cours d execution et cc
     * @return
     */
    public boolean uneCoordPlusGrandeQueDistance(double x, double y, CartesianCoordinates cc, double distance) {
        return (Math.abs(x - cc.x()) > distance || Math.abs(y - cc.y()) > distance);
    }

    /** M??thode auxiliaire pour ??viter la duplication de code dans objectClosestTo
     * @param tableau
     * @param cc
     * @param min
     * @return
     */
    private double[] methode(double[] tableau, CartesianCoordinates cc, double min) {
        int indexObjetPlusProche = 0;
        double aRendre[] = new double[2];

        for (int i = 0; i < tableau.length; i += 2) {
            // Si la distance a une planete est plus petite que la distance ?? l'objet suppos?? alors la plan??te devient l'objet le plus proche.

            // Verification de la condition necessaire (cf methode uneCoordPlusGrandeQueDistance)
            if (!(uneCoordPlusGrandeQueDistance(tableau[i], tableau[i + 1], cc, min))) {
                double distCondNecessaireP = distance(CartesianCoordinates.of(tableau[i], tableau[i + 1]), cc);
                if (distCondNecessaireP < min) {
                    min = distCondNecessaireP;
                    indexObjetPlusProche = i / 2;

                    aRendre[0] = indexObjetPlusProche;
                    aRendre[1] = min;
                    return aRendre;
                }
            }
        }
        return null;
    }
}


