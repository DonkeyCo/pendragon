package dev.donkz.pendragon.ui;

import javafx.beans.property.BooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.SortedMap;

public class Tile extends VBox {
    @FXML
    private Label lblTitle;
    @FXML
    private VBox tile;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnStart;
    private boolean hasStart;

    private final String objectId;
    private final String title;
    private final SortedMap<String, String> items;

    public Tile(String objectId, String title, SortedMap<String, String> items) {
        load();
        this.objectId = objectId;
        this.title = title;
        this.items = items;
        this.hasStart = false;
        initTile(title, items);
    }

    private void load() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Tile.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTile(String title, SortedMap<String, String> items) {
        lblTitle.setText(title);
        btnStart.setVisible(hasStart);
        items.forEach((label, value) -> {
            HBox row = new HBox();
            row.setSpacing(10);

            Label lblLabel = new Label(label);
            lblLabel.getStyleClass().add("labelName");
            Label lblValue = new Label(value);
            lblValue.getStyleClass().add("textLabel");

            row.getChildren().addAll(lblLabel, lblValue);
            tile.getChildren().add(row);
        });
    }

    public String getObjectId() {
        return objectId;
    }

    public String getTitle() {
        return title;
    }

    public SortedMap<String, String> getItems() {
        return items;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public void setHasStart(boolean hasStart) {
        this.hasStart = hasStart;
        this.btnStart.setVisible(hasStart);
    }
}
