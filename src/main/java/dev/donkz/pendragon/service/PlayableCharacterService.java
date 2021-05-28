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

    public List<Pc> getPlayerCharacters() throws MultiplePlayersException {
        Player player = playerRepository.findRegisteredPlayer();
        return player.getCharacters();
    }

    public List<Pc> getPlayerCharacters(String playerId) throws EntityNotFoundException {
        Player player = playerRepository.findById(playerId);
        return player.getCharacters();
    }

    public void createPlayerCharacter(Pc pc) throws SessionAlreadyExists, IndexAlreadyExistsException {
        pcRepository.save(pc);
    }

    public Pc getPlayerCharacter(String id) throws EntityNotFoundException {
        return pcRepository.findById(id);
    }
}
