package dev.donkz.pendragon.domain.player;

import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.common.Ability;
import dev.donkz.pendragon.exception.model.AbilityScoreBelowMinimumExcecption;

/**
 * Command to decrease a PC's ability score
 */
public class DecreaseAbilityCommand implements PlayerCommand {
    private final Pc pc;
    private final Ability type;
    private final int amount;

    public DecreaseAbilityCommand(Pc pc, Ability type, int amount) {
        this.pc = pc;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public void execute() {
        try {
            pc.decreaseAbility(amount, type);
        } catch (AbilityScoreBelowMinimumExcecption e) {
            e.printStackTrace();
        }
    }
}
