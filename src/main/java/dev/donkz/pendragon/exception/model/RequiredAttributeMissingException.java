package dev.donkz.pendragon.exception.model;

public class RequiredAttributeMissingException extends Exception {
    public RequiredAttributeMissingException() {
        super("Required Attribute is missing.");
    }

    public RequiredAttributeMissingException(String msg) {
        super(msg);
    }
}
