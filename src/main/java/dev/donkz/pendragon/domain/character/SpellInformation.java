package dev.donkz.pendragon.domain.character;

import lombok.Data;

import java.util.List;

@Data
public class SpellInformation {
    private String spellCastingAbility;
    private String spellAbility;
    private String spellSave;
    private String spellAttackBonus;

    // Spell Sheet
    private SpellClass cantrips;
    private SpellClass level1;
    private SpellClass level2;
    private SpellClass level3;
    private SpellClass level4;
    private SpellClass level5;
    private SpellClass level6;
    private SpellClass level7;
    private SpellClass level8;
    private SpellClass level9;
}
