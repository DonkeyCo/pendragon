package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.common.Ability;
import lombok.Value;

@Value
public class Skill implements Printable {
    String name;
    String description;
    Ability abilityScore;


    @Override
    public String shortString() {
        return name + " (" + abilityScore + ")";
    }

    public String toString() {
        return name + " (" + abilityScore + ")" + ": " + description;
    }
}
