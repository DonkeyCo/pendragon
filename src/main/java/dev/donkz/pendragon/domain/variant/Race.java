package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class Race implements Printable {
    String name;
    String description;
    String notes;
    int speed;
    List<AbilityBonus> abilityBonuses;
    String alignment;
    String age;
    String size;
    List<String> languages;
    List<Proficiency> startingProficiencies;
    List<Trait> traits;

    public Race() {
        this.name = "";
        this.description = "";
        this.notes = "";
        this.speed = 0;
        this.abilityBonuses = new ArrayList<>();
        this.alignment = "";
        this.age = "";
        this.size = "";
        this.languages = new ArrayList<>();
        this.startingProficiencies = new ArrayList<>();
        this.traits = new ArrayList<>();
    }

    @Override
    public String shortString() {
        return null;
    }

    public String toString() {
        return name + ": " + description + " | "
                + "Speed: " + speed + " | "
                + "Ability Bonuses :" + abilityBonuses.stream().map(AbilityBonus::shortString).collect(Collectors.joining(",")) + " | "
                + "Alignment: " + alignment + " | "
                + "Age: " + age + " | "
                + "Size: " + size + " | "
                + "Languages: " + String.join(",", languages) + " | "
                + "Starting Proficiencies: " + startingProficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(",")) + " | "
                + "Traits: " + traits.stream().map(Trait::getName).collect(Collectors.joining(","));
    }
}
