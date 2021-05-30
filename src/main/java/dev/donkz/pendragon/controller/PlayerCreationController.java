package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import dev.donkz.pendragon.service.PlayerManagementService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerCreationController implements Initializable, HierarchicalController<ViewableController> {
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtProfile;

    private ViewableController parentController;
    private PlayerManagementService playerManagementService;

    @Inject
    public PlayerCreationController(PlayerManagementService playerManagementService) {
        this.playerManagementService = playerManagementService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Event Handler for Create Button. Creates a registered player
     */
    public void onCreate() {
        try {
            playerManagementService.createPlayer(txtUsername.getText(), txtProfile.getText());
        } catch (RequiredAttributeMissingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error occurred during saving.");
            alert.show();
        }
        parentController.switchView();
    }

    @Override
    public ViewableController getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(ViewableController parentController) {
        this.parentController = parentController;
    }
}
