package dev.donkz.pendragon.domain;

import dev.donkz.pendragon.domain.character.AbilityScore;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.player.IncreaseAbilityCommand;
import dev.donkz.pendragon.domain.player.PlayerCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UnitTestIncreaseAbilityCommand {
    private Pc pc;

    @BeforeEach
    public void setUp() {
        pc = new Pc();
        pc.setName("TEST");
        pc.setCharisma(new AbilityScore(10));
        pc.setConstitution(new AbilityScore(10));
        pc.setDexterity(new AbilityScore(10));
        pc.setIntelligence(new AbilityScore(10));
        pc.setStrength(new AbilityScore(10));
        pc.setWisdom(new AbilityScore(10));
    }

    @Test
    @DisplayName("Increase Charisma")
    public void testIncreaseCHA() {
        PlayerCommand command = new IncreaseAbilityCommand(pc, Ability.CHA, 2);
        command.execute();

        assert pc.getCharisma().getScore() == 12;
        assert pc.getCharisma().getModifier() == 1;
    }

    @Test
    @DisplayName("Increase Constitution")
    public void testIncreaseCON() {
        PlayerCommand command = new IncreaseAbilityCommand(pc, Ability.CON, 2);
        command.execute();

        assert pc.getConstitution().getScore() == 12;
        assert pc.getConstitution().getModifier() == 1;
    }

    @Test
    @DisplayName("Increase Dexterity")
    public void testIncreaseDEX() {
        PlayerCommand command = new IncreaseAbilityCommand(pc, Ability.DEX, 2);
        command.execute();

        assert pc.getDexterity().getScore() == 12;
        assert pc.getDexterity().getModifier() == 1;
    }

    @Test
    @DisplayName("Increase Intelligence")
    public void testIncreaseINT() {
        PlayerCommand command = new IncreaseAbilityCommand(pc, Ability.INT, 2);
        command.execute();

        assert pc.getIntelligence().getScore() == 12;
        assert pc.getIntelligence().getModifier() == 1;
    }

    @Test
    @DisplayName("Increase Strength")
    public void testIncreaseSTR() {
        PlayerCommand command = new IncreaseAbilityCommand(pc, Ability.STR, 2);
        command.execute();

        assert pc.getStrength().getScore() == 12;
        assert pc.getStrength().getModifier() == 1;
    }

    @Test
    @DisplayName("Increase Wisdom")
    public void testIncreaseWIS() {
        PlayerCommand command = new IncreaseAbilityCommand(pc, Ability.WIS, 2);
        command.execute();

        assert pc.getWisdom().getScore() == 12;
        assert pc.getWisdom().getModifier() == 1;
    }
}
