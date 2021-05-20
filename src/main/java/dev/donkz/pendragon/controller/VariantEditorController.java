package dev.donkz.pendragon.controller;

import com.sun.javafx.scene.control.IntegerField;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.common.PriceUnit;
import dev.donkz.pendragon.domain.variant.*;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.ui.CreateDialog;
import dev.donkz.pendragon.util.ControlUtility;
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
    private Pane traitPane;
    @FXML
    private Pane kindPane;
    @FXML
    private Pane racePane;
    @FXML
    private Pane equipmentPane;
    @FXML
    private Pane spellPane;
    @FXML
    private Pane featurePane;
    @FXML
    private Pane skillPane;

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

        TextField txtName = ControlUtility.createTextField("Enter a proficiency name");
        ComboBox<Ability> cmbAbilities = ControlUtility.createComboBox("Choose an ability type", Arrays.asList(Ability.values()));

        items.put("Name", txtName);
        items.put("Ability", cmbAbilities);
        Dialog<String> dialog = createDialog("Create Proficiency", items);

        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                Ability ability = cmbAbilities.getValue();
                Proficiency proficiency = new Proficiency(name, ability);
                campaignVariant.addProficiency(proficiency);

                renderContent();
                return proficiency.toString();
            }
            return "None";
        });

        dialog.show();
    }

    public void onTraits() {
        Map<String, Control> items = new LinkedHashMap<>();

        TextField txtName = ControlUtility.createTextField("Enter a trait name");
        TextField txtDescription = ControlUtility.createTextField("Enter a trait description");
        CheckComboBox<Proficiency> cmbProficiency = ControlUtility.createCheckComboBox("Select proficiencies", campaignVariant.getProficiencies());

        items.put("Name", txtName);
        items.put("Description", txtDescription);
        items.put("Proficiencies", cmbProficiency);
        Dialog<String> dialog = createDialog("Create Trait", items);

        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                String description = txtDescription.getText();
                List<Proficiency> proficiencies = cmbProficiency.getCheckModel().getCheckedItems();

                Trait trait = new Trait(name, description, proficiencies);
                campaignVariant.addTrait(trait);

                renderContent();
                return trait.toString();
            }
            return "None";
        });

        dialog.show();
    }

    public void onKinds() {
        Map<String, Control> items = new LinkedHashMap<>();
        TextField txtName = ControlUtility.createTextField("Enter a class name");
        TextField txtDescription = ControlUtility.createTextField("Enter a class description");
        TextArea txtNotes = ControlUtility.createTextArea("Enter additional class notes");
        TextField txtDie = ControlUtility.createTextField("Enter the hit die for the class");
        CheckComboBox<Proficiency> cmbProficiencies = ControlUtility.createCheckComboBox("Class Proficiencies", campaignVariant.getProficiencies());
        CheckComboBox<Ability> cmbAbilities = ControlUtility.createCheckComboBox("Abilities", Arrays.asList(Ability.values()));

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
                List<Ability> savingThrows = new ArrayList<>(cmbAbilities.getCheckModel().getCheckedItems());

                Kind kind = new Kind(name, description, notes, hitDie, proficiencies, savingThrows);
                campaignVariant.addKind(kind);

                renderContent();
                return kind.toString();
            }
            return "None";
        });

        dialog.show();
    }

    public void onRaces() {
        Map<String, Control> items = new LinkedHashMap<>();
        TextField txtName = ControlUtility.createTextField("Enter a race name");
        TextField txtDescription = ControlUtility.createTextField("Enter a race description");
        TextArea txtArea = ControlUtility.createTextArea("Enter additional race notes");
        IntegerField intFSpeed = ControlUtility.createIntegerField("Enter a speed");
        CheckComboBox<Ability> cmbAbility = ControlUtility.createCheckComboBox("Ability", Arrays.asList(Ability.values()));
        TextField txtAlignment = ControlUtility.createTextField("Enter an alignment");
        TextField txtAge = ControlUtility.createTextField("Enter an age");
        TextField txtSize = ControlUtility.createTextField("Enter a size");
        TextField txtLanguages = ControlUtility.createTextField("Enter languages (separate with comma)");
        CheckComboBox<Proficiency> cmbProficiencies = ControlUtility.createCheckComboBox("Choose starting proficiencies", campaignVariant.getProficiencies());
        CheckComboBox<Trait> cmbTraits = ControlUtility.createCheckComboBox("Choose traits", campaignVariant.getTraits());

        items.put("Name", txtName);
        items.put("Description", txtDescription);
        items.put("Alignment", txtAlignment);
        items.put("Age", txtAge);
        items.put("Size", txtSize);
        items.put("Speed", intFSpeed);
        items.put("Languages", txtLanguages);
        items.put("Starting Proficiencies", cmbProficiencies);
        items.put("Traits", cmbTraits);
        items.put("Abilities", cmbAbility);
        items.put("Notes", txtArea);

        Dialog<String> dialog = createDialog("Create Race", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                String description = txtDescription.getText();
                String alignment = txtAlignment.getText();
                String age = txtAge.getText();
                String size = txtSize.getText();
                int speed = intFSpeed.getValue();
                List<String> languages = Arrays.asList(txtLanguages.getText().split(","));
                List<Proficiency> proficiencies = cmbProficiencies.getCheckModel().getCheckedItems();
                List<Trait> traits = cmbTraits.getCheckModel().getCheckedItems();
                List<Ability> abilities = cmbAbility.getCheckModel().getCheckedItems();
                List<AbilityBonus> abilityBonuses = abilities.stream().map(ability -> new AbilityBonus(ability, 0)).collect(Collectors.toList());
                String notes = txtArea.getText();

                Race race = new Race(name, description, notes, speed, abilityBonuses, alignment, age, size, languages, proficiencies, traits);
                campaignVariant.addRace(race);

                renderContent();
                return race.toString();
            }
            return "None";
        });

        dialog.show();
    }

    public void onSkills() {
        Map<String, Control> items = new LinkedHashMap<>();
        TextField txtName = ControlUtility.createTextField("Enter a feature name");
        TextField txtDescription = ControlUtility.createTextField("Enter a feature description");
        ComboBox<Ability> cmbAbilities = ControlUtility.createComboBox("Choose a class", Arrays.asList(Ability.values()));

        items.put("Name", txtName);
        items.put("Description", txtDescription);
        items.put("Ability Score", cmbAbilities);

        Dialog<String> dialog = createDialog("Create Skill", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                String description = txtDescription.getText();
                Ability ability = cmbAbilities.getValue();

                Skill skill = new Skill(name, description, ability);
                campaignVariant.addSkill(skill);

                renderContent();
                return skill.toString();
            }
            return "None";
        });

        dialog.show();
    }

    public void onFeature() {
        Map<String, Control> items = new LinkedHashMap<>();
        TextField txtName = ControlUtility.createTextField("Enter a feature name");
        TextField txtDescription = ControlUtility.createTextField("Enter a feature description");
        ComboBox<Kind> cmbKind = ControlUtility.createComboBox("Choose a class", campaignVariant.getKinds());
        IntegerField intFLevel = ControlUtility.createIntegerField("Enter the minimum required level");

        items.put("Name", txtName);
        items.put("Description", txtDescription);
        items.put("Kind", cmbKind);
        items.put("Level", intFLevel);

        Dialog<String> dialog = createDialog("Create Feature", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                String description = txtDescription.getText();
                Kind kind = cmbKind.getValue();
                int level = intFLevel.getValue();

                Feature feature = new Feature(name, description, kind, level);
                campaignVariant.addFeature(feature);

                renderContent();
                return feature.toString();
            }
            return "None";
        });

        dialog.show();
    }

    public void onSpell() {
        Map<String, Control> items = new LinkedHashMap<>();
        TextField txtName = ControlUtility.createTextField("Enter a feature name");
        TextField txtDescription = ControlUtility.createTextField("Enter a feature description");

        items.put("Name", txtName);
        items.put("Description", txtDescription);

        Dialog<String> dialog = createDialog("Create Spell", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                String description = txtDescription.getText();

                Spell spell = new Spell(name, description);
                campaignVariant.addSpell(spell);

                renderContent();
                return spell.toString();
            }
            return "None";
        });

        dialog.show();
    }

    public void onEquipment() {
        Map<String, Control> items = new LinkedHashMap<>();
        TextField txtName = ControlUtility.createTextField("Enter a equipment name");
        TextField txtDescription = ControlUtility.createTextField("Enter a equipment description");
        IntegerField intFWeight = ControlUtility.createIntegerField("Enter a equipment weight");
        IntegerField intFValue = ControlUtility.createIntegerField("Enter a price value");
        ComboBox<PriceUnit> cmbUnits = ControlUtility.createComboBox("Choose a unit", Arrays.asList(PriceUnit.values()));

        items.put("Name", txtName);
        items.put("Description", txtDescription);
        items.put("Weight", intFWeight);
        items.put("Price Value", intFValue);
        items.put("Price Unit", cmbUnits);

        Dialog<String> dialog = createDialog("Create Equipment", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                String name = txtName.getText();
                String description = txtDescription.getText();
                float weight = intFWeight.getValue();
                int value = intFValue.getValue();
                PriceUnit unit = cmbUnits.getValue();

                Equipment equipment = new Equipment(name, description, weight, value, unit);
                campaignVariant.addEquipment(equipment);

                renderContent();
                return equipment.toString();
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

    private void renderContent() {
        renderPane(proficiencyPane, campaignVariant.getProficiencies());
        renderPane(traitPane, campaignVariant.getTraits());
        renderPane(kindPane, campaignVariant.getKinds());
        renderPane(racePane, campaignVariant.getRaces());
        renderPane(featurePane, campaignVariant.getFeatures());
        renderPane(spellPane, campaignVariant.getSpells());
        renderPane(equipmentPane, campaignVariant.getEquipments());
        renderPane(skillPane, campaignVariant.getSkills());
    }

    private void renderPane(Pane pane, List<?> items) {
        pane.getChildren().clear();
        Label lblNoItems = new Label("No Items");
        pane.getChildren().add(lblNoItems);
        items.forEach(item -> {
            pane.getChildren().remove(lblNoItems);

            HBox row = new HBox();
            row.setSpacing(10);

            Label lblLabel = new Label(item.toString());
            lblLabel.setWrapText(true);
            lblLabel.setFont(new Font(15));

            row.getChildren().add(lblLabel);
            pane.getChildren().add(row);
        });
    }
}
