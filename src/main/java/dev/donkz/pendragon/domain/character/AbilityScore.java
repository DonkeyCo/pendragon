package dev.donkz.pendragon.domain.character;

import lombok.Value;

@Value
public class AbilityScore {
    int score;
    int modifier;

    public AbilityScore(int score) {
        this.score = score;
        this.modifier = Math.floorDiv(score - 10, 2);
    }
}
