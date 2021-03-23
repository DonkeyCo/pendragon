package dev.donkz.pendragon.domain.player;

import dev.donkz.pendragon.domain.Repository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;

import java.util.List;

public interface PlayerRepository extends Repository<Player> {
    void saveClient(Player player) throws IndexAlreadyExistsException;
    List<Player> findByUsername(String username);
    Player findRegisteredPlayer() throws MultiplePlayersException;
}
