package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalCampaignRepository;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPlayerRepository;
import dev.donkz.pendragon.service.CampaignListingService;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.ui.Router;
import dev.donkz.pendragon.ui.components.Tile;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CampaignController {
    private static final String ROUTE = "campaign";

    private final CampaignListingService listingService;
    private final PlayerManagementService playerManagementService;
    private final Router router;

    @FXML
    private Pane root;
    @FXML
    private TilePane campaignPane;
    @FXML
    private TilePane dmCampaignPane;

    public CampaignController(Router router) {
        this.router = router;
        this.listingService = new CampaignListingService(new LocalCampaignRepository());
        this.playerManagementService = new PlayerManagementService(new LocalPlayerRepository());
    }

    @FXML
    public void initialize() {
        try {
            registerRoutes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        AnchorPane.setLeftAnchor(root, 0.0);
        AnchorPane.setRightAnchor(root, 0.0);
        AnchorPane.setTopAnchor(root, 0.0);
        AnchorPane.setBottomAnchor(root, 0.0);

        Player player = playerManagementService.getRegisteredPlayer();
        if (player != null) {
            List<Campaign> attendedCampaigns = listingService.getCampaignsForPlayer(player);
            List<Campaign> dmCampaigns = listingService.getCampaignsForDm(player);
            List<Tile> tiles = getTiles(attendedCampaigns, player);
            List<Tile> dmTiles = getTiles(dmCampaigns, player);

            campaignPane.getChildren().setAll(tiles);
            dmCampaignPane.getChildren().setAll(dmTiles);
        } else {
            System.out.println("Player does not exist");
        }
    }

    @FXML
    public void onCreate(Event e) {
        // route to campaign creation menu
        router.goToSub(HomeController.SUB_ROUTER, "createCampaign");
    }

    private List<Tile> getTiles(List<Campaign> campaigns, Player player) {
        return campaigns.stream().map((campaign -> {
            Tile tile = new Tile();
            tile.setTitle(campaign.getName());
            tile.setDm(campaign.getDm().getUsername());
            tile.setVariant(campaign.getCampaignVariant().getName());
            if (campaign.getPcs() != null) {
                Pc character = campaign.getPcs().stream().filter(pc -> player.getCharacters().contains(pc)).findFirst().orElse(null);
                if (character == null) {
                    tile.setPc("-");
                } else {
                    tile.setPc(character.getName());
                }
            } else {
                tile.setPc("-");
            }
            return tile;
        })).collect(Collectors.toList());
    }

    private void registerRoutes() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/CampaignCreationPanel.fxml"));
        loader.setControllerFactory(classType -> new CampaignCreationController(router));
        router.registerSubRoute(HomeController.SUB_ROUTER, "createCampaign", loader.load());
    }
}
