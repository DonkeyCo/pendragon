package dev.donkz.pendragon.exception.infrastructure;

public class SessionInProgressException extends Exception {
    public SessionInProgressException() {
        super("Session is already in progress.");
    }

    public SessionInProgressException(String message) {
        super(message);
    }
}
