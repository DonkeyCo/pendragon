package dev.donkz.pendragon.infrastructure.persistence.local;

import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.domain.session.SessionRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.SessionAlreadyExists;
import dev.donkz.pendragon.infrastructure.database.local.Driver;

import javax.inject.Inject;
import java.util.List;

public class LocalSessionRepository implements SessionRepository {
    private final String REPOSITORY = "session";

    private Driver driver;

    @Inject
    public LocalSessionRepository(Driver driver) {
        this.driver = driver;
    }

    @Override
    public void save(Session entity) throws IndexAlreadyExistsException, SessionAlreadyExists {
        if (driver.select(REPOSITORY, Session.class).size() > 1) {
            throw new SessionAlreadyExists();
        }
        driver.save(REPOSITORY, entity.getId(), entity);
    }

    @Override
    public void delete(String id) throws EntityNotFoundException {
        driver.delete(REPOSITORY, id);
    }

    @Override
    public void update(String id, Session entity) throws EntityNotFoundException {
        driver.update(REPOSITORY, entity.getId(), entity);
    }

    @Override
    public List<Session> findAll() {
        return driver.select(REPOSITORY, Session.class);
    }

    @Override
    public Session findById(String id) throws EntityNotFoundException {
        return driver.selectByIndex(REPOSITORY, id, Session.class);
    }

    @Override
    public void saveOrUpdate(Session entity) throws IndexAlreadyExistsException, EntityNotFoundException, SessionAlreadyExists {
        if (driver.select(REPOSITORY, Session.class).size() == 0) {
            this.save(entity);
        } else {
            this.update(entity.getId(), entity);
        }
    }
}
