package dev.donkz.pendragon.controller;

import com.sun.javafx.scene.control.IntegerField;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.player.DecreaseAbilityCommand;
import dev.donkz.pendragon.domain.player.IncreaseAbilityCommand;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerCommand;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.domain.variant.*;
import dev.donkz.pendragon.exception.infrastructure.*;
import dev.donkz.pendragon.service.*;
import dev.donkz.pendragon.ui.CreateDialog;
import dev.donkz.pendragon.util.ControlUtility;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.util.Pair;
import org.controlsfx.control.CheckComboBox;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

public class LobbyController implements RenderableController, HierarchicalController<ViewableController>, Initializable {
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
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnChoose;
    @FXML
    private Button btnScore;

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
    public ViewableController getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(ViewableController parentController) {
        this.parentController = (SessionController) parentController;
    }

    public void openSession(Campaign campaign) {
        sessionService.clear();
        parentController.connect();
        parentController.createLobby(campaign);
        btnEdit.setVisible(false);
        btnChoose.setVisible(false);
        btnScore.setVisible(false);
        render();
    }

    public void joinSession(String room) {
        sessionService.clear();
        parentController.connect();
        parentController.joinLobby(room);
        btnEdit.setVisible(true);
        btnChoose.setVisible(true);
        btnScore.setVisible(true);
        render();
    }

    public void render() {
        fillCode();
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
        Dialog<String> dialog = ControlUtility.createDialog("Edit Character Sheet", items);
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
                Player player = playerManagementService.getRegisteredPlayer();
                try {
                    pc = ControlUtility.controlsToValues(Pc.class, items);
                    pc.setId(editedPc.getId());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (pc != null) {
                    player.addCharacter(pc);
                    campaign.addPlayer(player);
                    campaign.addCharacter(pc);
                    try {
                        playableCharacterService.updatePlayerCharacter(pc);
                        playerManagementService.addOrUpdatePcForRegisteredPlayer(pc);
                    } catch (EntityNotFoundException | MultiplePlayersException sessionAlreadyExists) {
                        sessionAlreadyExists.printStackTrace();
                    }
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
                        sessionService.updateParticipant(playerManagementService.getRegisteredPlayer().getId(), pc.getId());
                        sessionService.updateCampaign(campaign);
                    } catch (EntityNotFoundException e) {
                        e.printStackTrace();
                    }

                    render();
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

    public void onAbility() {
        Session session = sessionService.getCurrentSession();

        Map<String, Region> items = new LinkedHashMap<>();
        ComboBox<Ability> cmbAbility = ControlUtility.createComboBox("Choose an ability", "cmbAbility");
        ControlUtility.fillComboBox(cmbAbility, Arrays.asList(Ability.values()));
        ComboBox<String> cmbOperation = ControlUtility.createComboBox("Choose an operation", "cmbOperation");
        ControlUtility.fillComboBox(cmbOperation, Arrays.asList("Increase", "Decrease"));
        items.put("Ability Type", cmbAbility);
        items.put("Amount", ControlUtility.createIntegerField("Input an amount", "intAmount"));
        items.put("Operation Type", cmbOperation);
        Dialog<String> dialog = ControlUtility.createDialog("Increase/Decrease Ability", items);
        dialog.show();
        dialog.setOnShowing(dialogEvent -> dialog.setResult(null));

        dialog.setResultConverter(buttonType -> {
            Ability ability = ((ComboBox<Ability>) items.get("Ability Type")).getValue();
            int amount = ((IntegerField) items.get("Amount")).getValue();
            String operation = ((ComboBox<String>) items.get("Operation Type")).getValue();
            String pcId = session.getParticipants().get(playerManagementService.getRegisteredPlayer().getId());
            Pc editedPc;
            try {
                editedPc = playableCharacterService.getPlayerCharacter(pcId);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
                return null;
            }

            PlayerCommand command = null;
            if (operation.equalsIgnoreCase("Increase")) {
                 command = new IncreaseAbilityCommand(editedPc, ability, amount);
            } else {
                command = new DecreaseAbilityCommand(editedPc, ability, amount);
            }
            command.execute();
            try {
                playableCharacterService.updatePlayerCharacter(editedPc);
            } catch (EntityNotFoundException e) {
                e.printStackTrace();
            }
            return "";
        });
    }

    private void fillCode() {
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
        } while((session == null || session.getRoom() == null) && tries <= 5);

        if (session == null || session.getRoom() == null) {
            parentController.getParentController().switchView();
        } else {
            lblCode.setText(session.getRoom());
            lblDm.setText(session.getHost().getUsername());
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
