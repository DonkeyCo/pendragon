package dev.donkz.pendragon.domain.character;

import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.variant.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class Npc implements Character, Printable {
    private String id;

    // General Information
    private String name;
    private String notes;
    private String alignment;
    private String background;
    private Kind kind;
    private Race race;
    private List<Ability> savingThrows;
    private int maxHp;
    private int curHp;
    private int ac;
    private int initiative;
    private int speed;
    private int deathSaveSuccesses;
    private int deathSaveFails;
    private List<String> hitDice;
    private List<String> languages;

    // Character Abilities/Traits
    private List<String> actions;
    private List<Proficiency> proficiencies;
    private List<String> attacks;
    private List<Feature> features;
    private List<Trait> traits;
    private List<Equipment> equipment;

    // Currency
    private int cp;
    private int sp;
    private int ep;
    private int pp;
    private int gp;

    // Spells
    private SpellInformation spellInformation;

    // Ability Scores
    private AbilityScore strength;
    private AbilityScore dexterity;
    private AbilityScore constitution;
    private AbilityScore intelligence;
    private AbilityScore wisdom;
    private AbilityScore charisma;

    public Npc() {
        this.id = UUID.randomUUID().toString();
        this.savingThrows = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.proficiencies = new ArrayList<>();
        this.attacks = new ArrayList<>();
        this.features = new ArrayList<>();
        this.traits = new ArrayList<>();
        this.equipment = new ArrayList<>();
    }

    @Override
    public String shortString() {
        return name;
    }

    @Override
    public String longString() {
        return name + " | Alignment: " + alignment + " | Background: " + background + " | "
                + "Kind: " + kind.getName() + " | "
                + "Race: " + race.getName() + " | "
                + "Saving Throws: " + savingThrows.stream().map(Enum::name).collect(Collectors.joining(",")) + " | "
                + "Max. HP: " + maxHp + " | "
                + "Armor Class: " + ac + " | "
                + "Initiative: " + initiative + " | "
                + "Speed: " + speed + " | "
                + "Hit Dice: " + String.join(",", hitDice) + " | "
                + "Languages: " + String.join(",", languages) + " | "
                + "Actions: " + String.join(",", actions) + " | "
                + "Proficiencies: " + proficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(",")) + " | "
                + "Attacks: " + String.join(",", attacks) + " | "
                + "Features: " + features.stream().map(Feature::getName).collect(Collectors.joining(",")) + " | "
                + "Traits: " + traits.stream().map(Trait::getName).collect(Collectors.joining(",")) + " | "
                + "Equipment: " + equipment.stream().map(Equipment::getName).collect(Collectors.joining(","));
    }
}
