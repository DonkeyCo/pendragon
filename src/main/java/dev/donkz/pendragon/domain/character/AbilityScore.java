package dev.donkz.pendragon.domain.character;

import lombok.Value;

@Value
public class AbilityScore {
    int score;
    int modifier;

    public AbilityScore() {
        this.score = 0;
        this.modifier = 0;
    }

    public AbilityScore(int score) {
        this.score = score;
        this.modifier = Math.floorDiv(score - 10, 2);
    }

    public AbilityScore(int score, int modifier) {
        this.score = score;
        this.modifier = modifier;
    }
}
