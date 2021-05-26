package dev.donkz.pendragon.exception.infrastructure;

public class ConnectionException extends Exception {
    public ConnectionException() {
        super("Could not connect to web socket server.");
    }
}
