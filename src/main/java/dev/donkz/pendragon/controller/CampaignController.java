package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.variant.CampaignVariant;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.infrastructure.SessionAlreadyExists;
import dev.donkz.pendragon.service.CampaignManipulationService;
import dev.donkz.pendragon.service.CampaignListingService;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.service.VariantListingService;
import dev.donkz.pendragon.ui.Tile;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.util.StringConverter;

import javax.inject.Inject;
import java.net.URL;
import java.util.*;

public class CampaignController implements Initializable, Controller {
    @FXML private Button btnSave;
    @FXML private StackPane campaignRoot;
    @FXML private Pane editor;
    @FXML private TilePane tilePane;
    @FXML private TextField txtName;
    @FXML private TextField txtDescription;
    @FXML private ComboBox<CampaignVariant> cmbVariants;
    @FXML private TextArea txtNotes;
    @FXML private Pane overview;

    private Controller parentController;
    private final CampaignListingService listingService;
    private final CampaignManipulationService manipulationService;
    private final PlayerManagementService playerManagementService;
    private final VariantListingService variantListingService;

    @Inject
    public CampaignController(CampaignListingService listingService, CampaignManipulationService manipulationService, PlayerManagementService playerManagementService, VariantListingService variantListingService) {
        this.listingService = listingService;
        this.manipulationService = manipulationService;
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

        List<Campaign> campaigns = listingService.getAllCampaigns(player);
        campaigns.sort(Comparator.comparing(Campaign::getName));
        tilePane.getChildren().clear();
        for (Campaign campaign : campaigns) {
            SortedMap<String, String> items = getTileItems(campaign, player);
            Tile tile = new Tile(campaign.getId(), campaign.getName(), items);
            if (campaign.getDm().getId().equalsIgnoreCase(player.getId())) {
                tile.setHasStart(true);
                tile.getBtnStart().setOnAction(actionEvent -> {
                    Controller rootController = parentController;
                    while (rootController.getParentController() != null) {
                        rootController = parentController.getParentController();
                    }
                    ((MainController) rootController).activateLobby(campaign);
                });
            }
            tile.setOnMouseClicked(mouseEvent -> {
                Tile sourceTile = (Tile) mouseEvent.getSource();
                onEdit(sourceTile.getObjectId());
            });
            tile.getBtnDelete().setOnAction(actionEvent -> {
                onDelete(tile.getObjectId());
            });
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
    public void onJoin() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Enter Host Address");
        dialog.showAndWait().ifPresent(response -> {
            Controller rootController = parentController;
            while (rootController.getParentController() != null) {
                rootController = parentController.getParentController();
            }
            ((MainController) rootController).joinLobby(response);
        });
    }

    @FXML
    public void onCreate() {
        btnSave.setOnAction(actionEvent -> onSave());
        initVariantPicker();
        switchMode();
    }

    @FXML
    public void onSave() {
        String name = txtName.getText();
        String description = txtDescription.getText();
        CampaignVariant variant = cmbVariants.getValue();
        String notes = txtNotes.getText();

        if (isFilled()) {
            try {
                manipulationService.createCampaign(name, description, variant, notes);
            } catch (IndexAlreadyExistsException | MultiplePlayersException | SessionAlreadyExists e) {
                e.printStackTrace();
            }
            switchMode();
            createTiles();
        } else {
            System.out.println("Error: Missing required fields");
        }
    }

    @FXML
    public void onCancel() {
        txtName.clear();
        txtDescription.clear();
        cmbVariants.valueProperty().set(null);
        txtNotes.clear();

        switchMode();
    }

    public void onEdit(String campaignId) {
        Campaign campaign = null;
        try {
            campaign = listingService.getCampaign(campaignId);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        if (campaign != null) {
            fillForm(campaign);
        }
        btnSave.setOnAction(actionEvent -> onUpdate(campaignId));
        switchMode();
    }

    public void onDelete(String campaignId) {
        try {
            manipulationService.deleteCampaign(campaignId);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        createTiles();
    }

    public void onUpdate(String campaignId) {
        String name = txtName.getText();
        String description = txtDescription.getText();
        CampaignVariant variant = cmbVariants.getValue();
        String notes = txtNotes.getText();

        if (isFilled()) {
            try {
                Campaign campaign = listingService.getCampaign(campaignId);
                campaign.setName(name);
                campaign.setDescription(description);
                campaign.setCampaignVariant(variant);
                campaign.setNotes(notes);
                manipulationService.updateCampaign(campaign);
            } catch (MultiplePlayersException | EntityNotFoundException e) {
                e.printStackTrace();
            }
            switchMode();
            createTiles();
        } else {
            System.out.println("Error: Missing required fields");
        }
    }

    private void fillForm(Campaign campaign) {
        txtName.setText(campaign.getName());
        txtDescription.setText(campaign.getDescription());
        txtNotes.setText(campaign.getNotes());
        if (cmbVariants.getItems().size() == 0 || cmbVariants.getItems() == null) {
            initVariantPicker();
        }
        cmbVariants.setValue(campaign.getCampaignVariant());
    }

    private boolean isFilled() {
        boolean isFilled = true;
        if (txtName.getText().matches("\\s*")) {
            isFilled = false;
        } else if (txtDescription.getText().matches("\\s*")) {
            isFilled = false;
        } else if (cmbVariants.getValue() == null) {
            isFilled = false;
        }
        return isFilled;
    }

    private void switchMode() {
        editor.setVisible(!editor.isVisible());
        overview.setVisible(!overview.isVisible());
    }

    private void initVariantPicker() {
        cmbVariants.setItems(FXCollections.observableList(variantListingService.getAvailableVariants()));
        cmbVariants.setConverter(new StringConverter<>() {
            @Override
            public String toString(CampaignVariant variant) {
                return variant.getName();
            }

            @Override
            public CampaignVariant fromString(String s) {
                return variantListingService.getAvailableVariants().stream().filter(variant ->
                        variant.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
            }
        });
    }

    @Override
    public Controller getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }

    @Override
    public void switchView() {
    }

    @Override
    public void render() {
        createTiles();
    }
}
