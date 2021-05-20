package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class Trait implements Printable {
    String name;
    String description;
    List<Proficiency> proficiencies;

    @Override
    public String shortString() {
        return name;
    }

    public String toString() {
        return name + ": " + description + " | "
                + "Proficiencies: " + proficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(","));
    }
}
