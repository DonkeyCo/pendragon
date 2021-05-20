package dev.donkz.pendragon.util;

import com.sun.javafx.scene.control.IntegerField;
import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.common.Ability;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import java.util.List;

public class ControlUtility {
    public static TextField createTextField(String promptText) {
        TextField field = new TextField();
        field.setPromptText(promptText);

        return field;
    }

    public static TextArea createTextArea(String promptText) {
        TextArea area = new TextArea();
        area.setPromptText(promptText);

        return area;
    }

    public static IntegerField createIntegerField(String promptText) {
        IntegerField field = new IntegerField();
        field.setPromptText(promptText);

        return field;
    }

    public static <T> ComboBox<T> createComboBox(String promptText, List<T> items) {
        ComboBox<T> cmbBox = new ComboBox<>();
        cmbBox.setPromptText(promptText);
        cmbBox.setItems(FXCollections.observableList(items));
        if (items.size() > 0 && (items.get(0) instanceof Printable)) {
            cmbBox.setConverter(stringConverter(items));
        }

        return cmbBox;
    }

    public static <T> CheckComboBox<T> createCheckComboBox(String title, List<T> items) {
        CheckComboBox<T> cmbBox = new CheckComboBox<>();
        cmbBox.setTitle(title);
        cmbBox.getItems().addAll(items);
        if (items.size() > 0 && (items.get(0) instanceof Printable)) {
            cmbBox.setConverter(stringConverter(items));
        }

        return cmbBox;
    }

    private static <T> StringConverter<T> stringConverter(List<T> items) {
        return new StringConverter<>() {
            @Override
            public String toString(T t) {
                return ((Printable) t).shortString();
            }

            @Override
            public T fromString(String s) {
                return items.stream().filter(t -> ((Printable) t).shortString().equalsIgnoreCase(s)).findFirst().orElse(null);
            }
        };
    }
}
