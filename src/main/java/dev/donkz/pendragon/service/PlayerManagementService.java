package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;

public class PlayerManagementService {
    private final PlayerRepository repository;

    public PlayerManagementService(PlayerRepository repository) {
        this.repository = repository;
    }

    public void createPlayer(String username) throws RequiredAttributeMissingException {
        Player player = Player.builder().username(username).build();
        try {
            repository.saveClient(player);
        } catch (IndexAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    public Player getRegisteredPlayer() {
        try {
            return repository.findRegisteredPlayer();
        } catch (MultiplePlayersException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isPlayerRegistered() {
        try {
            return repository.findRegisteredPlayer() != null;
        } catch (MultiplePlayersException e) {
            e.printStackTrace();
        }
        return false;
    }
}
