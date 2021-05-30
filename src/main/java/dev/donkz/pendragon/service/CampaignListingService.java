package dev.donkz.pendragon.service;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.campaign.CampaignRepository;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Read Service for Campaigns
 */
public class CampaignListingService {
    private final CampaignRepository campaignRepository;

    public CampaignListingService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    /**
     * Retrieve all campaigns for a player
     * @param player player object
     * @return list of found campaigns
     */
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

    /**
     * Get all campaigns, in which player participated
     * @param player participant
     * @return list of campaigns
     */
    public List<Campaign> getCampaignsForPlayer(Player player) {
        return campaignRepository.findByPlayer(player);
    }

    /**
     * Get all campaign, in which player was DM
     * @param player DM
     * @return list of campaigns
     */
    public List<Campaign> getCampaignsForDm(Player player) {
        return campaignRepository.findByDm(player);
    }

    /**
     * Retrieve campaign object by ID
     * @param id id of campaign
     * @return campaign object
     * @throws EntityNotFoundException
     */
    public Campaign getCampaign(String id) throws EntityNotFoundException {
        return campaignRepository.findById(id);
    }

    /**
     * Check if campaign exists
     * @param id id of campaign
     * @return exists
     */
    public boolean campaignExists(String id) {
        try {
            campaignRepository.findById(id);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }
}
