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

    public Feature(String name, String description, Kind kind, int level) {
        this.name = name;
        this.description = description;
        this.kind = kind;
        this.level = Math.max(level, 0);
    }

    @Override
    public String shortString() {
        return name;
    }

    public String toString() {
        return name;
    }

    public String longString() {
        return name + ": " + description + " | "
                + "Class: " + kind.getName() + " | "
                + "Level: " + level;
    }
}
