package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;

public class PlayerManagementService {
    private final PlayerRepository repository;

    public PlayerManagementService(PlayerRepository repository) {
        this.repository = repository;
    }

    public void createPlayer(String username) throws RequiredAttributeMissingException {
        Player player = new Player.Builder().setUsername(username).build();
        try {
            repository.save(player);
        } catch (IndexAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    public boolean playerExists() {
        return repository.findAll().size() > 0;
    }
}
