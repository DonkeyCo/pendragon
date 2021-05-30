package dev.donkz.pendragon.domain;

import dev.donkz.pendragon.domain.character.AbilityScore;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.domain.player.DecreaseAbilityCommand;
import dev.donkz.pendragon.domain.player.PlayerCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UnitTestDecreaseAbilityCommand {
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
    @DisplayName("Decrease Charisma over min")
    public void testDecreaseCHAOverMin() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.CHA, 10);
        command.execute();

        assert pc.getCharisma().getScore() == 10;
        assert pc.getCharisma().getModifier() == 0;
    }

    @Test
    @DisplayName("Decrease Constitution over min")
    public void testDecreaseCONOverMin() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.CON, 10);
        command.execute();

        assert pc.getConstitution().getScore() == 10;
        assert pc.getConstitution().getModifier() == 0;
    }

    @Test
    @DisplayName("Decrease Dexterity over min")
    public void testDecreaseDEXOverMin() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.DEX, 10);
        command.execute();

        assert pc.getDexterity().getScore() == 10;
        assert pc.getDexterity().getModifier() == 0;
    }

    @Test
    @DisplayName("Decrease Intelligence over min")
    public void testDecreaseINTOverMin() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.INT, 10);
        command.execute();

        assert pc.getIntelligence().getScore() == 10;
        assert pc.getIntelligence().getModifier() == 0;
    }

    @Test
    @DisplayName("Decrease Strength over min")
    public void testDecreaseSTROverMin() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.STR, 10);
        command.execute();

        assert pc.getStrength().getScore() == 10;
        assert pc.getStrength().getModifier() == 0;
    }

    @Test
    @DisplayName("Decrease Wisdom over min")
    public void testDecreaseWISOverMin() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.WIS, 10);
        command.execute();

        assert pc.getWisdom().getScore() == 10;
        assert pc.getWisdom().getModifier() == 0;
    }

    @Test
    @DisplayName("Decrease Charisma")
    public void testDecreaseCHA() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.CHA, 2);
        command.execute();

        assert pc.getCharisma().getScore() == 8;
        assert pc.getCharisma().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Constitution")
    public void testDecreaseCON() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.CON, 2);
        command.execute();

        assert pc.getConstitution().getScore() == 8;
        assert pc.getConstitution().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Dexterity")
    public void testDecreaseDEX() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.DEX, 2);
        command.execute();

        assert pc.getDexterity().getScore() == 8;
        assert pc.getDexterity().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Intelligence")
    public void testDecreaseINT() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.INT, 2);
        command.execute();

        assert pc.getIntelligence().getScore() == 8;
        assert pc.getIntelligence().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Strength")
    public void testDecreaseSTR() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.STR, 2);
        command.execute();

        assert pc.getStrength().getScore() == 8;
        assert pc.getStrength().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Wisdom")
    public void testDecreaseWIS() {
        PlayerCommand command = new DecreaseAbilityCommand(pc, Ability.WIS, 2);
        command.execute();

        assert pc.getWisdom().getScore() == 8;
        assert pc.getWisdom().getModifier() == -1;
    }
}
