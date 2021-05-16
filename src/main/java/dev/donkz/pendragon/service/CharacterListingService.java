package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.character.PcRepository;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;

import java.util.List;

public class CharacterListingService {
    private final PcRepository pcRepository;
    private final PlayerRepository playerRepository;

    public CharacterListingService(PcRepository pcRepository, PlayerRepository playerRepository) {
        this.pcRepository = pcRepository;
        this.playerRepository = playerRepository;
    }

    public List<Pc> getPlayerCharacters() throws MultiplePlayersException {
        Player player = playerRepository.findRegisteredPlayer();
        return player.getCharacters();
    }
}
