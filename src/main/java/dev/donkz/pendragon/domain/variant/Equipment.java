package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.NameDescriptionObject;
import dev.donkz.pendragon.domain.common.PriceUnit;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class Equipment extends NameDescriptionObject {
    float weight;
    int value;
    PriceUnit unit;
}
