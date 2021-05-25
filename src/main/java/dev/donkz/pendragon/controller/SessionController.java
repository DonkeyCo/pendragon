package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.service.SessionManagementService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.processframework.exceptions.InvalidProcessStateException;
import org.hive2hive.processframework.exceptions.ProcessExecutionException;

import javax.inject.Inject;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class SessionController implements Initializable, Controller {
    @FXML
    private Pane lobbyView;
    @FXML
    private LobbyController lobbyViewController; // injected via FXML

    private Controller parentController;
    private final SessionManagementService sessionManagementService;

    @Inject
    public SessionController(SessionManagementService sessionManagementService) {
        this.sessionManagementService = sessionManagementService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobbyView.setVisible(true);
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
        lobbyView.setVisible(!lobbyView.isVisible());
    }

    public void showLobby(Campaign campaign) {
        lobbyView.setVisible(true);
        lobbyViewController.openSession(campaign);
        lobbyViewController.render();
    }

    public void joinLobby(String host) {
        lobbyView.setVisible(true);
        lobbyViewController.joinSession(host);
    }

    public Controller getLobbyController() {
        return lobbyViewController;
    }
}
