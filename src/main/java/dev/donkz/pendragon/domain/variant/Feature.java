package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import lombok.Value;

@Value
public class Feature implements Printable {
    String name;
    String description;
    Kind kind;
    int level;

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
