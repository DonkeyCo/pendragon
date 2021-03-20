package dev.donkz.pendragon.domain.character;

import lombok.Value;

@Value
public class CharacterInformation {
    String name;
    String notes;
    String alignment;
    String background;
    String kind;
    String race;
}
