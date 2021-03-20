package dev.donkz.pendragon.domain.variant;

import lombok.Value;

import java.util.List;

@Value
public class Race {
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
}
