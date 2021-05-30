package dev.donkz.pendragon.exception.model;

/**
 * Exception if ability score is below minimum
 */
public class AbilityScoreBelowMinimumExcecption extends Exception {
    public AbilityScoreBelowMinimumExcecption() {
        super("Ability Score cannot be negative");
    }
}
