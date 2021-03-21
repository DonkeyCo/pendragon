package dev.donkz.pendragon.exception.infrastructure;

public class IndexAlreadyExistsException extends Exception {
    public IndexAlreadyExistsException() {
        super("Index already exists.");
    }

    public IndexAlreadyExistsException(String msg) {
        super(msg);
    }
}
