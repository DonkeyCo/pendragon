package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.domain.variant.*;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.stage.Screen;
import org.controlsfx.control.CheckComboBox;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Character Controller for Session View. Editing/Creating of Characters while in a Lobby
 */
public class CharacterSessionController implements Initializable, ViewableController {
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
    public ViewableController getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(ViewableController parentController) {
        this.parentController = (SessionController) parentController;
    }

    @Override
    public void switchView() {
        parentController.switchView();
    }

    @Override
    public void render() {
        createTiles();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Event Handler for Create Button.
     * Creates a Character Creator
     */
    public void onCreate() {
        Session session = sessionService.getCurrentSession();
        Map<String, Region> items = ControlUtility.createForm(Pc.class);

        ControlUtility.fillComboBox((ComboBox<Kind>) items.get("Kind"), session.getCampaign().getCampaignVariant().getKinds());
        ControlUtility.fillComboBox((ComboBox<Race>) items.get("Race"), session.getCampaign().getCampaignVariant().getRaces());
        ControlUtility.fillCheckComboBox((CheckComboBox<Ability>) items.get("SavingThrows"), Arrays.asList(Ability.values()));
        ControlUtility.fillCheckComboBox((CheckComboBox<Proficiency>) items.get("Proficiencies"), session.getCampaign().getCampaignVariant().getProficiencies());
        ControlUtility.fillCheckComboBox((CheckComboBox<Feature>) items.get("Features"), session.getCampaign().getCampaignVariant().getFeatures());
        ControlUtility.fillCheckComboBox((CheckComboBox<Trait>) items.get("Traits"), session.getCampaign().getCampaignVariant().getTraits());
        ControlUtility.fillCheckComboBox((CheckComboBox<Equipment>) items.get("Equipment"), session.getCampaign().getCampaignVariant().getEquipments());

        Campaign campaign = session.getCampaign();

        Dialog<String> dialog = ControlUtility.createDialog("Create Playable Character", items);
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
                    try {
                        playerManagementService.addOrUpdatePcForRegisteredPlayer(pc);
                        Player player = playerManagementService.getRegisteredPlayer();
                        campaign.addPlayer(player);
                        campaign.addCharacter(pc);
                        if (campaignListingService.campaignExists(campaign.getId())) {
                            manipulationService.updateCampaign(campaign);
                        } else {
                            manipulationService.createCampaign(campaign);
                        }
                    } catch (MultiplePlayersException | EntityNotFoundException | SessionAlreadyExists | IndexAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                    try {
                        playerManagementService.addOrUpdatePcForRegisteredPlayer(pc);
                    } catch (MultiplePlayersException | EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        sessionService.updateParticipant(playerManagementService.getRegisteredPlayer().getId(), pc.getId());
                        sessionService.updateCampaign(campaign);
                    } catch (EntityNotFoundException e) {
                        e.printStackTrace();
                    }
                    render();
                    return pc.toString();
                }
            }
            return null;
        });
    }

    /**
     * Event Handler for Cancel Button
     */
    public void onCancel() {
        parentController.switchView();
    }

    /**
     * Creates the tiles for every available PC
     */
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

        if (pc.getKind() != null) {
            items.put("Class", pc.getKind().shortString());
        }
        if (pc.getRace() != null) {
            items.put("Race", pc.getRace().shortString());
        }
        items.put("Level", String.valueOf(pc.getLevel()));
        return items;
    }
}
