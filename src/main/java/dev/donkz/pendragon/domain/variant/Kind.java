package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.common.Ability;
import lombok.Value;

import java.util.List;

@Value
public class Kind {
    String name;
    String description;
    String notes;
    String hitDie;
    List<Proficiency> proficiencies;
    List<Ability> savingThrows;
}
