package dev.donkz.pendragon.domain.player;

import dev.donkz.pendragon.domain.Repository;

import java.util.List;

public interface PlayerRepository extends Repository<Player> {
    List<Player> findByUsername(String username);
}
