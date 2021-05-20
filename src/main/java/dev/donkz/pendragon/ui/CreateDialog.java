package dev.donkz.pendragon.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class CreateDialog extends DialogPane {
    @FXML
    private Pane content;
    @FXML
    private Label lblTitle;

    private final String title;
    private final Map<String, Control> items;

    public CreateDialog(String title, Map<String, Control> items) {
        load();
        this.title = title;
        this.items = items;
        createForm();
    }

    private void load() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/CreateDialog.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createForm() {
        lblTitle.setText(title);
        items.forEach((labelText, control) -> {
            HBox row = new HBox();
            row.setSpacing(10);

            row.getChildren().add(new Label(labelText));
            row.getChildren().add(control);

            content.getChildren().add(row);
        });
    }

    public Map<String, Control> getItems() {
        return items;
    }
}
