package dev.donkz.pendragon.infrastructure.persistence.local;

import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.infrastructure.database.local.Driver;
import dev.donkz.pendragon.infrastructure.database.local.LocalDriver;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class LocalPlayerRepository implements PlayerRepository {
    private final static String REPOSITORY = "players";
    private final static String ACTIVE_NAME = "player";

    private final Driver driver;

    @Inject
    public LocalPlayerRepository(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void saveClient(Player player) throws IndexAlreadyExistsException {
        this.save(player);
        driver.customIndex(REPOSITORY, ACTIVE_NAME, player.getId());
    }

    @Override
    public List<Player> findByUsername(String username) {
        List<Player> players = driver.select(REPOSITORY, Player.class);
        return players.stream().filter(it -> it.getUsername().contains(username)).collect(Collectors.toList());
    }

    @Override
    public Player findRegisteredPlayer() throws MultiplePlayersException {
        List<Player> players = driver.selectCustomIndex(REPOSITORY, ACTIVE_NAME, Player.class);
        if (players.size() == 1) {
            return players.get(0);
        } else if (players.size() == 0) {
            return null;
        } else {
            throw new MultiplePlayersException();
        }
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

    @Override
    public void saveOrUpdate(Player entity) throws IndexAlreadyExistsException, EntityNotFoundException {
        if (driver.select(REPOSITORY, Player.class).stream().anyMatch(player -> player.getId().equalsIgnoreCase(entity.getId()))) {
            this.update(entity.getId(), entity);
        } else {
            this.save(entity);
        }
    }
}
