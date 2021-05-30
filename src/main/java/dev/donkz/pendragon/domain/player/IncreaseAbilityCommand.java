package dev.donkz.pendragon.domain.player;

import dev.donkz.pendragon.domain.character.Pc;
import dev.donkz.pendragon.domain.common.Ability;

/**
 * Increase the ability score for a given PC
 */
public class IncreaseAbilityCommand implements PlayerCommand {
    private final Pc pc;
    private final Ability ability;
    private final int amount;

    public IncreaseAbilityCommand(Pc pc, Ability ability, int amount) {
        this.pc = pc;
        this.ability = ability;
        this.amount = amount;
    }

    @Override
    public void execute() {
        pc.increaseAbility(amount, ability);
    }
}
