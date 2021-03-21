package dev.donkz.pendragon.domain.character;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CharacterInformation {
    String name;
    String notes;
    String alignment;
    String background;
    String kind;
    String race;
}
