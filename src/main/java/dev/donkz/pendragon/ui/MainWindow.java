package dev.donkz.pendragon.ui;

import dev.donkz.pendragon.controller.HomeController;
import dev.donkz.pendragon.controller.PlayerCreationController;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPlayerRepository;
import dev.donkz.pendragon.service.PlayerManagementService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Router router = new Router(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/PlayerCreationScene.fxml"));
        loader.setControllerFactory(classType -> new PlayerCreationController(new LocalPlayerRepository(), router));
        router.registerRoute("playerCreation", loader.load());

        loader = new FXMLLoader(getClass().getResource("/scenes/HomeScreen.fxml"));
        loader.setControllerFactory(classType -> new HomeController(router));
        router.registerRoute("home", loader.load());

        PlayerManagementService service = new PlayerManagementService(new LocalPlayerRepository());
        if (service.playerExists()) {
            router.goTo("home");
        } else {
            router.goTo("playerCreation");
        }

        primaryStage.getIcons().add(new Image(getClass().getResource("/images/pendragon_icon.png").toExternalForm()));
        primaryStage.setMaximized(true);
        primaryStage.setTitle("pendragon");
        primaryStage.show();
    }
}
