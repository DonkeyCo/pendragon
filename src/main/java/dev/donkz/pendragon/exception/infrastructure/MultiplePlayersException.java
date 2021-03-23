package dev.donkz.pendragon.exception.infrastructure;

public class MultiplePlayersException extends Exception {
    public MultiplePlayersException() {
        super("There are multiple active players configured.");
    }
}
