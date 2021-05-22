package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.Printable;
import dev.donkz.pendragon.domain.common.Ability;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Value
public class Kind implements Printable {
    String name;
    String description;
    String notes;
    String hitDie;
    List<Proficiency> proficiencies;
    List<Ability> savingThrows;

    public Kind() {
        this.name = "";
        this.description = "";
        this.notes = "";
        this.hitDie = "";
        this.proficiencies = new ArrayList<>();
        this.savingThrows = new ArrayList<>();
    }

    @Override
    public String shortString() {
        return name;
    }

    public String toString() {
        return name + " | "
                + description + " | "
                + "Hit Die: " + hitDie + " | "
                + "Proficiencies: " + proficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(","))
                + "Saving Throws: " + savingThrows.stream().map(Enum::name).collect(Collectors.joining(","));
    }
}
