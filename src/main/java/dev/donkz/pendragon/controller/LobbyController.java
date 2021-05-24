package dev.donkz.pendragon.controller;

import javafx.fxml.FXML;

import javax.inject.Inject;

public class LobbyController implements Controller {

    private Controller parentController;

    @Inject
    public LobbyController() {

    }

    @FXML
    public void initialize() {
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
}
