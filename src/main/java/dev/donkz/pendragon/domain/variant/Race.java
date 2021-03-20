package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.NameDescriptionObject;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Value
public class Race extends NameDescriptionObject {
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
