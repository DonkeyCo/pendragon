package dev.donkz.pendragon.exception.infrastructure;

/**
 * Exception for multiple registered players
 */
public class MultiplePlayersException extends Exception {
    public MultiplePlayersException() {
        super("There are multiple active players configured.");
    }
}
