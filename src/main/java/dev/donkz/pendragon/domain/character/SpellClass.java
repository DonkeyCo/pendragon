package dev.donkz.pendragon.domain.character;

import lombok.Value;

import java.util.List;

@Value
public class SpellClass {
    List<String> spells;
    int totalSlots;
    int usedSlots;
}
