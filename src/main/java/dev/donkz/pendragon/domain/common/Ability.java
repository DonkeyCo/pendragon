package dev.donkz.pendragon.domain.common;

import dev.donkz.pendragon.domain.Printable;

public enum Ability implements Printable {
    STR("Strength"),
    DEX("Dexterity"),
    CON("Constitution"),
    INT("Intelligence"),
    WIS("Wisdom"),
    CHA("Charisma");

    private final String longName;
    Ability(String longName) {
        this.longName = longName;
    }

    public String longString() {
        return longName;
    }

    @Override
    public String shortString() {
        return name();
    }
}
