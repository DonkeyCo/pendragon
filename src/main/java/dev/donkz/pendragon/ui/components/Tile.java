package dev.donkz.pendragon.ui.components;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Tile extends VBox {
    @FXML private Label lblTitle;
    @FXML private Label lblPc;
    @FXML private Label lblDm;
    @FXML private Label lblVariant;

    public Tile() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Tile.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return titleProperty().get();
    }

    public void setTitle(String title) {
        titleProperty().set(title);
    }

    public String getPc() {
        return pcProperty().get();
    }

    public void setPc(String pc) {
        pcProperty().set(pc);
    }

    public String getDm() {
        return dmProperty().get();
    }

    public void setDm(String dm) {
        dmProperty().set(dm);
    }

    public String getVariant() {
        return variantProperty().get();
    }

    public void setVariant(String variant) {
        variantProperty().set(variant);
    }

    public StringProperty titleProperty() {
        return lblTitle.textProperty();
    }

    public StringProperty pcProperty() {
        return lblPc.textProperty();
    }

    public StringProperty dmProperty() {
        return lblDm.textProperty();
    }

    public StringProperty variantProperty() {
        return lblVariant.textProperty();
    }
}
