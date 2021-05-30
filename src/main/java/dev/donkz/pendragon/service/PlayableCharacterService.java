package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.character.PcRepository;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.infrastructure.SessionAlreadyExists;

import java.util.List;

public class PlayableCharacterService {
    private final PcRepository pcRepository;
    private final PlayerRepository playerRepository;

    public PlayableCharacterService(PcRepository pcRepository, PlayerRepository playerRepository) {
        this.pcRepository = pcRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Retrieves player characters for registered player
     * @return
     * @throws MultiplePlayersException
     */
    public List<Pc> getPlayerCharacters() throws MultiplePlayersException {
        Player player = playerRepository.findRegisteredPlayer();
        return player.getCharacters();
    }

    /**
     * Retrieve PCs for given player
     * @param playerId
     * @return
     * @throws EntityNotFoundException
     */
    public List<Pc> getPlayerCharacters(String playerId) throws EntityNotFoundException {
        Player player = playerRepository.findById(playerId);
        return player.getCharacters();
    }

    /**
     * Create a new PC
     * @param pc
     * @throws SessionAlreadyExists
     * @throws IndexAlreadyExistsException
     */
    public void createPlayerCharacter(Pc pc) throws SessionAlreadyExists, IndexAlreadyExistsException {
        pcRepository.save(pc);
    }

    /**
     * Get a PC by ID
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    public Pc getPlayerCharacter(String id) throws EntityNotFoundException {
        return pcRepository.findById(id);
    }

    /**
     * Update a PC
     * @param pc
     * @throws EntityNotFoundException
     */
    public void updatePlayerCharacter(Pc pc) throws EntityNotFoundException {
        pcRepository.update(pc.getId(), pc);
    }
}
