package dev.donkz.pendragon.domain.player;

import dev.donkz.pendragon.domain.Repository;

public interface PlayerRepository extends Repository<Player> {
    Player findByUsername(String username);
}
