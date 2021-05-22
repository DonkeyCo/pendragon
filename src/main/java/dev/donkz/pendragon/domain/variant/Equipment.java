package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.common.PriceUnit;
import lombok.Value;

@Value
public class Equipment implements Printable {
    String name;
    String description;
    float weight;
    int value;
    PriceUnit unit;

    public Equipment() {
        this.name = "";
        this.description = "";
        this.weight = 0;
        this.value = 0;
        this.unit = null;
    }

    @Override
    public String shortString() {
        return name;
    }

    public String toString() {
        return name + ": " + description + " | "
                + "Weight: " + weight + " | "
                + "Price: " + value + unit.getName();
    }
}
