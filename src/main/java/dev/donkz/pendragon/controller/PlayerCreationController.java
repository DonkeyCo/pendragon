package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class PlayerCreationController implements Initializable, ViewableController {
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtProfile;

    private ViewableController parentController;
    private PlayerRepository playerRepository;

    @Inject
    public PlayerCreationController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void onCreate() {
        Player player;
        try {
            player = Player.builder().username(txtUsername.getText()).profileIconUrl(txtProfile.getText()).build();
        } catch (RequiredAttributeMissingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Username is missing.");
            alert.show();
            return;
        }
        try {
            playerRepository.saveClient(player);
        } catch (IndexAlreadyExistsException e) {
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

    @Override
    public void switchView() {
    }

    @Override
    public void render() {
    }
}
