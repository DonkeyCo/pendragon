package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.player.Player;

public interface ControllableSession {
    void sync();
    void left(String id);
    void joined(Player player);
    void message(String playerName, String message);
}
