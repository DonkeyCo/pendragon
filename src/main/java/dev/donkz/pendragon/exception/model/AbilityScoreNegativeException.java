package dev.donkz.pendragon.exception.model;

public class AbilityScoreNegativeException extends Exception {
    public AbilityScoreNegativeException() {
        super("Ability Score cannot be negative");
    }
}
