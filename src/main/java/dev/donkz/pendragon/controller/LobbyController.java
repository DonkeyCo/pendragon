package dev.donkz.pendragon.controller;

import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import dev.donkz.pendragon.domain.campaign.Campaign;
import dev.donkz.pendragon.domain.player.Player;
import dev.donkz.pendragon.domain.session.Session;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.service.SessionManagementService;
import dev.donkz.pendragon.util.ControlUtility;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import org.hive2hive.core.exceptions.NoPeerConnectionException;
import org.hive2hive.processframework.exceptions.InvalidProcessStateException;
import org.hive2hive.processframework.exceptions.ProcessExecutionException;

import javax.inject.Inject;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class LobbyController implements Controller, Initializable {
    @FXML
    private Label lblCode;
    @FXML
    private Label lblDm;
    @FXML
    private Pane membersPane;
    @FXML
    private Label lblLobbyName;

    private Controller parentController;
    private final SessionManagementService sessionManagementService;
    private final PlayerManagementService playerManagementService;
    private Session session;

    @Inject
    public LobbyController(SessionManagementService sessionManagementService, PlayerManagementService playerManagementService) {
        this.sessionManagementService = sessionManagementService;
        this.playerManagementService = playerManagementService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public Controller getParentController() {
        return parentController;
    }

    @Override
    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }

    public void switchView() {
    }

    public void openSession(Campaign campaign) {
        try {
            session = sessionManagementService.createSession(campaign);
        } catch (NoPeerConnectionException | ProcessExecutionException | InvalidProcessStateException | UnknownHostException e) {
            e.printStackTrace();
        }
        lblCode.setText(session.getHostAddress());
    }

    public void render() {
        lblLobbyName.setText("Lobby - " + session.getCampaign().getName());
        Player host = playerManagementService.getRegisteredPlayer();
        lblDm.setText(host.getUsername());

        if (session.getParticipants() != null) {
            session.getParticipants().forEach((player, pc) -> {
                Label lblPlayer = new Label(player.getUsername());
                Label lblPc = new Label("None");
                if (pc != null) {
                    lblPc.setText(pc.getName());
                }
                HBox row = ControlUtility.createRow(lblPlayer, lblPc);
                membersPane.getChildren().addAll(row);
            });
        }
    }

    public void onCopy() {
        TKClipboard clipboard = Toolkit.getToolkit().getSystemClipboard();
        clipboard.putContent(new Pair<>(DataFormat.PLAIN_TEXT, lblCode.getText()));
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
