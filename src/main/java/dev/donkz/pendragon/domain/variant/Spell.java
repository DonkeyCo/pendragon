package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import lombok.Value;

@Value
public class Spell implements Printable {
    String name;
    String description;

    public Spell() {
        this.name = "";
        this.description = "";
    }

    @Override
    public String shortString() {
        return name;
    }

    public String toString() {
        return name + ": " + description;
    }
}
