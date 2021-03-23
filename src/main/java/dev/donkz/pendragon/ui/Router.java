package dev.donkz.pendragon.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class Router {
    private final Stage stage;
    private HashMap<String, Parent> mapping;

    public Router(Stage stage) {
        this.stage = stage;
        this.mapping = new HashMap<>();
    }

    public void registerRoute(String route, Parent p) {
        mapping.put(route, p);
    }

    public void unregisterRoute(String route) {
        mapping.remove(route);
    }

    public void goTo(String route) {
        if (stage.getScene() != null) {
            stage.getScene().setRoot(mapping.get(route));
        } else {
            stage.setScene(new Scene(mapping.get(route)));
        }
    }
}
