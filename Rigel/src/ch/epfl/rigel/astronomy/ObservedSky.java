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
     * une méthode d'accès afin d'obtenir sun sous la forme d'instance(s) de la sous-classe de CelestialObject correspondant au type d'objet
     *
     * @return
     */
    public Sun sun() {
        return this.soleil;
    }

    /**
     * une méthode donnant la position de sun dans le plan, sous la forme d'une instance de CartesianCoordinates pour les objets individuels
     *
     * @return
     */
    public CartesianCoordinates sunPosition() {
        return CartesianCoordinates.of(projectionSoleil.x(), projectionSoleil.y());
    }

    /**
     * une méthode d'accès afin d'obtenir sun sous la forme d'instance(s) de la sous-classe de CelestialObject correspondant au type d'objet
     *
     * @return
     */
    public Moon moon() {
        return this.lune;
    }

    /**
     * une méthode donnant la position de moon dans le plan, sous la forme d'une instance de CartesianCoordinates pour les objets individuels
     *
     * @return
     */
    public CartesianCoordinates moonPosition() {
        return CartesianCoordinates.of(projectionLune.x(), projectionLune.y());
    }

    /**
     * une méthode d'accès afin d'obtenir sun sous la forme d'instance(s) de la sous-classe de CelestialObject correspondant au type d'objet
     *
     * @return
     */
    public List<Planet> planets() {
        return List.copyOf(planetes);
    }

    /**
     * une méthode donnant la position planetes sous la forme d'un tableau de double pour les objets multiples (planètes et étoiles)
     *
     * @return
     */
    public double[] planetPositions() {
        return this.projectionPlanetes.clone();

    }

    /**
     * une méthode d'accès afin d'obtenir sun sous la forme d'instance(s) de la sous-classe de CelestialObject correspondant au type d'objet
     *
     * @return
     */
    public List<Star> stars() {
        return List.copyOf(this.etoiles);
    }

    /**
     * une méthode donnant la position planetes sous la forme d'un tableau de double pour les objets multiples (planètes et étoiles)
     *
     * @return
     */
    public double[] starPosition() {
        return this.projectionEtoiles.clone();
    }

    /**
     * acces aux asterismes du catalogue utilisé.
     *
     * @return
     */
    public Set<Asterism> asterisms() {
        return this.starCatalogue.asterisms();
    }

    /**
     * Accès à la liste des index des étoiles d'un astérisme donné.
     *
     * @param a
     * @return
     */
    public List<Integer> starsIndexOfAsterism(Asterism a) {
        return this.starCatalogue.asterismIndices(a);
    }

    /**
     * Methodes retournant l'objet le plus proche de l'objet  dont les coordonnées sont cc (respectant une distance maximale)
     * @param cc : Coordonnées de l'objet dont on veut l'objet le plus proche
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


        // Si la distance à l'objet le plus proche est inférieure à la distance maximale passée en argument alors on retourne
        // l'objet céleste le plus proche correspondant à la distance la plus procche.
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
     * Si la difference entre l abcisse (ordonnée) de la planete ou l etoile et l abcisse (ordonnée) de notre cc est plus grande
     * qu une distance, alors forcement d apres la formule de la distance euclidienne, l objet (x,y) est forcément plus loin que
     * l objet dont la distance vaut distance (donnée en argument de la methode)
     * <p>
     * Si la condition necessaire est respectée, a ce moment la on doit calculer la distance entre cet objet et notre cc
     * Et la comparer a la distance minimal stockée.
     *
     * @param x        : abcisse de la planete ou l etoile dont on va potentiellement calculer sa distance avec le cc en argument
     * @param y        : ordonnée de la planete ou l etoile dont on va potentiellement calculer sa distance avec le cc en argument
     * @param cc       : coordonnées de l objet celestes donné en argument de objectClosestTo
     * @param distance : La distance entre l objet le plus proche en cours d execution et cc
     * @return
     */
    public boolean uneCoordPlusGrandeQueDistance(double x, double y, CartesianCoordinates cc, double distance) {
        return (Math.abs(x - cc.x()) > distance || Math.abs(y - cc.y()) > distance);
    }

    /** Méthode auxiliaire pour éviter la duplication de code dans objectClosestTo
     * @param tableau
     * @param cc
     * @param min
     * @return
     */
    private double[] methode(double[] tableau, CartesianCoordinates cc, double min) {
        int indexObjetPlusProche = 0;
        double aRendre[] = new double[2];

        for (int i = 0; i < tableau.length; i += 2) {
            // Si la distance a une planete est plus petite que la distance à l'objet supposé alors la planète devient l'objet le plus proche.

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


