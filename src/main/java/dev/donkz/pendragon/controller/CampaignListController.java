package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalCampaignRepository;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPlayerRepository;
import dev.donkz.pendragon.service.CampaignListingService;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.ui.Tile;
import javafx.fxml.FXML;
import javafx.scene.layout.TilePane;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class CampaignListController {
    @FXML
    private TilePane tilePane;

    private final CampaignListingService listingService;
    private final PlayerManagementService playerManagementService;

    public CampaignListController() {
        this.listingService = new CampaignListingService(new LocalCampaignRepository());
        this.playerManagementService = new PlayerManagementService(new LocalPlayerRepository());
    }

    public void initialize() {
        createTiles();
    }

    private void createTiles() {
        Player player = playerManagementService.getRegisteredPlayer();
        if (player == null) {
            // TODO: Catch Error
            System.out.println("Player not found.");
            return;
        }

        List<Campaign> campaigns = listingService.getCampaignsForPlayer(player);

        for (Campaign campaign : campaigns) {
            SortedMap<String, String> items = getTileItems(campaign, player);
            Tile tile = new Tile(campaign.getName(), items);
            System.out.print(tile.toString());
            tilePane.getChildren().add(tile);
        }
    }

    /**
     * Creates the items map, which contains all Label/Value pairs to create a tile
     *
     * @param campaign campaign
     * @param player   registered player
     * @return map of Label/Value pairs for tile
     */
    private SortedMap<String, String> getTileItems(Campaign campaign, Player player) {
        SortedMap<String, String> items = new TreeMap<>();
        items.put("Variant", campaign.getCampaignVariant().getName());
        items.put("Dungeon Master", campaign.getDm().getUsername());
        String characterName = "-";
        if (campaign.getPcs() != null) {
            Pc character = campaign.getPcs().stream().filter(pc -> player.getCharacters().contains(pc)).findFirst().orElse(null);
            if (character != null) {
                characterName = character.getName();
            }
        }
        items.put("Character", characterName);
        return items;
    }
}
