package dev.donkz.pendragon.ui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.plaf.synth.Region;
import java.util.HashMap;

public class Router {
    private final Scene scene;
    private HashMap<String, Pane> subRouters;
    private HashMap<String, HashMap<String, Parent>> subMapping;
    private HashMap<String, Parent> mapping;

    public Router(Scene scene) {
        this.scene = scene;
        this.subRouters = new HashMap<>();
        this.mapping = new HashMap<>();
        this.subMapping = new HashMap<>();
    }

    public void registerRoute(String route, Parent p) {
        mapping.put(route, p);
    }

    public void unregisterRoute(String route) {
        mapping.remove(route);
    }

    public void goTo(String route) {
        scene.setRoot(mapping.get(route));
    }

    public void registerSubRouter(String name, Pane root) {
        subRouters.put(name, root);
    }

    public void unregisterSubRouter(String name) {
        subRouters.remove(name);
    }

    public void registerSubRoute(String routerName, String route, Pane p) {
        if(!subMapping.containsKey(routerName)) {
            subMapping.put(routerName, new HashMap<>());
        }
        subMapping.get(routerName).put(route, p);
    }

    public void unregisterSubRoute(String routerName, String route) {
        if (subMapping.containsKey(routerName)) {
            subMapping.get(routerName).remove(route);
        }
    }

    public void goToSub(String router, String subRoute) {
        subRouters.get(router).getChildren().setAll(subMapping.get(router).get(subRoute));
    }
}
