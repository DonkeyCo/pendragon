package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.exception.infrastructure.*;
import dev.donkz.pendragon.service.SessionService;
import dev.donkz.pendragon.service.WebSocketSessionService;
import dev.donkz.pendragon.util.ControlUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for Sessions/Lobbys
 */
public class SessionController implements Initializable, ViewableController, ControllableSession {
    @FXML
    private Pane lobbyView;
    @FXML
    private Pane selectionView;
    @FXML
    private LobbyController lobbyViewController; // injected via FXML
    @FXML
    private CharacterSessionController selectionViewController;

    private ViewableController parentController;
    private final WebSocketSessionService webSocketSessionService;
    private final SessionService sessionService;

    @Inject
    public SessionController(WebSocketSessionService webSocketSessionService, SessionService sessionService) {
        this.webSocketSessionService = webSocketSessionService;
        this.webSocketSessionService.setControllableSession(this);
        this.sessionService = sessionService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobbyView.setVisible(true);
        selectionView.setVisible(false);
        lobbyViewController.setParentController(this);
        selectionViewController.setParentController(this);
    }

    @Override
    public ViewableController getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(ViewableController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void switchView() {
        lobbyView.setVisible(!lobbyView.isVisible());
        selectionView.setVisible(!selectionView.isVisible());

        if (lobbyView.isVisible()) {
            lobbyViewController.render();
        } else {
            selectionViewController.createTiles();
        }
    }

    @Override
    public void render() {
    }

    /**
     * Show the lobby in a Host perspective
     * @param campaign campaign object
     */
    public void showHostLobby(Campaign campaign) {
        lobbyView.setVisible(true);
        lobbyViewController.openSession(campaign);
        lobbyViewController.render();
    }

    /**
     * Show the lobby in a Participant perspective
     * @param host host room
     */
    public void showJoinLobby(String host) {
        lobbyView.setVisible(true);
        lobbyViewController.joinSession(host);
        lobbyViewController.render();
    }

    /**
     * Connect to a web socket session
     */
    public void connect() {
        try {
            webSocketSessionService.connect();
        } catch (ConnectionException e) {
            e.printStackTrace();
            parentController.getParentController().switchView();
            sessionService.clear();
            new Alert(Alert.AlertType.ERROR, "Could not connect to communication server!").show();
        }
    }

    /**
     * Create a lobby based on the campaign
     * @param campaign campaign object
     */
    public void createLobby(Campaign campaign) {
        try {
            webSocketSessionService.createLobby(campaign);
        } catch (MultiplePlayersException | IndexAlreadyExistsException | SessionAlreadyExists e) {
            e.printStackTrace();
            parentController.getParentController().switchView();
            sessionService.clear();
            new Alert(Alert.AlertType.ERROR, "Could not create lobby!").show();
        }
    }

    /**
     * Join a lobby
     * @param room room name
     */
    public void joinLobby(String room) {
        try {
            webSocketSessionService.joinLobby(room);
        } catch (MultiplePlayersException e) {
            parentController.getParentController().switchView();
            sessionService.clear();
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not join lobby!").show();
        }
    }

    /**
     * Update Session
     * @param session session object
     */
    public void updateSession(Session session) {
        webSocketSessionService.updateSession(session);
    }

    /**
     * Leave the lobby
     * @param session session object
     * @param player player object
     */
    public void leaveLobby(Session session, Player player) {
        try {
            webSocketSessionService.leaveLobby(session.getRoom(), player.getId(), session);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the controller for the lobby
     * @return
     */
    public HierarchicalController getLobbyController() {
        return lobbyViewController;
    }

    @Override
    public void sync() {
        lobbyViewController.render();
    }

    @Override
    public void left(String id) {
        lobbyViewController.playerLeft(id);
        sync();
    }

    @Override
    public void message(String playerName, String message) {
        Label lblSender = ControlUtility.createLabel(playerName + ":", true);
        Label lblMessage = ControlUtility.createLabel(message, false);
        lobbyViewController.getChatBox().getChildren().add(ControlUtility.createRow(lblSender, lblMessage));
    }

    /**
     * Send a message
     * @param message message string
     * @param session session object
     */
    public void sendMessage(String message, Session session) {
        webSocketSessionService.sendMessage(message, session);
    }

    /**
     * Send the roll
     * @param rollMessage roll string
     */
    public void sendRoll(String rollMessage) {
        Session session = sessionService.getCurrentSession();
        webSocketSessionService.sendRoll(rollMessage, session);
    }

    @Override
    public void roll(String rollMessage) {
        lobbyViewController.getChatBox().getChildren().add(ControlUtility.createLabel(rollMessage, false));
    }

    public void joined(Player player) {
        lobbyViewController.playerJoined(player.getUsername());
    }
}
