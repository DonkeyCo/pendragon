package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.common.Ability;
import lombok.Value;

@Value
public class AbilityBonus {
    Ability type;
    int bonus;
}
