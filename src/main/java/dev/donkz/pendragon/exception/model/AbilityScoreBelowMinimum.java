package dev.donkz.pendragon.exception.model;

public class AbilityScoreBelowMinimum extends Exception {
    public AbilityScoreBelowMinimum() {
        super("Ability Score cannot be negative");
    }
}
