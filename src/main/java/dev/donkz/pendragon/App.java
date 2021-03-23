package dev.donkz.pendragon;

import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPlayerRepository;
import dev.donkz.pendragon.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World");
        Application.launch(MainWindow.class, args);
    }
}
