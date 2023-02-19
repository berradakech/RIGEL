
package ch.epfl.rigel.gui;

import static ch.epfl.rigel.astronomy.SiderealTime.jourSideralEnSeconde;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.SECONDS;

/**
 * NamedTimeAccelerator
 *
 * Représente un accélérateur de temps nommé, c'est-à-dire une paire (nom, accélérateur).
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
public enum NamedTimeAccelerator {

    TIMES_1("1x", TimeAccelerator.continuous(1)),
    TIMES_30("30x", TimeAccelerator.continuous(30)),
    TIMES_300("300x", TimeAccelerator.continuous(300)),
    TIMES_3000("3000x", TimeAccelerator.continuous(3000)),
    DAY("jour", TimeAccelerator.discrete(60, of(24, HOURS))),
    SIDERAL_DAY("jour sideral", TimeAccelerator.discrete(60, of(jourSideralEnSeconde,SECONDS)));


    private String typeAcceleration;
    private  TimeAccelerator accelerateur;


    NamedTimeAccelerator (String s, TimeAccelerator accelerator){
        this.typeAcceleration = s;
        this.accelerateur = accelerator;
    }

    /**
     * Getteur du nom de la paire.
     * @return
     */
    String getName (){
        return typeAcceleration;
    }

    /**
     * Getteur de l'accélérateur de la paire.
     * @return
     */
    public TimeAccelerator getAccelerateur(){
        return this.accelerateur;
    }

    @Override
    /**
     * @see Object#toString()
     */
    public String toString() {
        return getName();
    }
}



