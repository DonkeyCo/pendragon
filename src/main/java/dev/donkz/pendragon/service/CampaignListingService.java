package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.player.Player;

import java.util.List;

public class CampaignListingService {
    private final CampaignRepository campaignRepository;

    public CampaignListingService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public List<Campaign> getCampaignsForPlayer(Player player) {
        return campaignRepository.findByPlayer(player);
    }

    public List<Campaign> getCampaignsForDm(Player player) {
        return campaignRepository.findByDm(player);
    }
}
