package dev.donkz.pendragon.exception.infrastructure;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException() {
        super("Specified entity not found.");
    }

    public EntityNotFoundException(String msg) {
        super(msg);
    }
}
