package dev.donkz.pendragon.domain.character;

import dev.donkz.pendragon.domain.common.Ability;
import lombok.Data;

import java.util.List;

@Data
public class Pc {
    private String id;

    // General Information
    private CharacterInformation characterInformation;
    private List<Ability> savingThrows;
    private int level;
    private int exp;
    private int maxHp;
    private int curHp;
    private int ac;
    private int initiative;
    private int speed;
    private int deathSaveSuccesses;
    private int deathSaveFails;
    private List<String> hitDice;

    // Character Abilities/Traits
    private List<String> actions;
    private List<String> proficiencies;
    private List<String> attacks;
    private List<String> features;
    private List<String> traits;
    private List<String> equipment;

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
}
