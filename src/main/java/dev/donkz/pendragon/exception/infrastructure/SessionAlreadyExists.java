package dev.donkz.pendragon.exception.infrastructure;

public class SessionAlreadyExists extends Exception {
    public SessionAlreadyExists() {
        super("Session already exists");
    }
}
