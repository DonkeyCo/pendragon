package dev.donkz.pendragon.ui;

import dev.donkz.pendragon.controller.HomeController;
import dev.donkz.pendragon.controller.PlayerCreationController;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPlayerRepository;
import dev.donkz.pendragon.service.PlayerManagementService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainWindow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene rootScene = new Scene(new Pane());
        Router router = new Router(rootScene);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/PlayerCreationScene.fxml"));
        loader.setControllerFactory(classType -> new PlayerCreationController(new LocalPlayerRepository(), router));

        router.registerRoute("playerCreation", loader.load());

        loader = new FXMLLoader(getClass().getResource("/scenes/HomeScreen.fxml"));
        loader.setControllerFactory(classType -> new HomeController(router));
        router.registerRoute("home", loader.load());

        PlayerManagementService service = new PlayerManagementService(new LocalPlayerRepository());
        if (service.isPlayerRegistered()) {
            router.goTo("home");
        } else {
            System.out.println("TE>ST");
            router.goTo("playerCreation");
        }

        primaryStage.getIcons().add(new Image(getClass().getResource("/images/pendragon_icon.png").toExternalForm()));
        primaryStage.setMaximized(true);
        primaryStage.setScene(rootScene);
        primaryStage.show();
    }
}
