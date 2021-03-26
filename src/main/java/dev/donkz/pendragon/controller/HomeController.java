package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.ui.Router;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class HomeController {
    public static final String SUB_ROUTER = "menu";
    private final Router router;

    @FXML
    private VBox content;
    @FXML
    private Pane contentRoot;
    @FXML
    private ToggleGroup sidebarGroup;
    @FXML
    private RadioButton rbtnCampaign;
    @FXML
    private RadioButton rbtnCharacter;
    @FXML
    private SplitPane root;

    public HomeController(Router router) {
        this.router = router;
    }

    @FXML
    public void initialize() {
        try {
            registerRoutes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> root.requestFocus());
        ChangeListener<Number> changeListener = (observable, oldValue, newValue) -> root.setDividerPositions(0.15);
        root.widthProperty().addListener(changeListener);
        root.heightProperty().addListener(changeListener);

        rbtnCampaign.getStyleClass().remove("radio-button");
        rbtnCharacter.getStyleClass().remove("radio-button");
        rbtnCampaign.getStyleClass().add("toggle-button");
        rbtnCharacter.getStyleClass().add("toggle-button");

        sidebarGroup.selectToggle(rbtnCampaign);
        VBox.setVgrow(content, Priority.ALWAYS);

        router.goToSub(SUB_ROUTER, "campaigns");
    }

    @FXML
    public void onCharacters(Event event) {
    }

    private void registerRoutes() throws Exception {
        router.registerSubRouter(SUB_ROUTER, contentRoot);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/CampaignPanel.fxml"));
        loader.setControllerFactory(classType -> new CampaignController(router));
        router.registerSubRoute(SUB_ROUTER, "campaigns", loader.load());
    }

}
