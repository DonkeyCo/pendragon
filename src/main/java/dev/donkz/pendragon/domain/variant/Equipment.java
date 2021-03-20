package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.common.PriceUnit;
import lombok.Value;

@Value
public class Equipment {
    String name;
    String description;
    float weight;
    int value;
    PriceUnit unit;
}
