package dev.donkz.pendragon.infrastructure.persistence.local;

import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.character.PcRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.infrastructure.database.local.Driver;
import dev.donkz.pendragon.infrastructure.database.local.LocalDriver;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class LocalPcRepository implements PcRepository {
    private final static String REPOSITORY = "pcs";

    private final Driver driver;

    @Inject
    public LocalPcRepository(Driver driver) {
        this.driver = driver;
    }

    @Override
    public List<Pc> findByName(String name) {
        List<Pc> pcs = driver.select(REPOSITORY, Pc.class);
        return pcs.stream().filter(it -> it.getName().contains(name)).collect(Collectors.toList());
    }

    @Override
    public void save(Pc entity) throws IndexAlreadyExistsException {
        driver.save(REPOSITORY, entity.getId(), entity);
    }

    @Override
    public void delete(String id) throws EntityNotFoundException {
        driver.delete(REPOSITORY, id);
    }

    @Override
    public void update(String id, Pc entity) throws EntityNotFoundException {
        driver.update(REPOSITORY, id, entity);
    }

    @Override
    public List<Pc> findAll() {
        return driver.select(REPOSITORY, Pc.class);
    }

    @Override
    public Pc findById(String id) throws EntityNotFoundException {
        return driver.selectByIndex(REPOSITORY, id, Pc.class);
    }

    @Override
    public void saveOrUpdate(Pc entity) throws IndexAlreadyExistsException, EntityNotFoundException {
        if (driver.select(REPOSITORY, Pc.class).size() == 0) {
            this.save(entity);
        } else {
            this.update(entity.getId(), entity);
        }
    }
}
