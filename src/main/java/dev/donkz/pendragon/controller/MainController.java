package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.ui.CreateDialog;
import javafx.fxml.FXML;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

import javax.inject.Inject;
import java.util.Map;

/**
 * MainController
 *
 * Controller for switching between the main scenes: Management View, Game View.
 */
public class MainController implements ViewableController {
    @FXML
    private Pane managementView;
    @FXML
    private Pane sessionView;
    @FXML
    private VBox playerCreationView;
    @FXML
    private SessionController sessionViewController;
    @FXML
    private PlayerCreationController playerCreationViewController;
    @FXML
    private ManagementController managementViewController;

    private PlayerRepository playerRepository;

    @Inject
    public MainController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @FXML
    public void initialize() {
        try {
            if (playerRepository.findRegisteredPlayer() != null) {
                managementView.setVisible(true);
                sessionView.setVisible(false);
                playerCreationView.setVisible(false);
            } else {
                playerCreationView.setVisible(true);
                managementView.setVisible(false);
                sessionView.setVisible(false);
            }
        } catch (MultiplePlayersException e) {
            e.printStackTrace();
        }

        playerCreationViewController.setParentController(this);
        sessionViewController.setParentController(this);
        managementViewController.setParentController(this);
    }

    public void switchView() {
        if (playerCreationView.isVisible()) {
            managementView.setVisible(true);
            sessionView.setVisible(false);
            playerCreationView.setVisible(false);

            managementViewController.render();
        } else {
            managementView.setVisible(!managementView.isVisible());
            sessionView.setVisible(!sessionView.isVisible());
            if (managementView.isVisible()) {
                managementViewController.render();
            } else {
                sessionViewController.render();
            }
        }
    }

    @Override
    public void render() {
    }

    public ViewableController getParentController() {
        return null;
    }

    @Override
    public void setParentController(ViewableController parentController) {
    }

    public void activateLobby(Campaign campaign) {
        managementView.setVisible(!managementView.isVisible());
        sessionView.setVisible(!sessionView.isVisible());
        sessionViewController.showHostLobby(campaign);
    }

    public void joinLobby(String host) {
        managementView.setVisible(!managementView.isVisible());
        sessionView.setVisible(!sessionView.isVisible());
        sessionViewController.showJoinLobby(host);
    }

    private Dialog<String> createDialog(String title, Map<String, Region> items) {
        CreateDialog dialogPane = new CreateDialog(title, items);
        dialogPane.setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 5);
        dialogPane.setMaxWidth(Screen.getPrimary().getBounds().getWidth() / 5);
        dialogPane.setPrefHeight(Screen.getPrimary().getBounds().getHeight() / 1.5);
        dialogPane.setMaxHeight(Screen.getPrimary().getBounds().getHeight() / 1.5);

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setDialogPane(dialogPane);

        return dialog;
    }
}
