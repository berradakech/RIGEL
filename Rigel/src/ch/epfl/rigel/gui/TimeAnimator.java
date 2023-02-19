package ch.epfl.rigel.gui;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.ZonedDateTime;

/**
 * TimeAnimator : Animateur du temps, permettant d accelerer la simulation du ciel
 *
 * Représente un animateur de temps.
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
public final class TimeAnimator extends AnimationTimer {

    //on initialise la variable a une valeur negatif
    // pour pouvoir enregistrer le premier appel dans la methode handle
    private long startPremierAppel = -1;

    private final DateTimeBean dateTimeBean;
    private ObjectProperty<TimeAccelerator> accelerator = new SimpleObjectProperty<>();
    private SimpleBooleanProperty running = new SimpleBooleanProperty(false);

    //T0 de la methode adjust dans TimeAccelerator
    private ZonedDateTime T0;


    /**
     * @see AnimationTimer {@link #handle(long)}
     * @param now : nombre de nanosecondes écoulées depuis un instant de départ non spécifié
     */
    @Override
    public void handle(long now) {
        if (startPremierAppel < 0) {
            this.startPremierAppel = now;
            T0 = dateTimeBean.getZonedDateTime();
        } else {
            long dt = now - startPremierAppel;

            ZonedDateTime T = accelerator.get().adjust(T0, dt);
            dateTimeBean.setZonedDateTime(T);
        }
    }

    /**
     * @see AnimationTimer {@link #start()}
     */
    @Override
    public void start() {
        super.start();
        startPremierAppel = -1;
        running.set(true);

    }

    /**
     * @see AnimationTimer {@link #stop()}
     */
    @Override
    public void stop() {
        super.stop();
        startPremierAppel = -1;
        running.set(false);
    }

    /**
     * Constructeur du time animator qui apelle le constructeur de animation timer
     * Et initialise notre dateTimeBean
     * @param dtb
     */
    public TimeAnimator(DateTimeBean dtb) {
        super();
        this.dateTimeBean = dtb;
    }

    /**
     * Getteur de la propriete running
     * @return
     */
    public ReadOnlyBooleanProperty runningProperty() {
        return this.running;
    }

    /**
     * Getteur de la valeur dans la propriete running
     * @return
     */
    public boolean isRunning() {
        return this.running.get();
    }

    /**
     * Getteur de la propriete accelerator
     * @return
     */
    public ObjectProperty<TimeAccelerator> acceleratorProperty() {
        return this.accelerator;
    }

    /**
     * Getteur de la valeur de la propriete accelerator
     * @return
     */
    public TimeAccelerator getAccelerator() {
        return this.accelerator.get();
    }

    /**
     * Setteur de la valeur dans la propriete accelerator
     * @return
     */
    public void setAccelerator(TimeAccelerator accelerator) {
        this.accelerator.set(accelerator);
    }


}


