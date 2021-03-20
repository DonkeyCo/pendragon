package dev.donkz.pendragon.domain.character;

import dev.donkz.pendragon.domain.common.Ability;
import lombok.Data;

import java.util.List;

@Data
public class Monster implements Character {
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

}
