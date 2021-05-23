package dev.donkz.pendragon.controller;

import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.character.Monster;
import dev.donkz.pendragon.domain.character.Npc;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.common.PriceUnit;
import dev.donkz.pendragon.domain.variant.*;
import dev.donkz.pendragon.exception.infrastructure.EntityNotFoundException;
import dev.donkz.pendragon.exception.infrastructure.IndexAlreadyExistsException;
import dev.donkz.pendragon.exception.infrastructure.MultiplePlayersException;
import dev.donkz.pendragon.service.PlayerManagementService;
import dev.donkz.pendragon.service.VariantMutationService;
import dev.donkz.pendragon.ui.CreateDialog;
import dev.donkz.pendragon.util.ControlUtility;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import org.controlsfx.control.CheckComboBox;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    @FXML
    private Pane monsterPane;
    @FXML
    private Pane npcPane;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtDescription;
    @FXML
    private CheckBox cbPublic;

    private CampaignVariant campaignVariant;
    private VariantListController variantListController;

    private final PlayerManagementService playerManagementService;
    private final VariantMutationService variantMutationService;

    @Inject
    public VariantEditorController(PlayerManagementService playerManagementService, VariantMutationService variantMutationService) {
        this.playerManagementService = playerManagementService;
        this.variantMutationService = variantMutationService;
    }

    @FXML
    public void initialize() {
        resetVariant();
    }

    private void resetVariant() {
        campaignVariant = new CampaignVariant("", "", false, playerManagementService.getRegisteredPlayer());
    }

    public void setParentController(VariantListController variantListController) {
        this.variantListController = variantListController;
    }

    @FXML
    public void onCancel() {
        resetVariant();
        this.variantListController.switchMode();
    }

    public void onSubmit() {
        try {
            this.variantMutationService.mutateVariant(campaignVariant, txtName.getText(), txtDescription.getText(), cbPublic.isSelected());
        } catch (MultiplePlayersException | EntityNotFoundException | IndexAlreadyExistsException e) {
            e.printStackTrace();
        }

        resetVariant();
        this.variantListController.createTiles();
        this.variantListController.switchMode();
    }

    public void onProficiencies() {
        Map<String, Region> items = ControlUtility.createForm(Proficiency.class);

        ControlUtility.fillComboBox((ComboBox<Ability>) items.get("Type"), Arrays.asList(Ability.values()));

        Dialog<String> dialog = createDialog("Create Proficiency", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Proficiency proficiency = null;
                try {
                    proficiency = ControlUtility.controlsToValues(Proficiency.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (proficiency != null) {
                    campaignVariant.addProficiency(proficiency);

                    renderContent();
                    return proficiency.toString();
                }
            }
            return "None";
        });

        dialog.show();
    }

    public void onTraits() {
        Map<String, Region> items = ControlUtility.createForm(Trait.class);

        ControlUtility.fillCheckComboBox((CheckComboBox<Proficiency>) items.get("Proficiencies"), campaignVariant.getProficiencies());

        Dialog<String> dialog = createDialog("Create Trait", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Trait trait = null;
                try {
                    trait = ControlUtility.controlsToValues(Trait.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (trait != null) {
                    campaignVariant.addTrait(trait);

                    renderContent();
                    return trait.toString();
                }
            }
            return "None";
        });

        dialog.show();
    }

    public void onKinds() {
        Map<String, Region> items = ControlUtility.createForm(Kind.class);

        ControlUtility.fillCheckComboBox((CheckComboBox<Proficiency>) items.get("Proficiencies"), campaignVariant.getProficiencies());
        ControlUtility.fillCheckComboBox((CheckComboBox<Ability>) items.get("SavingThrows"), Arrays.asList(Ability.values()));

        Dialog<String> dialog = createDialog("Create Class", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Kind kind = null;
                try {
                    kind = ControlUtility.controlsToValues(Kind.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (kind != null) {
                    campaignVariant.addKind(kind);

                    renderContent();
                    return kind.toString();
                }
            }
            return "None";
        });

        dialog.show();
    }

    public void onRaces() {
        Map<String, Region> items = ControlUtility.createForm(Race.class);

        ControlUtility.fillCheckComboBox((CheckComboBox<Proficiency>) items.get("StartingProficiencies"), campaignVariant.getProficiencies());
        ControlUtility.fillCheckComboBox((CheckComboBox<Trait>) items.get("Traits"), campaignVariant.getTraits());

        Dialog<String> dialog = createDialog("Create Race", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Race race = null;
                try {
                    race = ControlUtility.controlsToValues(Race.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (race != null) {
                    campaignVariant.addRace(race);

                    renderContent();
                    return race.toString();
                }
            }
            return "None";
        });

        dialog.show();
    }

    public void onSkills() {
        Map<String, Region> items = ControlUtility.createForm(Skill.class);

        ControlUtility.fillComboBox((ComboBox<Ability>) items.get("AbilityScore"), Arrays.asList(Ability.values()));

        Dialog<String> dialog = createDialog("Create Skill", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Skill skill = null;
                try {
                    skill = ControlUtility.controlsToValues(Skill.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (skill != null) {
                    campaignVariant.addSkill(skill);

                    renderContent();
                    return skill.toString();
                }
            }
            return "None";
        });

        dialog.show();
    }

    public void onFeature() {
        Map<String, Region> items = ControlUtility.createForm(Feature.class);

        ControlUtility.fillComboBox((ComboBox<Kind>) items.get("Kind"), campaignVariant.getKinds());

        Dialog<String> dialog = createDialog("Create Feature", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Feature feature = null;
                try {
                    feature = ControlUtility.controlsToValues(Feature.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (feature != null) {
                    campaignVariant.addFeature(feature);

                    renderContent();
                    return feature.toString();
                }
            }
            return "None";
        });

        dialog.show();
    }

    public void onSpell() {
        Map<String, Region> items = ControlUtility.createForm(Spell.class);

        Dialog<String> dialog = createDialog("Create Spell", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Spell spell = null;
                try {
                    spell = ControlUtility.controlsToValues(Spell.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (spell != null) {
                    campaignVariant.addSpell(spell);

                    renderContent();
                    return spell.toString();
                }
            }
            return "None";
        });

        dialog.show();
    }

    public void onEquipment() {
        Map<String, Region> items = ControlUtility.createForm(Equipment.class);

        ControlUtility.fillComboBox((ComboBox<PriceUnit>) items.get("Unit"), Arrays.asList(PriceUnit.values()));

        Dialog<String> dialog = createDialog("Create Equipment", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Equipment equipment = null;
                try {
                    equipment = ControlUtility.controlsToValues(Equipment.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (equipment != null) {
                    campaignVariant.addEquipment(equipment);

                    renderContent();
                    return equipment.toString();
                }
            }
            return "None";
        });

        dialog.show();
    }

    public void onNpc() {
        Map<String, Region> items = ControlUtility.createForm(Npc.class);

        ControlUtility.fillComboBox((ComboBox<Kind>) items.get("Kind"), campaignVariant.getKinds());
        ControlUtility.fillComboBox((ComboBox<Race>) items.get("Race"), campaignVariant.getRaces());
        ControlUtility.fillCheckComboBox((CheckComboBox<Ability>) items.get("SavingThrows"), Arrays.asList(Ability.values()));
        ControlUtility.fillCheckComboBox((CheckComboBox<Proficiency>) items.get("Proficiencies"), campaignVariant.getProficiencies());
        ControlUtility.fillCheckComboBox((CheckComboBox<Feature>) items.get("Features"), campaignVariant.getFeatures());
        ControlUtility.fillCheckComboBox((CheckComboBox<Trait>) items.get("Traits"), campaignVariant.getTraits());
        ControlUtility.fillCheckComboBox((CheckComboBox<Equipment>) items.get("Equipment"), campaignVariant.getEquipments());

        Dialog<String> dialog = createDialog("Create NPC", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Npc npc = null;
                try {
                    npc = ControlUtility.controlsToValues(Npc.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (npc != null) {
                    campaignVariant.addNpc(npc);

                    renderContent();
                    return npc.toString();
                }
            }
            return "None";
        });

        dialog.show();
    }

    public void onMonster() {
        Map<String, Region> items = ControlUtility.createForm(Monster.class);

        ControlUtility.fillCheckComboBox((CheckComboBox<Ability>) items.get("SavingThrows"), Arrays.asList(Ability.values()));

        Dialog<String> dialog = createDialog("Create Monster", items);
        dialog.setResultConverter(buttonType -> {
            if (buttonType != null) {
                Monster monster = null;
                try {
                    monster = ControlUtility.controlsToValues(Monster.class, items);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (monster != null) {
                    campaignVariant.addMonsters(monster);

                    renderContent();
                    return monster.toString();
                }
            }
            return "None";
        });

        dialog.show();
    }

    private Dialog<String> createDialog(String title, Map<String, Region> items) {
        CreateDialog dialogPane = new CreateDialog(title, items);
        dialogPane.setPrefWidth(Screen.getPrimary().getBounds().getWidth() / 5);
        dialogPane.setMaxWidth(Screen.getPrimary().getBounds().getWidth() / 5);
        dialogPane.setPrefHeight(Screen.getPrimary().getBounds().getHeight() / 1.5);
        dialogPane.setMaxHeight(Screen.getPrimary().getBounds().getHeight() / 1.5);

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setDialogPane(dialogPane);

        return dialog;
    }

    public void renderContent() {
        renderPane(proficiencyPane, campaignVariant.getProficiencies());
        renderPane(traitPane, campaignVariant.getTraits());
        renderPane(kindPane, campaignVariant.getKinds());
        renderPane(racePane, campaignVariant.getRaces());
        renderPane(featurePane, campaignVariant.getFeatures());
        renderPane(spellPane, campaignVariant.getSpells());
        renderPane(equipmentPane, campaignVariant.getEquipments());
        renderPane(skillPane, campaignVariant.getSkills());
        renderPane(monsterPane, campaignVariant.getMonsters());
        renderPane(npcPane, campaignVariant.getNpcs());
    }

    private void renderPane(Pane pane, List<?> items) {
        pane.getChildren().clear();

        Label lblNoItems = new Label("No Items");
        pane.getChildren().add(lblNoItems);
        if (items != null) {
            items.forEach(item -> {
                pane.getChildren().remove(lblNoItems);

                HBox row = new HBox();
                row.setSpacing(10);

                Label lblLabel = new Label(((Printable) item).longString());
                lblLabel.setWrapText(true);
                lblLabel.setFont(new Font(15));

                row.getChildren().add(lblLabel);
                pane.getChildren().add(row);
            });
        }
    }

    public void setCampaignVariant(CampaignVariant campaignVariant) {
        this.campaignVariant = campaignVariant;
    }
}
