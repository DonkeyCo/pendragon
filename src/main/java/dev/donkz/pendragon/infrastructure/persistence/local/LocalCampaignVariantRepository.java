package dev.donkz.pendragon.infrastructure.persistence.local;

import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.infrastructure.database.local.Driver;
import dev.donkz.pendragon.infrastructure.database.local.LocalDriver;

import java.util.List;
import java.util.stream.Collectors;

public class LocalCampaignVariantRepository implements CampaignVariantRepository {
    private final static String REPOSITORY = "campaignVariants";

    private final Driver driver;

    public LocalCampaignVariantRepository() {
        this.driver = new LocalDriver();
    }

    @Override
    public List<CampaignVariant> findByName(String name) {
        List<CampaignVariant> variants = driver.select(REPOSITORY, CampaignVariant.class);
        return variants.stream().filter(it -> it.getName().contains(name)).collect(Collectors.toList());
    }

    @Override
    public List<CampaignVariant> findByCreator(Player player) {
        List<CampaignVariant> variants = driver.select(REPOSITORY, CampaignVariant.class);
        return variants.stream().filter((it) -> {
            if (it.getCreator() != null) {
                return it.getCreator().getId().equalsIgnoreCase(player.getId());
            }
            return false;
        }).collect(Collectors.toList());
    }

    @Override
    public void save(CampaignVariant entity) throws IndexAlreadyExistsException {
        driver.save(REPOSITORY, entity.getId(), entity);
    }

    @Override
    public void delete(String id) throws EntityNotFoundException {
        driver.delete(REPOSITORY, id);
    }

    @Override
    public void update(String id, CampaignVariant entity) throws EntityNotFoundException {
        driver.update(REPOSITORY, id, entity);
    }

    @Override
    public List<CampaignVariant> findAll() {
        return driver.select(REPOSITORY, CampaignVariant.class);
    }

    @Override
    public CampaignVariant findById(String id) throws EntityNotFoundException {
        return driver.selectByIndex(REPOSITORY, id, CampaignVariant.class);
    }
}
