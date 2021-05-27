package dev.donkz.pendragon.infrastructure.persistence.local;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.infrastructure.database.local.Driver;
import dev.donkz.pendragon.infrastructure.database.local.LocalDriver;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class LocalCampaignRepository implements CampaignRepository {
    public static final String REPOSITORY = "campaigns";

    private final Driver driver;

    @Inject
    public LocalCampaignRepository(Driver driver) {
        this.driver = driver;
    }

    public List<Campaign> findByName(String name) {
        List<Campaign> campaigns = driver.select(REPOSITORY, Campaign.class);
        return campaigns.stream().filter(it -> it.getName().equalsIgnoreCase(name)).collect(Collectors.toList());
    }

    public List<Campaign> findByDm(Player dm) {
        List<Campaign> campaigns = driver.select(REPOSITORY, Campaign.class);
        return campaigns.stream().filter((it) -> {
            if (it.getDm() != null) {
                return it.getDm().getId().equalsIgnoreCase(dm.getId());
            }
            return false;
        }).collect(Collectors.toList());
    }

    public List<Campaign> findByPlayer(Player player) {
        List<Campaign> campaigns = driver.select(REPOSITORY, Campaign.class);
        return campaigns.stream().filter((it) -> {
            if (it.getPlayers() != null) {
                return it.getPlayers().stream().anyMatch(x -> x.getId().equalsIgnoreCase(player.getId()));
            }
            return false;
        }).collect(Collectors.toList());
    }

    public List<Campaign> findByPc(Pc pc) {
        List<Campaign> campaigns = driver.select(REPOSITORY, Campaign.class);
        return campaigns.stream().filter((it) -> {
            if (it.getPcs() != null) {
                return it.getPcs().stream().anyMatch(x -> x.getId().equalsIgnoreCase(pc.getId()));
            }
            return false;
        }).collect(Collectors.toList());
    }

    public void save(Campaign entity) throws IndexAlreadyExistsException {
        driver.save(REPOSITORY, entity.getId(), entity);
    }

    public void delete(String id) throws EntityNotFoundException {
        driver.delete(REPOSITORY, id);
    }

    public void update(String id, Campaign entity) throws EntityNotFoundException {
        driver.update(REPOSITORY, id, entity);
    }

    public List<Campaign> findAll() {
        return driver.select(REPOSITORY, Campaign.class);
    }

    public Campaign findById(String id) throws EntityNotFoundException {
        return driver.selectByIndex(REPOSITORY, id, Campaign.class);
    }

    @Override
    public void saveOrUpdate(Campaign entity) throws IndexAlreadyExistsException, EntityNotFoundException {
        if (driver.select(REPOSITORY, Campaign.class).size() == 0) {
            this.save(entity);
        } else {
            this.update(entity.getId(), entity);
        }
    }
}
