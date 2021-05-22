package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import lombok.Value;

@Value
public class Feature implements Printable {
    String name;
    String description;
    Kind kind;
    int level;

    public Feature() {
        this.name = "";
        this.description = "";
        this.kind = null;
        this.level = 0;
    }

    @Override
    public String shortString() {
        return name;
    }

    public String toString() {
        return name + ": " + description + " | "
                + "Class: " + kind.getName() + " | "
                + "Level: " + level;
    }
}
