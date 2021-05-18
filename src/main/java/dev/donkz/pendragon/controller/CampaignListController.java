package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.service.CampaignCreationService;
import dev.donkz.pendragon.service.CampaignListingService;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.service.VariantListingService;
import dev.donkz.pendragon.ui.Tile;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CampaignListController implements Initializable {
    @FXML
    private StackPane campaignRoot;
    @FXML
    private ComboBox<String> cmbVariants;
    @FXML
    private Pane editor;
    @FXML
    private TilePane tilePane;
    @FXML
    private Pane overview;

    private final CampaignListingService listingService;
    private final CampaignCreationService creationService;
    private final PlayerManagementService playerManagementService;
    private final VariantListingService variantListingService;

    @Inject
    public CampaignListController(CampaignListingService listingService, CampaignCreationService creationService, PlayerManagementService playerManagementService, VariantListingService variantListingService) {
        this.listingService = listingService;
        this.creationService = creationService;
        this.playerManagementService = playerManagementService;
        this.variantListingService = variantListingService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    @FXML
    public void onCreate() {
        overview.setVisible(false);
        editor.setVisible(true);
        initVariantPicker();
    }

    @FXML
    public void onSave() {

    }

    private void initVariantPicker() {
        List<String> variants = variantListingService.getAvailableVariants().stream().map(CampaignVariant::getName).collect(Collectors.toList());
        cmbVariants.setItems(FXCollections.observableList(variants));
    }
}
