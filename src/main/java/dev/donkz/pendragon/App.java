package dev.donkz.pendragon;

import dev.donkz.pendragon.infrastructure.database.local.LocalDriver;
import dev.donkz.pendragon.infrastructure.network.socket.WebSocketCommunicator;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalPlayerRepository;
import dev.donkz.pendragon.infrastructure.persistence.local.LocalSessionRepository;
import dev.donkz.pendragon.service.WebSocketSessionService;
import dev.donkz.pendragon.ui.MainWindow;
import javafx.application.Application;

public class App {
    public static void main(String[] args) {
        Application.launch(MainWindow.class, args);
    }
}
