package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.NameDescriptionObject;
import dev.donkz.pendragon.domain.common.Ability;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Value
public class Kind extends NameDescriptionObject {
    String notes;
    String hitDie;
    List<Proficiency> proficiencies;
    List<Ability> savingThrows;
}
