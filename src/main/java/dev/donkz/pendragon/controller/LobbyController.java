package dev.donkz.pendragon.controller;

import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.exception.infrastructure.*;
import dev.donkz.pendragon.service.PlayableCharacterService;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.service.SessionService;
import dev.donkz.pendragon.service.WebSocketSessionService;
import dev.donkz.pendragon.util.ControlUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Pair;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class LobbyController implements Controller, Initializable {
    @FXML
    private Label lblCode;
    @FXML
    private Label lblDm;
    @FXML
    private Pane membersPane;
    @FXML
    private Label lblLobbyName;

    private Controller parentController;
    private final PlayerManagementService playerManagementService;
    private final PlayableCharacterService playableCharacterService;
    private final WebSocketSessionService webSocketSessionService;
    private final SessionService sessionService;
    private Session session;

    @Inject
    public LobbyController(PlayerManagementService playerManagementService, PlayableCharacterService playableCharacterService, WebSocketSessionService webSocketSessionService, SessionService sessionService) {
        this.playerManagementService = playerManagementService;
        this.playableCharacterService = playableCharacterService;
        this.webSocketSessionService = webSocketSessionService;
        this.sessionService = sessionService;
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
        this.parentController = parentController;
    }

    public void switchView() {

    }

    public void openSession(Campaign campaign) {
        sessionService.clear();
        try {
            webSocketSessionService.connect();
        } catch (ConnectionException e) {
            e.printStackTrace();
            parentController.getParentController().switchView();
            sessionService.clear();
            new Alert(Alert.AlertType.ERROR, "Could not connect to communication server!").show();
            return;
        }
        try {
            webSocketSessionService.createLobby(campaign);
        } catch (MultiplePlayersException | IndexAlreadyExistsException | SessionAlreadyExists e) {
            e.printStackTrace();
            parentController.getParentController().switchView();
            sessionService.clear();
            new Alert(Alert.AlertType.ERROR, "Could not create lobby!").show();
            return;
        }
        fillCode();
    }

    public void joinSession(String room) {
        sessionService.clear();
        try {
            webSocketSessionService.connect();
        } catch (ConnectionException e) {
            parentController.getParentController().switchView();
            sessionService.clear();
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not connect to communication server!").show();
            return;
        }
        try {
            webSocketSessionService.joinLobby(room);
        } catch (MultiplePlayersException e) {
            parentController.getParentController().switchView();
            sessionService.clear();
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not join lobby!").show();
            return;
        }
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
                    pc = playableCharacterService.getPlayerCharacters().stream().filter(p -> p.getId().equalsIgnoreCase(pcId)).findFirst().orElse(null);
                } catch (MultiplePlayersException e) {
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
        try {
            webSocketSessionService.leaveLobby(session.getRoom(), player.getId(), session);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
        parentController.getParentController().switchView();
    }

    public void setSession(Session session) {
        this.session = session;
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
        } while((session == null || session.getRoom() == null) && tries <= 10);

        if (session == null || session.getRoom() == null) {
            parentController.getParentController().switchView();
        } else {
            lblCode.setText(session.getRoom());
            lblDm.setText(session.getHost().getUsername());
            render();
        }
    }
}
