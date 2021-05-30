package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.player.Player;

/**
 * Interface for Controllers, which control session related actions
 */
public interface ControllableSession {
    /**
     * Incoming Synchronize Sessions Request Callback
     */
    void sync();

    /**
     * Incoming Left Session Request Callback
     * @param id id of player who left
     */
    void left(String id);

    /**
     * Incoming Joined Session Request Callback
     * @param player joined player
     */
    void joined(Player player);

    /**
     * Incoming Message Request Callback
     * @param playerName name of sender
     * @param message message string
     */
    void message(String playerName, String message);

    /**
     * Roll Dice Request Callback
     * @param rollMessage message for roll
     */
    void roll(String rollMessage);
}
