package dev.donkz.pendragon.exception.infrastructure;

/**
 * Exception for already existing session
 */
public class SessionAlreadyExists extends Exception {
    public SessionAlreadyExists() {
        super("Session already exists");
    }
}
