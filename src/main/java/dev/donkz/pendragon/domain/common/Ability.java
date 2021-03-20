package dev.donkz.pendragon.domain.common;

public enum Ability {
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

    public String getLongName() {
        return longName;
    }
}
