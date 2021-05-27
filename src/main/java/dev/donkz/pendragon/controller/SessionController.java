package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class SessionController implements Initializable, Controller {
    @FXML
    private Pane lobbyView;
    @FXML
    private Pane selectionView;
    @FXML
    private LobbyController lobbyViewController; // injected via FXML
    @FXML
    private CharacterSessionController selectionViewController;

    private Controller parentController;

    @Inject
    public SessionController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lobbyView.setVisible(true);
        selectionView.setVisible(false);
        lobbyViewController.setParentController(this);
        selectionViewController.setParentController(this);
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
        selectionView.setVisible(!selectionView.isVisible());

        if (lobbyView.isVisible()) {
            lobbyViewController.render();
        } else {
            selectionViewController.createTiles();
        }
    }

    public void showLobby(Campaign campaign) {
        lobbyView.setVisible(true);
        lobbyViewController.openSession(campaign);
        lobbyViewController.render();
    }

    public void joinLobby(String host) {
        lobbyView.setVisible(true);
        lobbyViewController.joinSession(host);
        lobbyViewController.render();
    }

    public Controller getLobbyController() {
        return lobbyViewController;
    }
}
