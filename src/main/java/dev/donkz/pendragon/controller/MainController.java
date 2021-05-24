package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * MainController
 *
 * Controller for switching between the main scenes: Management View, Game View.
 */
public class MainController implements Controller {
    @FXML
    private Pane managementView;
    @FXML
    private Pane sessionView;
    @FXML
    private SessionController sessionViewController;
    @FXML
    private ManagementController managementViewController;


    public MainController() {
    }

    @FXML
    public void initialize() {
        managementView.setVisible(true);
        sessionView.setVisible(false);

        sessionViewController.setParentController(this);
        managementViewController.setParentController(this);
    }

    public void switchView() {
        managementView.setVisible(!managementView.isVisible());
        sessionView.setVisible(!sessionView.isVisible());
    }

    public Controller getParentController() {
        return null;
    }

    @Override
    public void setParentController(Controller parentController) {
    }

    public void activateLobby(Campaign campaign) {
        managementView.setVisible(!managementView.isVisible());
        sessionView.setVisible(!sessionView.isVisible());
        sessionViewController.showLobby(campaign);
    }
}
