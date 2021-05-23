package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.common.Ability;
import lombok.Value;

@Value
public class AbilityBonus implements Printable {
    Ability type;
    int bonus;

    public AbilityBonus() {
        this.type = null;
        this.bonus = 0;
    }

    @Override
    public String shortString() {
        return type + ": " + bonus;
    }

    public String toString() {
        return type + ": " + bonus;
    }

    public String longString() {
        return type + ": " + bonus;
    }
}
