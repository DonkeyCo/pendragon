package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;

import java.util.List;

public class VariantListingService {
    private final CampaignVariantRepository variantRepository;

    public VariantListingService(CampaignVariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    public List<CampaignVariant> getAvailableVariants() {
        return variantRepository.findAll();
    }

    public List<CampaignVariant> getVariantsByPlayer(Player player) {
        return variantRepository.findByCreator(player);
    }
}
