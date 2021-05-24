package dev.donkz.pendragon.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class SessionController implements Initializable, Controller {
    @FXML
    private Pane lobbyView;
    @FXML
    private Controller lobbyViewController; // injected via FXML

    private Controller parentController;

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

    public void showLobby() {
        lobbyView.setVisible(true);
    }

    public Controller getLobbyController() {
        return lobbyViewController;
    }
}
