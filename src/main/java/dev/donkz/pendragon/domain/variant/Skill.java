package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.NameDescriptionObject;
import dev.donkz.pendragon.domain.common.Ability;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class Skill extends NameDescriptionObject {
    Ability abilityScore;
}
