package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.session.HostService;
import javafx.fxml.FXML;

import javax.inject.Inject;

public class LobbyController implements Controller {

    private final HostService hostService;
    private Controller parentController;

    @Inject
    public LobbyController(HostService hostService) {
        this.hostService = hostService;
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
