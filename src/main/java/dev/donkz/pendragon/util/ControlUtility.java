package dev.donkz.pendragon.util;

import com.sun.javafx.scene.control.IntegerField;
import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.character.AbilityScore;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.common.PriceUnit;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;

public class ControlUtility {
    public static Label createLabel(String text, boolean isBold) {
        Label label = new Label(text);
        FontWeight weight = isBold ? FontWeight.BOLD : FontWeight.NORMAL;
        label.setFont(Font.font("System", weight, 15));

        return label;
    }

    public static TextField createTextField(String promptText, String id) {
        TextField field = new TextField();
        HBox.setHgrow(field, Priority.ALWAYS);
        field.setPromptText(promptText);
        field.setId(id);

        return field;
    }

    public static TextArea createTextArea(String promptText, String id) {
        TextArea area = new TextArea();
        HBox.setHgrow(area, Priority.ALWAYS);
        area.setPromptText(promptText);
        area.setId(id);

        return area;
    }

    public static IntegerField createIntegerField(String promptText, String id) {
        IntegerField field = new IntegerField();
        HBox.setHgrow(field, Priority.ALWAYS);
        field.setPromptText(promptText);
        field.setId(id);

        return field;
    }

    public static <T> ComboBox<T> createComboBox(String promptText, String id) {
        ComboBox<T> cmbBox = new ComboBox<>();
        HBox.setHgrow(cmbBox, Priority.ALWAYS);
        cmbBox.setPromptText(promptText);
        cmbBox.setId(id);

        return cmbBox;
    }

    public static <T> CheckComboBox<T> createCheckComboBox(String title, String id) {
        CheckComboBox<T> cmbBox = new CheckComboBox<>();
        HBox.setHgrow(cmbBox, Priority.ALWAYS);
        cmbBox.setTitle(title);
        cmbBox.setId(id);
        cmbBox.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T t) {
                if (t instanceof Printable) {
                    return ((Printable) t).shortString();
                }
                return t.toString();
            }

            @Override
            public T fromString(String s) {
                return null;
            }
        });

        return cmbBox;
    }

    public static HBox createRow(Region... regions) {
        HBox row = new HBox();
        row.getChildren().addAll(regions);
        row.setSpacing(10);

        return row;
    }

    public static <T> Map<String, Region> createForm(Class<T> tClass) {
        Map<String, Region> elements = new LinkedHashMap<>();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String labelName = capitalize(fieldName);

            if (labelName.equalsIgnoreCase("Id")) {
                continue;
            }

            if ((field.getType().toString().contains("List"))) {
                String type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName();
                if (type.contains("String")) {
                    elements.put(labelName, createTextField("Provide " + labelName + " (separate by comma)", "txt" + labelName));
                } else {
                    elements.put(labelName, createCheckComboBox("Choose " + labelName, "cmb" + labelName));
                }
            } else if (field.getType().toString().contains("float") || field.getType().toString().contains("int")) {
                elements.put(labelName, createIntegerField("", "int" + labelName));
            } else if ((field.getType().toString().contains("String"))) {
                elements.put(labelName, createTextField("", "txt" + labelName));
            } else if (field.getType().toString().contains("AbilityScore")) {
                elements.put(labelName, createIntegerField("", "int" + labelName));
            } else if (field.getType().toString().contains("PriceUnit")) {
                elements.put(labelName, createComboBox("", "cmb" + labelName));
            } else {
                elements.put(labelName, createComboBox("", "cmb" + labelName));
            }
            field.setAccessible(false);
        }
        return elements;
    }

    public static <T> T controlsToValues(Class<T> tClass, Map<String, Region> items) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
        Field[] fields = tClass.getDeclaredFields();

        T object = tClass.getDeclaredConstructor().newInstance();
        for (Field field : fields) {
            field.setAccessible(true);
            String labelName = capitalize(field.getName());

            if (labelName.equalsIgnoreCase("Id")) {
                continue;
            }

            if ((field.getType().toString().contains("List"))) {
                String type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName();
                if (type.contains("String")) {
                    List<String> value = Arrays.asList(((TextField) items.get(labelName)).getText().split(","));
                    field.set(object, value);
                } else {
                    List<Object> cmbBox = ((CheckComboBox<Object>) items.get(labelName)).getCheckModel().getCheckedItems();
                    Class<?> classType = Class.forName(type);
                    field.set(object, cmbBox.stream().map(classType::cast).collect(Collectors.toList()));
                }

            } else if (field.getType().toString().contains("float") || field.getType().toString().contains("int")) {
                int value = ((IntegerField) items.get(labelName)).getValue();
                field.set(object, value);
            } else if ((field.getType().toString().contains("String"))) {
                String value = ((TextField) items.get(labelName)).getText();
                field.set(object, value);
            } else if (field.getType().toString().contains("AbilityScore")) {
                int value = ((IntegerField) items.get(labelName)).getValue();
                field.set(object, new AbilityScore(value));
            } else if (field.getType().toString().contains("PriceUnit")) {
                PriceUnit value = ((ComboBox<PriceUnit>) items.get(labelName)).getValue();
                field.set(object, value);
            } else {
                Class<?> objectClass = field.getType();
                Object value = ((ComboBox<Object>) items.get(labelName)).getValue();
                field.set(object, objectClass.cast(value));
            }

            field.setAccessible(false);
        }
        return object;
    }

    public static <T> void fillComboBox(ComboBox<T> comboBox, List<T> content) {
        comboBox.setItems(FXCollections.observableList(content));
    }

    public static <T> void fillCheckComboBox(CheckComboBox<T> comboBox, List<T> content) {
        comboBox.getItems().addAll(content);
    }

    public static <T> void fillField(TextInputControl field, String content) {
        field.setText(content);
    }

    private static String capitalize(String word) {
        String firstLetter = word.substring(0, 1);
        String remainingLetters = word.substring(1);

        return firstLetter.toUpperCase() + remainingLetters;
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
