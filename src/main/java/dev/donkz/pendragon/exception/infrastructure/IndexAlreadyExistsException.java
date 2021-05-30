package dev.donkz.pendragon.exception.infrastructure;

/**
 * Exception for duplicated indexes
 */
public class IndexAlreadyExistsException extends Exception {
    public IndexAlreadyExistsException() {
        super("Index already exists.");
    }

    public IndexAlreadyExistsException(String msg) {
        super(msg);
    }
}
