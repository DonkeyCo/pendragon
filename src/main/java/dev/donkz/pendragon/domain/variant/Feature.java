package dev.donkz.pendragon.domain.variant;

import dev.donkz.pendragon.domain.NameDescriptionObject;
import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode(callSuper = true)
@Value
public class Feature extends NameDescriptionObject {
    Kind kind;
    int level;
}
