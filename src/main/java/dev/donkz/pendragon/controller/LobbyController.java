package dev.donkz.pendragon.controller;

import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.exception.infrastructure.ConnectionException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.exception.infrastructure.SessionAlreadyExists;
import dev.donkz.pendragon.service.CharacterListingService;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.service.SessionService;
import dev.donkz.pendragon.service.WebSocketSessionService;
import dev.donkz.pendragon.util.ControlUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private final CharacterListingService characterListingService;
    private final WebSocketSessionService webSocketSessionService;
    private final SessionService sessionService;
    private Session session;

    @Inject
    public LobbyController(PlayerManagementService playerManagementService, CharacterListingService characterListingService, WebSocketSessionService webSocketSessionService, SessionService sessionService) {
        this.playerManagementService = playerManagementService;
        this.characterListingService = characterListingService;
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
        try {
            webSocketSessionService.connect();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        try {
            webSocketSessionService.createLobby(campaign);
        } catch (MultiplePlayersException | IndexAlreadyExistsException | SessionAlreadyExists e) {
            e.printStackTrace();
        }
        fillCode();
    }

    public void joinSession(String room) {
        try {
            webSocketSessionService.connect();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        try {
            webSocketSessionService.joinLobby(room);
        } catch (MultiplePlayersException e) {
            e.printStackTrace();
        }
        fillCode();
    }

    public void render() {
        Session session = sessionService.getCurrentSession();
        lblLobbyName.setText("Lobby - " + session.getCampaign().getName());
        Player host = playerManagementService.getRegisteredPlayer();
        lblDm.setText(host.getUsername());

        if (session.getParticipants() != null) {
            session.getParticipants().forEach((playerId, pcId) -> {
                Player participant = playerManagementService.findPlayerById(playerId);
                Pc pc = null;
                try {
                    pc = characterListingService.getPlayerCharacters().stream().filter(p -> p.getId().equalsIgnoreCase(pcId)).findFirst().orElse(null);
                } catch (MultiplePlayersException e) {
                    e.printStackTrace();
                }

                Label lblPlayer = new Label(participant.getUsername());
                Label lblPc = new Label("None");
                if (pc != null) {
                    lblPc.setText(pc.getName());
                }
                HBox row = ControlUtility.createRow(lblPlayer, lblPc);
                membersPane.getChildren().addAll(row);
            });
        }
    }

    public void onCopy() {
        TKClipboard clipboard = Toolkit.getToolkit().getSystemClipboard();
        clipboard.putContent(new Pair<>(DataFormat.PLAIN_TEXT, lblCode.getText()));
    }

    public void onStart() {
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private void fillCode() {
        Session session = null;
        int tries = 0;
        do {
            session = sessionService.getCurrentSession();
            tries++;
            try {
                Thread.sleep(1000*tries);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while(session.getRoom() == null && tries < 5);
        lblCode.setText(session.getRoom());
    }
}
