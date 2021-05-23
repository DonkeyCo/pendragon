package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.common.Ability;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
public class Proficiency implements Printable {
    String name;
    Ability type;

    public Proficiency() {
        this.name = "";
        this.type = null;
    }

    @Override
    public String shortString() {
        return name;
    }

    public String toString() {
        return name + "(" + type + ")";
    }

    public String longString() {
        return name + ": " + type;
    }
}
