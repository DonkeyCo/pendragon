package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Repository;
import dev.donkz.pendragon.domain.player.Player;

import java.util.List;

public interface CampaignVariantRepository extends Repository<CampaignVariant> {
    List<CampaignVariant> findByName(String name);
    List<CampaignVariant> findByCreator(Player player);
}
