package dev.donkz.pendragon.domain.character;

import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.common.Ability;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class Monster implements Character, Printable {
    private String id;

    // General Information
    private String name;
    private String description;
    private String notes;
    private String size;
    private int maxHp;
    private int curHp;
    private int speed;
    private List<Ability> savingThrows;
    private int challengeRating;
    private List<String> hitDice;

    // Ability Scores
    private AbilityScore strength;
    private AbilityScore dexterity;
    private AbilityScore constitution;
    private AbilityScore intelligence;
    private AbilityScore wisdom;
    private AbilityScore charisma;

    // Skills, Abilities, etc.
    private List<String> skills;
    private List<String> senses;
    private List<String> languages;
    private List<String> traits;

    // Actions
    private List<String> actions;
    private List<String> legendaryActions;

    public Monster() {
        this.id = UUID.randomUUID().toString();
        this.savingThrows = new ArrayList<>();
        this.hitDice = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.senses = new ArrayList<>();
        this.languages = new ArrayList<>();
        this.traits = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.legendaryActions = new ArrayList<>();
    }


    @Override
    public String shortString() {
        return name;
    }

    public String toString() {
        return name + ": " + description + " | "
                + "Size: " + size + " | "
                + "Max. HP: " + maxHp + " | "
                + "Speed: " + speed + " | "
                + "Challenge Rating: " + challengeRating + " | "
                + "Saving Throws: " + savingThrows.stream().map(Ability::getLongName).collect(Collectors.joining(",")) + " | "
                + "Hit Dice: " + String.join(",", hitDice) + " | "
                + "Strength: " + strength.getScore() + "(" + strength.getModifier() + ")" + " | "
                + "Dexterity: " + dexterity.getScore() + "(" + dexterity.getModifier() + ")" + " | "
                + "Constitution: " + constitution.getScore() + "(" + constitution.getModifier() + ")" + " | "
                + "Intelligence: " + intelligence.getScore() + "(" + intelligence.getModifier() + ")" + " | "
                + "Wisdom: " + wisdom.getScore() + "(" + wisdom.getModifier() + ")" + " | "
                + "Charisma: " + charisma.getScore() + "(" + charisma.getModifier() + ")" + " | "
                + "Skills: " + String.join(",", skills) + " | "
                + "Senses: " + String.join(",", senses) + " | "
                + "Languages: " + String.join(",", languages) + " | "
                + "Traits: " + String.join(",", traits) + " | "
                + "Actions: " + String.join(",", actions) + " | "
                + "Legendary Actions: " + String.join(",", legendaryActions);
    }
}
