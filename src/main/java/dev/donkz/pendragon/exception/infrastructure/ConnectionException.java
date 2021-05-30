package dev.donkz.pendragon.exception.infrastructure;

/**
 * Exception for failed connection
 */
public class ConnectionException extends Exception {
    public ConnectionException() {
        super("Could not connect to web socket server.");
    }
}
