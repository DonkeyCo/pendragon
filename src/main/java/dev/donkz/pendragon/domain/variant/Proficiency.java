package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.common.Ability;
import lombok.Value;

@Value
public class Proficiency implements Printable {
    String name;
    Ability type;

    @Override
    public String shortString() {
        return name;
    }

    public String toString() {
        return name + ": " + type;
    }
}
