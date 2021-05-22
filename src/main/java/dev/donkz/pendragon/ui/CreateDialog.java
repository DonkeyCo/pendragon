package dev.donkz.pendragon.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.util.Map;

public class CreateDialog extends DialogPane {
    @FXML
    private Pane content;
    @FXML
    private Label lblTitle;

    private final String title;
    private final Map<String, Region> items;

    public CreateDialog(String title, Map<String, Region> items) {
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
            HBox.setHgrow(row, Priority.ALWAYS);
            row.setSpacing(10);
            row.setAlignment(Pos.CENTER_LEFT);

            row.getChildren().add(new Label(labelText));
            row.getChildren().add(control);

            content.getChildren().add(row);
        });
    }

    public Map<String, Region> getItems() {
        return items;
    }
}
