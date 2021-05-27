package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.infrastructure.SessionAlreadyExists;
import dev.donkz.pendragon.service.*;
import dev.donkz.pendragon.ui.CreateDialog;
import dev.donkz.pendragon.ui.Tile;
import dev.donkz.pendragon.util.ControlUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CharacterSessionController implements Initializable, Controller {
    @FXML
    private TilePane tilePane;

    private SessionController parentController;
    private final PlayableCharacterService characterService;
    private final CampaignManipulationService manipulationService;
    private final SessionService sessionService;
    private final PlayerManagementService playerManagementService;
    private final CampaignListingService campaignListingService;

    @Inject
    public CharacterSessionController(PlayableCharacterService characterService, CampaignManipulationService manipulationService, SessionService sessionService, PlayerManagementService playerManagementService, CampaignListingService campaignListingService) {
        this.characterService = characterService;
        this.manipulationService = manipulationService;
        this.sessionService = sessionService;
        this.playerManagementService = playerManagementService;
        this.campaignListingService = campaignListingService;
    }

    @Override
    public Controller getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(Controller parentController) {
        this.parentController = (SessionController) parentController;
    }

    @Override
    public void switchView() {
        parentController.switchView();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void onCreate() {
        Map<String, Region> items = ControlUtility.createForm(Pc.class);
        // ControlUtility.fillComboBox((ComboBox<Ability>) items.get("Type"), Arrays.asList(Ability.values()));
        Session session = sessionService.getCurrentSession();
        Campaign campaign = session.getCampaign();


        Dialog<String> dialog = createDialog("Create Proficiency", items);
        dialog.show();

        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Pc pc = null;
                try {
                    pc = ControlUtility.controlsToValues(Pc.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (pc != null) {
                    try {
                        characterService.createPlayerCharacter(pc);
                    } catch (SessionAlreadyExists | IndexAlreadyExistsException sessionAlreadyExists) {
                        sessionAlreadyExists.printStackTrace();
                    }
                    campaign.addCharacter(pc);
                    try {
                        if (campaignListingService.campaignExists(campaign.getId())) {
                            manipulationService.updateCampaign(campaign);
                        } else {
                            manipulationService.createCampaign(campaign);
                        }
                    } catch (MultiplePlayersException | EntityNotFoundException | SessionAlreadyExists | IndexAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                    try {
                        playerManagementService.addPcForRegisteredPlayer(pc);
                    } catch (MultiplePlayersException | EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        sessionService.updateParticipant(playerManagementService.getRegisteredPlayer().getId(), pc.getId());
                        sessionService.updateCampaign(campaign);
                    } catch (EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                    createTiles();
                    return pc.toString();
                }
            }
            return null;
        });
    }

    public void onCancel() {
        parentController.switchView();
    }

    public void createTiles() {
        Session session = sessionService.getCurrentSession();
        Campaign campaign = session.getCampaign();

        tilePane.getChildren().clear();
        List<Pc> pcs = null;
        try {
            pcs = characterService.getPlayerCharacters();
        } catch (MultiplePlayersException e) {
            e.printStackTrace();
        }

        if (pcs != null) {
            pcs = pcs.stream().filter(pc -> campaign.getPcs().stream().anyMatch(
                    campaignPc -> campaignPc.getId().equalsIgnoreCase(pc.getId())
            )).collect(Collectors.toList());

            for (Pc pc : pcs) {
                SortedMap<String, String> items = getTileItems(pc);
                Tile tile = new Tile(pc.getId(), pc.getName(), items);
                tile.setOnMouseClicked(mouseEvent -> {
                    try {
                        sessionService.updateParticipant(playerManagementService.getRegisteredPlayer().getId(), tile.getObjectId());
                    } catch (EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                    parentController.updateSession(sessionService.getCurrentSession());
                    parentController.switchView();
                });
                tilePane.getChildren().add(tile);
            }
        }
    }

    /**
     * Creates the items map, which contains all Label/Value pairs to create a tile
     *
     * @param pc playable character
     * @return map of Label/Value pairs for tile
     */
    private SortedMap<String, String> getTileItems(Pc pc) {
        SortedMap<String, String> items = new TreeMap<>();
        items.put("Class", pc.getKind());
        items.put("Race", pc.getRace());
        items.put("Level", String.valueOf(pc.getLevel()));
        return items;
    }

    private Dialog<String> createDialog(String title, Map<String, Region> items) {
        CreateDialog dialogPane = new CreateDialog(title, items);
        dialogPane.setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 5);
        dialogPane.setMaxWidth(Screen.getPrimary().getBounds().getWidth() / 5);
        dialogPane.setPrefHeight(Screen.getPrimary().getBounds().getHeight() / 1.5);
        dialogPane.setMaxHeight(Screen.getPrimary().getBounds().getHeight() / 1.5);

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setDialogPane(dialogPane);

        return dialog;
    }
}
