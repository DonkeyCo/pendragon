package dev.donkz.pendragon.infrastructure.persistence.local;

import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.infrastructure.database.local.Driver;
import dev.donkz.pendragon.infrastructure.database.local.LocalDriver;

import java.util.List;
import java.util.stream.Collectors;

public class LocalPlayerRepository implements PlayerRepository {
    private final static String REPOSITORY = "players";

    private final Driver driver;

    public LocalPlayerRepository() {
        this.driver = new LocalDriver();
    }

    @Override
    public List<Player> findByUsername(String username) {
        List<Player> players = driver.select(REPOSITORY, Player.class);
        return players.stream().filter(it -> it.getUsername().contains(username)).collect(Collectors.toList());
    }

    @Override
    public void save(Player entity) throws IndexAlreadyExistsException {
        driver.save(REPOSITORY, entity.getId(), entity);
    }

    @Override
    public void delete(String id) throws EntityNotFoundException {
        driver.delete(REPOSITORY, id);
    }

    @Override
    public void update(String id, Player entity) throws EntityNotFoundException {
        driver.update(REPOSITORY, id, entity);
    }

    @Override
    public List<Player> findAll() {
        return driver.select(REPOSITORY, Player.class);
    }

    @Override
    public Player findById(String id) throws EntityNotFoundException {
        return driver.selectByIndex(REPOSITORY, id, Player.class);
    }
}
