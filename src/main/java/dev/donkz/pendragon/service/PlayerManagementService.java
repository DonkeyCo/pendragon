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

    /**
     * Create a player
     * @param username
     * @param profileIconUrl
     * @throws RequiredAttributeMissingException
     */
    public void createPlayer(String username, String profileIconUrl) throws RequiredAttributeMissingException {
        Player.PlayerBuilder builder = Player.builder().username(username);

        if (profileIconUrl != null) {
            builder.profileIconUrl(profileIconUrl);
        }

        Player player = builder.build();

        try {
            repository.saveClient(player);
        } catch (IndexAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve registered Player
     * @return
     */
    public Player getRegisteredPlayer() {
        try {
            return repository.findRegisteredPlayer();
        } catch (MultiplePlayersException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Retrieve player by id
     * @param id
     * @return
     */
    public Player findPlayerById(String id) {
        try {
            return repository.findById(id);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Add or update PC based on wheter it already exists
     * @param pc
     * @throws MultiplePlayersException
     * @throws EntityNotFoundException
     */
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
