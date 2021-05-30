package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;

import java.util.List;

/**
 * Read Service for Campaign Variant
 */
public class VariantListingService {
    private final CampaignVariantRepository variantRepository;

    public VariantListingService(CampaignVariantRepository variantRepository) {
        this.variantRepository = variantRepository;
    }

    /**
     * Get Available Variants
     * @return
     */
    public List<CampaignVariant> getAvailableVariants() {
        return variantRepository.findAll();
    }

    /**
     * Get Variants by Player
     * @param player
     * @return
     */
    public List<CampaignVariant> getVariantsByPlayer(Player player) {
        return variantRepository.findByCreator(player);
    }
}
