package ch.epfl.rigel.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;


/**
 * DateTimeBean
 *
 * Un bean JavaFX contenant l'instant d'observation, c-à-d le triplet (date, heure, fuseau horaire) d'observation.
 *
 * @author BERRADA EL AZIZI Mehdi (300339)
 * @author LABIDI Mohamed Helmi  (297297)
 */
public final class DateTimeBean {

    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private ObjectProperty<LocalTime> time = new SimpleObjectProperty<>();
    private ObjectProperty<ZoneId> zone = new SimpleObjectProperty<>();


    public DateTimeBean(){ }

    public ObjectProperty<LocalDate> dateProperty(){
        return this.date;
    }
    public LocalDate getDate(){
        return this.date.get();
    }
    public void setDate(LocalDate date){
        this.date.setValue(date);
    }

    public ObjectProperty<LocalTime> timeProperty() {
        return this.time;
    }
    public LocalTime getTime() {
        return this.time.get();
    }
    public void setTime(LocalTime time) {
        this.time.set(time);
    }

    public ObjectProperty<ZoneId> zoneProperty() {
        return this.zone;
    }
    public ZoneId getZone() {
        return this.zone.get();
    }
    public void setZone(ZoneId zone) {
        this.zone.set(zone);
    }

    /**
     * Getteur de l'instant d'observation.
     * @return
     */
    public ZonedDateTime getZonedDateTime (){
        return ZonedDateTime.of(getDate(),getTime(), getZone());
    }

    /**
     * Setteur de l'instant d'observation.
     * @param zdt
     */
    public void setZonedDateTime (ZonedDateTime zdt){
        this.date.set( zdt.toLocalDate());
        this.time.set( zdt.toLocalTime());
        this.zone.set( zdt.getZone());

    }
}





