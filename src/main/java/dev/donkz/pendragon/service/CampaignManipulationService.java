package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.domain.variant.CampaignVariantRepository;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.infrastructure.SessionAlreadyExists;

import java.util.List;

public class CampaignManipulationService {
    private final CampaignRepository campaignRepository;
    private final CampaignVariantRepository variantRepository;
    private final PlayerRepository playerRepository;

    public CampaignManipulationService(CampaignRepository campaignRepository, CampaignVariantRepository variantRepository, PlayerRepository playerRepository) {
        this.campaignRepository = campaignRepository;
        this.variantRepository = variantRepository;
        this.playerRepository = playerRepository;
    }

    public void createCampaign(String name, String description, CampaignVariant variant, String notes) throws IndexAlreadyExistsException, MultiplePlayersException, SessionAlreadyExists {
        Player player = playerRepository.findRegisteredPlayer();

        Campaign campaign = new Campaign(name, variant, player);
        campaign.setDescription(description);
        campaign.setNotes(notes);

        campaignRepository.save(campaign);
    }

    public void updateCampaign(Campaign campaign) throws MultiplePlayersException, EntityNotFoundException {
        Player player = playerRepository.findRegisteredPlayer();
        campaignRepository.update(campaign.getId(), campaign);
    }

    public void deleteCampaign(String id) throws EntityNotFoundException {
        campaignRepository.delete(id);
    }

    public List<CampaignVariant> getAvailableVariants() {
        return variantRepository.findAll();
    }
}
