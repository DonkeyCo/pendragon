package dev.donkz.pendragon.controller;

import com.sun.javafx.scene.control.IntegerField;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.variant.*;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.ui.CreateDialog;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

public class VariantEditorController {
    @FXML
    private Pane proficiencyPane;
    @FXML
    private Label lblNoProficiency;
    @FXML
    private Label lblNoTraits;
    @FXML
    private Pane traitPane;
    @FXML
    private Pane kindPane;
    @FXML
    private Label lblNoKinds;
    @FXML
    private Pane featurePane;
    @FXML
    private Label lblNoFeatures;

    private CampaignVariant campaignVariant;
    private VariantListController variantListController;

    private final PlayerManagementService playerManagementService;

    @Inject
    public VariantEditorController(PlayerManagementService playerManagementService) {
        this.playerManagementService = playerManagementService;
    }

    @FXML
    public void initialize() {
        campaignVariant = new CampaignVariant("", "", false, playerManagementService.getRegisteredPlayer());
    }

    public void setParentController(VariantListController variantListController) {
        this.variantListController = variantListController;
    }

    @FXML
    public void onCancel() {
        this.variantListController.switchMode();
    }

    public void onProficiencies() {
        SortedMap<String, Control> items = new TreeMap<>();
        TextField txtName = new TextField();
        txtName.setPromptText("Enter a proficiency name");
        ComboBox<String> cmbAbilities = getAbilityBox();

        items.put("Name", txtName);
        items.put("Ability", cmbAbilities);
        Dialog<String> dialog = createDialog("Create Proficiency", items);

        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                Ability ability = Ability.valueOf(cmbAbilities.getValue());
                campaignVariant.addProficiency(new Proficiency(name, ability));

                renderContent();
                return String.join(";", name, ability.getLongName());
            }
            return "None";
        });

        dialog.show();
    }

    public void onTraits() {
        Map<String, Control> items = new LinkedHashMap<>();
        TextField txtName = new TextField();
        txtName.setPromptText("Enter a trait name");
        TextField txtDescription = new TextField();
        txtDescription.setPromptText("Enter a trait description");
        CheckComboBox<Proficiency> cmbProficiency = new CheckComboBox<>();
        cmbProficiency.getItems().addAll(campaignVariant.getProficiencies());
        cmbProficiency.setConverter(new StringConverter<>() {
            @Override
            public String toString(Proficiency proficiency) {
                return proficiency.getName();
            }

            @Override
            public Proficiency fromString(String s) {
                return campaignVariant.getProficiencies().stream().filter(proficiency -> proficiency.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
            }
        });

        items.put("Name", txtName);
        items.put("Description", txtDescription);
        items.put("Proficiencies", cmbProficiency);
        Dialog<String> dialog = createDialog("Create Trait", items);

        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                String description = txtDescription.getText();
                List<Proficiency> proficiencies = cmbProficiency.getCheckModel().getCheckedItems();

                campaignVariant.addTrait(new Trait(name, description, proficiencies));

                renderContent();
                return String.join(";", name);
            }
            return "None";
        });

        dialog.show();
    }

    public void onKinds() {
        Map<String, Control> items = new LinkedHashMap<>();
        TextField txtName = new TextField();
        TextField txtDescription = new TextField();
        TextArea txtNotes = new TextArea();
        TextField txtDie = new TextField();
        CheckComboBox<Proficiency> cmbProficiencies = new CheckComboBox<>();
        cmbProficiencies.getItems().addAll(campaignVariant.getProficiencies());
        CheckComboBox<String> cmbAbilities = getMultiAbilityBox();

        items.put("Name", txtName);
        items.put("Description", txtDescription);
        items.put("Hit Die", txtDie);
        items.put("Proficiencies", cmbProficiencies);
        items.put("Saving Throws", cmbAbilities);
        items.put("Notes", txtNotes);

        Dialog<String> dialog = createDialog("Create Class", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                String description = txtDescription.getText();
                String notes = txtNotes.getText();
                String hitDie = txtDie.getText();
                List<Proficiency> proficiencies = cmbProficiencies.getCheckModel().getCheckedItems();
                List<Ability> savingThrows = cmbAbilities.getCheckModel().getCheckedItems().stream().map(Ability::valueOf).collect(Collectors.toList());

                campaignVariant.addKind(new Kind(name, description, notes, hitDie, proficiencies, savingThrows));

                renderContent();
                return String.join(";", name);
            }
            return "None";
        });

        dialog.show();
    }

    public void onFeature() {
        Map<String, Control> items = new LinkedHashMap<>();
        TextField txtName = new TextField();
        TextField txtDescription = new TextField();
        ComboBox<Kind> cmbKind = new ComboBox<>();
        cmbKind.setItems(FXCollections.observableList(campaignVariant.getKinds()));
        cmbKind.setConverter(new StringConverter<>() {
            @Override
            public String toString(Kind kind) {
                if (kind != null) {
                    return kind.getName();
                }
                return "";
            }
            @Override
            public Kind fromString(String s) {
                return campaignVariant.getKinds().stream().filter(kind -> kind.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
            }
        });
        IntegerField intfLevel = new IntegerField();

        items.put("Name", txtName);
        items.put("Description", txtDescription);
        items.put("Kind", cmbKind);
        items.put("Level", intfLevel);

        Dialog<String> dialog = createDialog("Create Feature", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                String description = txtDescription.getText();
                Kind kind = cmbKind.getValue();
                int level = intfLevel.getValue();

                campaignVariant.addFeature(new Feature(name, description, kind, level));

                renderContent();
                return String.join(";", name);
            }
            return "None";
        });

        dialog.show();
    }

    private Dialog<String> createDialog(String title, Map<String, Control> items) {
        CreateDialog dialogPane = new CreateDialog(title, items);

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setDialogPane(dialogPane);

        return dialog;
    }

    private ComboBox<String> getAbilityBox() {
        ComboBox<String> cmbAbility = new ComboBox<>();
        cmbAbility.setItems(FXCollections.observableList(Arrays.stream(Ability.values()).map(Enum::name).collect(Collectors.toList())));
        return cmbAbility;
    }

    private CheckComboBox<String> getMultiAbilityBox() {
        CheckComboBox<String> cmbAbility = new CheckComboBox<>();
        cmbAbility.getItems().addAll(FXCollections.observableList(Arrays.stream(Ability.values()).map(Enum::name).collect(Collectors.toList())));
        return cmbAbility;
    }

    private void renderContent() {
        renderProficiency();
        renderTrait();
        renderKind();
    }

    private void renderProficiency() {
        proficiencyPane.getChildren().clear();
        proficiencyPane.getChildren().add(lblNoProficiency);
        campaignVariant.getProficiencies().forEach(proficiency -> {
            proficiencyPane.getChildren().remove(lblNoProficiency);

            HBox row = new HBox();
            row.setSpacing(10);
            Label lblProficiency = new Label();
            lblProficiency.setFont(new Font(15));
            lblProficiency.setText(proficiency.getName() + " (" + proficiency.getType() + ")");
            row.getChildren().add(lblProficiency);
            proficiencyPane.getChildren().add(row);
        });
    }

    private void renderTrait() {
        traitPane.getChildren().clear();
        traitPane.getChildren().add(lblNoTraits);
        campaignVariant.getTraits().forEach(trait -> {
            traitPane.getChildren().remove(lblNoTraits);

            HBox row = new HBox();
            row.setSpacing(10);

            Label lblTrait = new Label();
            lblTrait.setWrapText(true);
            lblTrait.setText(trait.getName()
                    + " | "
                    + trait.getDescription()
                    + " | Proficiency: "
                    + trait.getProficiencies().stream().map(Proficiency::getName).collect(Collectors.toList()));
            lblTrait.setFont(new Font(15));

            row.getChildren().add(lblTrait);
            traitPane.getChildren().add(row);
        });
    }

    private void renderKind() {
        kindPane.getChildren().clear();
        kindPane.getChildren().add(lblNoKinds);
        campaignVariant.getKinds().forEach(kind -> {
            kindPane.getChildren().remove(lblNoKinds);

            HBox row = new HBox();
            row.setSpacing(10);

            Label lblKind = new Label();
            lblKind.setWrapText(true);
            lblKind.setText(kind.getName() + " | " + kind.getDescription());
            lblKind.setFont(new Font(15));

            row.getChildren().add(lblKind);
            kindPane.getChildren().add(row);
        });
    }

    private void renderFeature() {
        featurePane.getChildren().clear();
        featurePane.getChildren().add(lblNoFeatures);
        campaignVariant.getFeatures().forEach(feature -> {
            featurePane.getChildren().remove(lblNoFeatures);

            HBox row = new HBox();
            row.setSpacing(10);

            Label lblFeature = new Label();
            lblFeature.setWrapText(true);
            lblFeature.setText(feature.getName() + " | " + feature.getDescription() + "(" + feature.getKind().getName() + ", Lvl." + feature.getLevel()+ ")");
            lblFeature.setFont(new Font(15));

            row.getChildren().add(lblFeature);
            featurePane.getChildren().add(row);
        });
    }
}
