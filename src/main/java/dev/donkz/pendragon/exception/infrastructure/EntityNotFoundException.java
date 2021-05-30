package dev.donkz.pendragon.exception.infrastructure;

/**
 * Exception for an entity, which was not found
 */
public class EntityNotFoundException extends Exception {
    public EntityNotFoundException() {
        super("Specified entity not found.");
    }

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
