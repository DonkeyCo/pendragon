package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CampaignListingService {
    private final CampaignRepository campaignRepository;

    public CampaignListingService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public List<Campaign> getAllCampaigns(Player player) {
        // Could contain duplicates
        List<Campaign> allCampaigns = new ArrayList<>();
        allCampaigns.addAll(getCampaignsForPlayer(player));
        allCampaigns.addAll(getCampaignsForDm(player));

        Map<String, Campaign> campaigns = new HashMap<>();
        for (Campaign campaign : allCampaigns) {
            if (!campaigns.containsKey(campaign.getId())) {
                campaigns.put(campaign.getId(), campaign);
            }
        }
        return new ArrayList<>(campaigns.values());
    }

    public List<Campaign> getCampaignsForPlayer(Player player) {
        return campaignRepository.findByPlayer(player);
    }

    public List<Campaign> getCampaignsForDm(Player player) {
        return campaignRepository.findByDm(player);
    }

    public Campaign getCampaign(String id) throws EntityNotFoundException {
        return campaignRepository.findById(id);
    }

    public boolean campaignExists(String id) {
        try {
            campaignRepository.findById(id);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }
}
