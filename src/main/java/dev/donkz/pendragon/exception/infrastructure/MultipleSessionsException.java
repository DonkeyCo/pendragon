package dev.donkz.pendragon.exception.infrastructure;

public class MultipleSessionsException extends Exception {
    public MultipleSessionsException() {
        super("Multiple sessions exist. Only one session should be active!");
    }
}
