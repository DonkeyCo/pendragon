package dev.donkz.pendragon.ui;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.donkz.pendragon.config.StandardModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainWindow extends Application {
    private final String SCENE = "/scenes/main_scene.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        Injector injector = Guice.createInjector(new StandardModule());

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setControllerFactory(injector::getInstance);
        fxmlLoader.setLocation(getClass().getResource(SCENE));

        Parent root = fxmlLoader.load();
        Scene mainScene = new Scene(root, 800, 600);

        stage.setScene(mainScene);
        stage.setMaximized(true);
        stage.setTitle("pendragon");
        stage.setIconified(true);
        stage.getIcons().add(new Image("images/pendragon_icon.png"));

        stage.show();
    }
}
