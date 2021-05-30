package dev.donkz.pendragon.domain;

import dev.donkz.pendragon.domain.character.AbilityScore;
import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.exception.model.AbilityScoreBelowMinimumExcecption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UnitTestPc {

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
        pc.increaseAbility(10, Ability.CHA);

        assert pc.getCharisma().getScore() == 20;
        assert pc.getCharisma().getModifier() == 5;
    }

    @Test
    @DisplayName("Increase Constitution")
    public void testIncreaseCON() {
        pc.increaseAbility(10, Ability.CON);

        assert pc.getConstitution().getScore() == 20;
        assert pc.getConstitution().getModifier() == 5;
    }

    @Test
    @DisplayName("Increase Dexterity")
    public void testIncreaseDEX() {
        pc.increaseAbility(10, Ability.DEX);

        assert pc.getDexterity().getScore() == 20;
        assert pc.getDexterity().getModifier() == 5;
    }

    @Test
    @DisplayName("Increase Intelligence")
    public void testIncreaseINT() {
        pc.increaseAbility(10, Ability.INT);

        assert pc.getIntelligence().getScore() == 20;
        assert pc.getIntelligence().getModifier() == 5;
    }

    @Test
    @DisplayName("Increase Strength")
    public void testIncreaseSTR() {
        pc.increaseAbility(10, Ability.STR);

        assert pc.getStrength().getScore() == 20;
        assert pc.getStrength().getModifier() == 5;
    }

    @Test
    @DisplayName("Increase Wisdom")
    public void testIncreaseWIS() {
        pc.increaseAbility(10, Ability.WIS);

        assert pc.getWisdom().getScore() == 20;
        assert pc.getWisdom().getModifier() == 5;
    }

    @Test
    @DisplayName("Decrease Charisma")
    public void testDecreaseCHA() throws AbilityScoreBelowMinimumExcecption {
        pc.decreaseAbility(2, Ability.CHA);

        assert pc.getCharisma().getScore() == 8;
        assert pc.getCharisma().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Constitution")
    public void testDecreaseCON() throws AbilityScoreBelowMinimumExcecption {
        pc.decreaseAbility(2, Ability.CON);

        assert pc.getConstitution().getScore() == 8;
        assert pc.getConstitution().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Dexterity")
    public void testDecreaseDEX() throws AbilityScoreBelowMinimumExcecption {
        pc.decreaseAbility(2, Ability.DEX);

        assert pc.getDexterity().getScore() == 8;
        assert pc.getDexterity().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Intelligence")
    public void testDecreaseINT() throws AbilityScoreBelowMinimumExcecption {
        pc.decreaseAbility(2, Ability.INT);

        assert pc.getIntelligence().getScore() == 8;
        assert pc.getIntelligence().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Strength")
    public void testDecreaseSTR() throws AbilityScoreBelowMinimumExcecption {
        pc.decreaseAbility(2, Ability.STR);

        assert pc.getStrength().getScore() == 8;
        assert pc.getStrength().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Wisdom")
    public void testDecreaseWIS() throws AbilityScoreBelowMinimumExcecption {
        pc.decreaseAbility(2, Ability.WIS);

        assert pc.getWisdom().getScore() == 8;
        assert pc.getWisdom().getModifier() == -1;
    }

    @Test
    @DisplayName("Decrease Charisma over minimum")
    public void testDecreaseCHAOverMin() {
        try {
            pc.decreaseAbility(3, Ability.CHA);
        } catch (AbilityScoreBelowMinimumExcecption e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Decrease Constitution over minimum")
    public void testDecreaseCONOverMin() {
        try {
            pc.decreaseAbility(3, Ability.CON);
        } catch (AbilityScoreBelowMinimumExcecption e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Decrease Dexterity over minimum")
    public void testDecreaseDEXOverMin() {
        try {
            pc.decreaseAbility(3, Ability.DEX);
        } catch (AbilityScoreBelowMinimumExcecption e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Decrease Intelligence over minimum")
    public void testDecreaseINTOverMin() {
        try {
            pc.decreaseAbility(3, Ability.INT);
        } catch (AbilityScoreBelowMinimumExcecption e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Decrease Strength over minimum")
    public void testDecreaseSTROverMin() {
        try {
            pc.decreaseAbility(3, Ability.STR);
        } catch (AbilityScoreBelowMinimumExcecption e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Decrease Wisdom over minimum")
    public void testDecreaseWISOverMin() {
        try {
            pc.decreaseAbility(3, Ability.WIS);
        } catch (AbilityScoreBelowMinimumExcecption e) {
            assert true;
        }
    }
}
