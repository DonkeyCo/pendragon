package dev.donkz.pendragon.domain.character;

import dev.donkz.pendragon.domain.Entity;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.variant.*;
import dev.donkz.pendragon.exception.model.AbilityScoreNegativeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Pc implements Character, Entity {
    private String id;

    // General Information
    private String name;
    private String notes;
    private String alignment;
    private String background;
    private Kind kind;
    private Race race;
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

    public Pc() {
        this.id = UUID.randomUUID().toString();
    }

    public void increaseAbility(int increment, Ability type) {
        switch (type) {
            case CHA -> charisma = new AbilityScore(charisma.getScore() + increment);
            case CON -> constitution = new AbilityScore(constitution.getScore() + increment);
            case DEX -> dexterity = new AbilityScore(dexterity.getScore() + increment);
            case INT -> intelligence = new AbilityScore(intelligence.getScore() + increment);
            case STR -> strength = new AbilityScore(strength.getScore() + increment);
            case WIS -> wisdom = new AbilityScore(wisdom.getScore() + increment);
        }
    }

    public void decreaseAbility(int decrement, Ability type) throws AbilityScoreNegativeException {
        switch (type) {
            case CHA -> {
                if (charisma.getScore() - decrement >= 0) {
                    charisma = new AbilityScore(charisma.getScore() - decrement);
                } else {
                    throw new AbilityScoreNegativeException();
                }
            }
            case CON -> {
                if (constitution.getScore() - decrement >= 0) {
                    constitution = new AbilityScore(constitution.getScore() - decrement);
                } else {
                    throw new AbilityScoreNegativeException();
                }
            }
            case DEX -> {
                if (dexterity.getScore() - decrement >= 0) {
                    dexterity = new AbilityScore(dexterity.getScore() - decrement);
                } else {
                    throw new AbilityScoreNegativeException();
                }
            }
            case INT -> {
                if (intelligence.getScore() - decrement >= 0) {
                    intelligence = new AbilityScore(intelligence.getScore() - decrement);
                } else {
                    throw new AbilityScoreNegativeException();
                }
            }
            case STR -> {
                if (strength.getScore() - decrement >= 0) {
                    strength = new AbilityScore(strength.getScore() - decrement);
                } else {
                    throw new AbilityScoreNegativeException();
                }
            }
            case WIS -> {
                if (wisdom.getScore() - decrement >= 0) {
                    wisdom = new AbilityScore(wisdom.getScore() - decrement);
                } else {
                    throw new AbilityScoreNegativeException();
                }
            }
        }
    }
}
