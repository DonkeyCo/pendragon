package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.common.Ability;
import lombok.Value;

@Value
public class Skill implements Printable {
    String name;
    String description;
    Ability abilityScore;

    public Skill() {
        this.name = "";
        this.description = "";
        this.abilityScore = null;
    }

    @Override
    public String shortString() {
        return name + " (" + abilityScore + ")";
    }

    public String toString() {
        return name;
    }

    public String longString() {
        return name + " (" + abilityScore + ")" + ": " + description;
    }
}
