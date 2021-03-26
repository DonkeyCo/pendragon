package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;

import java.util.List;

public class CampaignCreationService {
    private final CampaignRepository campaignRepository;
    private final CampaignVariantRepository variantRepository;
    private final PlayerRepository playerRepository;

    public CampaignCreationService(CampaignRepository campaignRepository, CampaignVariantRepository variantRepository, PlayerRepository playerRepository) {
        this.campaignRepository = campaignRepository;
        this.variantRepository = variantRepository;
        this.playerRepository = playerRepository;
    }

    public void createCampaign(String name, String description, CampaignVariant variant, String notes) throws IndexAlreadyExistsException, MultiplePlayersException {
        Player player = playerRepository.findRegisteredPlayer();

        Campaign campaign = new Campaign(name, variant);
        campaign.setDm(player);
        campaign.setDescription(description);
        campaign.setNotes(notes);

        campaignRepository.save(campaign);
    }

    public List<CampaignVariant> getAvailableVariants() {
        return variantRepository.findAll();
    }
}
