package dev.donkz.pendragon.service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Service for dices
 */
public class DiceService {
    /**
     * Rolls a dice for max. given amount
     * @param amount
     * @return
     */
    public int rollDice(int amount) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, amount + 1);
        return randomNum;
    }
}
