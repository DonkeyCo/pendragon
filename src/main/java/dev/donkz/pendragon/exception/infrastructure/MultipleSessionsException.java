package dev.donkz.pendragon.exception.infrastructure;

/**
 * Exception for multiple existing sessions
 */
public class MultipleSessionsException extends Exception {
    public MultipleSessionsException() {
        super("Multiple sessions exist. Only one session should be active!");
    }
}
