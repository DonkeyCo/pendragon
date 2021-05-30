package dev.donkz.pendragon.exception.infrastructure;

/**
 * Exception for a running session
 */
public class SessionInProgressException extends Exception {
    public SessionInProgressException() {
        super("Session is already in progress.");
    }

    public SessionInProgressException(String message) {
        super(message);
    }
}
