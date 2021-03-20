package dev.donkz.pendragon.domain.variant;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@Value
public class Trait {
    String name;
    String description;
    List<Proficiency> proficiencies;
}
