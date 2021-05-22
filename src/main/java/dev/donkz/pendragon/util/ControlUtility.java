package dev.donkz.pendragon.util;

import com.sun.javafx.reflect.ReflectUtil;
import com.sun.javafx.scene.control.IntegerField;
import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.character.AbilityScore;
import dev.donkz.pendragon.domain.common.Ability;
import impl.org.controlsfx.ReflectionUtils;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ControlUtility {
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

        return cmbBox;
    }

    public static HBox createRow(Region... regions) {
        HBox row = new HBox();
        row.getChildren().addAll(regions);

        return row;
    }

    public static <T> Map<String, Region> createForm(Class<T> tClass) {
        Map<String, Region> elements = new LinkedHashMap<>();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            String labelName = capitalize(fieldName);

            if ((field.getType().toString().contains("List"))) {
                elements.put(fieldName, createCheckComboBox("Choose " + labelName, "cmb" + labelName));
            } else if (field.getType().toString().contains("float") || field.getType().toString().contains("int")) {
                elements.put(fieldName, createIntegerField("", "int" + labelName));
            } else if ((field.getType().toString().contains("String"))) {
                elements.put(fieldName, createTextField("", "txt" + labelName));
            } else if (field.getType().toString().contains("AbilityScore")) {
                elements.put(fieldName, createIntegerField("", "int" + labelName));
            } else {
                elements.put(fieldName, createComboBox("", "cmb" + labelName));
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

            if ((field.getType().toString().contains("List"))) {
                String type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0].getTypeName();
                List<Object> cmbBox = ((CheckComboBox<Object>) items.get(field.getName())).getCheckModel().getCheckedItems();
                Class<?> classType = Class.forName(type);
                field.set(object, cmbBox.stream().map(classType::cast).collect(Collectors.toList()));
            } else if (field.getType().toString().contains("float") || field.getType().toString().contains("int")) {
                int value = ((IntegerField) items.get(field.getName())).getValue();
                field.set(object, value);
            } else if ((field.getType().toString().contains("String"))) {
                String value = ((TextField) items.get(field.getName())).getText();
                field.set(object, value);
            } else if (field.getType().toString().contains("AbilityScore") || field.getType().toString().contains("PriceUnit")) {
                int value = ((IntegerField) items.get(field.getName())).getValue();
                field.set(object, new AbilityScore(value));
            } else {
                Class<?> objectClass = field.getType();
                Object value = ((ComboBox<Object>) items.get(field.getName())).getValue();
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
