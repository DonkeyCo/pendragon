package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.infrastructure.SessionAlreadyExists;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;

import java.util.List;

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

    public Player findPlayerById(String id) {
        try {
            return repository.findById(id);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addOrUpdatePcForRegisteredPlayer(Pc pc) throws MultiplePlayersException, EntityNotFoundException {
        Player player = repository.findRegisteredPlayer();
        List<Pc> pcs = player.getCharacters();

        int index = -1;
        for (int i = 0; i < pcs.size(); i++) {
            if (pcs.get(i).getId().equalsIgnoreCase(pc.getId())) {
                index = i;
            }
        }

        if (index != -1) {
            player.updateCharacter(index, pc);
        } else {
            player.addCharacter(pc);
        }

        repository.update(player.getId(), player);
    }
}
