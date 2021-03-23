package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.player.PlayerRepository;
import dev.donkz.pendragon.exception.model.RequiredAttributeMissingException;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.ui.Router;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.textfield.CustomTextField;

public class PlayerCreationController {
    private final PlayerManagementService playerService;
    private final Router router;

    @FXML
    private CustomTextField txtUsername;
    @FXML
    private GridPane root;

    public PlayerCreationController(PlayerRepository repository, Router router) {
        this.playerService = new PlayerManagementService(repository);
        this.router = router;
    }

    @FXML
    public void initialize() {
        Platform.runLater(() -> root.requestFocus());
    }

    @FXML
    public void onSubmit(Event event) {
        String username = txtUsername.getText();
        if (username.matches("\\s*")) {
            System.out.println("Nothing");
        } else {
            try {
                playerService.createPlayer(txtUsername.getText());
            } catch (RequiredAttributeMissingException e) {
                e.printStackTrace();
            }
            router.goTo("home");
        }
    }
}
