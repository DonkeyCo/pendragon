package dev.donkz.pendragon.controller;

import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.domain.variant.*;
import dev.donkz.pendragon.exception.infrastructure.*;
import dev.donkz.pendragon.service.*;
import dev.donkz.pendragon.ui.CreateDialog;
import dev.donkz.pendragon.util.ControlUtility;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.util.Pair;
import org.controlsfx.control.CheckComboBox;

import javax.inject.Inject;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import java.util.*;

public class LobbyController implements Controller, Initializable {
    @FXML
    private Label lblCode;
    @FXML
    private Label lblDm;
    @FXML
    private Pane membersPane;
    @FXML
    private Label lblLobbyName;
    @FXML
    private TextField txtMessage;
    @FXML
    private Pane chatBox;

    private SessionController parentController;
    private final PlayerManagementService playerManagementService;
    private final PlayableCharacterService playableCharacterService;
    private final CampaignListingService campaignListingService;
    private final SessionService sessionService;
    private final CampaignManipulationService manipulationService;
    private final DiceService diceService;

    @Inject
    public LobbyController(PlayerManagementService playerManagementService, PlayableCharacterService playableCharacterService, SessionService sessionService, CampaignListingService campaignListingService, CampaignManipulationService manipulationService, DiceService diceService) {
        this.playerManagementService = playerManagementService;
        this.playableCharacterService = playableCharacterService;
        this.sessionService = sessionService;
        this.campaignListingService = campaignListingService;
        this.manipulationService = manipulationService;
        this.diceService = diceService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public Controller getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(Controller parentController) {
        this.parentController = (SessionController) parentController;
    }

    public void switchView() {
    }

    public void openSession(Campaign campaign) {
        sessionService.clear();
        parentController.connect();
        parentController.createLobby(campaign);
        fillCode();
    }

    public void joinSession(String room) {
        sessionService.clear();
        parentController.connect();
        parentController.joinLobby(room);
        fillCode();
    }

    public void render() {
        Session session = sessionService.getCurrentSession();
        lblLobbyName.setText("Lobby - " + session.getCampaign().getName());
        Player host = session.getHost();
        lblDm.setText(host.getUsername());

        membersPane.getChildren().clear();
        if (session.getParticipants() != null) {
            session.getParticipants().forEach((playerId, pcId) -> {
                Player participant = playerManagementService.findPlayerById(playerId);
                Pc pc = null;
                try {
                    List<Pc> pcs = playableCharacterService.getPlayerCharacters(playerId);
                    pc = pcs.stream().filter(p -> p.getId().equalsIgnoreCase(pcId)).findFirst().orElse(null);
                } catch (EntityNotFoundException e) {
                    e.printStackTrace();
                }

                Label lblPlayer = new Label(participant.getUsername());
                Label lblPc = new Label("No Character");
                if (pc != null) {
                    lblPc.setText(pc.getName());
                }
                HBox row = ControlUtility.createRow(lblPlayer, lblPc);
                row.setSpacing(15);
                membersPane.getChildren().addAll(row);
            });
        }
    }

    public void onCopy() {
        TKClipboard clipboard = Toolkit.getToolkit().getSystemClipboard();
        clipboard.putContent(new Pair<>(DataFormat.PLAIN_TEXT, lblCode.getText()));
    }

    public void onChoose() {
        parentController.switchView();
    }

    public void onExit() {
        Session session = sessionService.getCurrentSession();
        Player player = playerManagementService.getRegisteredPlayer();

        parentController.leaveLobby(session, player);
        parentController.getParentController().switchView();
    }

    public void onSend() {
        Player player = playerManagementService.getRegisteredPlayer();

        parentController.sendMessage(txtMessage.getText(), sessionService.getCurrentSession());
        parentController.message(player.getUsername(), txtMessage.getText());
    }

    public void onChange() {
        Session session = sessionService.getCurrentSession();
        String pcId = session.getParticipants().get(playerManagementService.getRegisteredPlayer().getId());
        Map<String, Region> items;
        Pc editedPc;
        try {
            editedPc = playableCharacterService.getPlayerCharacter(pcId);
            items = ControlUtility.createForm(Pc.class, editedPc);
        } catch (EntityNotFoundException | IllegalAccessException e) {
            return;
        }
        Dialog<String> dialog = createDialog("Edit Character Sheet", items);
        dialog.show();

        ControlUtility.fillComboBox((ComboBox<Kind>) items.get("Kind"), session.getCampaign().getCampaignVariant().getKinds());
        ControlUtility.fillComboBox((ComboBox<Race>) items.get("Race"), session.getCampaign().getCampaignVariant().getRaces());
        ControlUtility.fillCheckComboBox((CheckComboBox<Ability>) items.get("SavingThrows"), Arrays.asList(Ability.values()));
        ControlUtility.fillCheckComboBox((CheckComboBox<Proficiency>) items.get("Proficiencies"), session.getCampaign().getCampaignVariant().getProficiencies());
        ControlUtility.fillCheckComboBox((CheckComboBox<Feature>) items.get("Features"), session.getCampaign().getCampaignVariant().getFeatures());
        ControlUtility.fillCheckComboBox((CheckComboBox<Trait>) items.get("Traits"), session.getCampaign().getCampaignVariant().getTraits());
        ControlUtility.fillCheckComboBox((CheckComboBox<Equipment>) items.get("Equipment"), session.getCampaign().getCampaignVariant().getEquipments());


        Campaign campaign = session.getCampaign();
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Pc pc = null;
                try {
                    pc = ControlUtility.controlsToValues(Pc.class, items);
                    pc.setId(editedPc.getId());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (pc != null) {
                    try {
                        playableCharacterService.updatePlayerCharacter(pc);
                    } catch (EntityNotFoundException sessionAlreadyExists) {
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
                    fillCode();
                    parentController.updateSession(sessionService.getCurrentSession());
                    return pc.toString();
                }
            }
            return null;
        });
    }

    public void onRoll(Event event) {
        MenuItem node = (MenuItem) event.getSource();
        int rollMax = Integer.parseInt((String) node.getUserData());
        int roll = diceService.rollDice(rollMax);

        Player player = playerManagementService.getRegisteredPlayer();
        String rollMessage = String.format("%s rolled a D%d. Rolled a %d.", player.getUsername(), rollMax, roll);
        parentController.sendRoll(rollMessage);
        parentController.roll(rollMessage);
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

    public void fillCode() {
        Session session = null;
        int tries = 0;
        do {
            tries++;
            session = sessionService.getCurrentSession();
            try {
                Thread.sleep(1000*tries);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while((session == null || session.getRoom() == null) && tries <= 10);

        if (session == null || session.getRoom() == null) {
            parentController.getParentController().switchView();
        } else {
            lblCode.setText(session.getRoom());
            lblDm.setText(session.getHost().getUsername());
            render();
        }
    }

    public void playerLeft(String playerId) {
        Player player = playerManagementService.findPlayerById(playerId);
        if (player != null) {
            chatBox.getChildren().add(ControlUtility.createLabel(String.format("%s left the lobby.", player.getUsername()), true));
        }
    }

    public void playerJoined(String playerName) {
        chatBox.getChildren().add(ControlUtility.createLabel(String.format("%s joined the lobby.", playerName), true));
    }

    public Pane getChatBox() {
        return chatBox;
    }
}
