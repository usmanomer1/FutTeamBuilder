
package model;

import java.util.Calendar;
import java.util.Date;

/**
 * Represents a football team builder application event.
 */
public class Event {
    private static final int HASH_CONSTANT = 13;
    private Date dateLogged;
    private String description;

    /**
     * Creates an event with the given description
     * and the current date/time stamp.
     * @param description a description of the event
     */
    public Event(String description) {
        dateLogged = Calendar.getInstance().getTime();
        this.description = description;
    }

    public Date getDate() {
        return dateLogged;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (getClass() != other.getClass()) {
            return false;
        }

        Event otherEvent = (Event) other;
        return dateLogged.equals(otherEvent.dateLogged) && description.equals(otherEvent.description);
    }

    @Override
    public int hashCode() {
        return HASH_CONSTANT * dateLogged.hashCode() + description.hashCode();
    }

    @Override
    public String toString() {
        return dateLogged.toString() + "\n" + description;
    }
}
