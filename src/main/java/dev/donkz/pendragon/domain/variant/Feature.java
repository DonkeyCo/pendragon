package dev.donkz.pendragon.domain.variant;

import lombok.Value;

@Value
public class Feature {
    String name;
    String description;
    Kind kind;
    int level;
}
