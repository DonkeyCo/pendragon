package dev.donkz.pendragon.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainWindow extends Application {
    private final String SCENE = "/scenes/main_scene.fxml";

    @Override
    public void start(Stage stage) throws Exception {
        Pane root = FXMLLoader.load(getClass().getResource(SCENE));
        Scene mainScene = new Scene(root, 800, 600);

        stage.setScene(mainScene);
        stage.setMaximized(true);
        stage.setTitle("pendragon");
        stage.setIconified(true);
        stage.getIcons().add(new Image("images/pendragon_icon.png"));

        stage.show();
    }
}
