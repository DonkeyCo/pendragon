package dev.donkz.pendragon.service;

import java.util.concurrent.ThreadLocalRandom;

public class DiceService {
    public int rollDice(int amount) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, amount + 1);
        return randomNum;
    }
}
