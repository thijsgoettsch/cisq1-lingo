package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.TurnNumberOutOfRangeException;

public class Score {
    int score;
    int turns;

    public Score(Integer turns) {
        this.score = 0;
        this.turns = turns;
    }

    public int calculateScore() {
        if (this.turns > 5) {
            throw new TurnNumberOutOfRangeException();
        }
        this.score += 5 * (5 - this.turns) + 5;
        return this.score;
    }
}
