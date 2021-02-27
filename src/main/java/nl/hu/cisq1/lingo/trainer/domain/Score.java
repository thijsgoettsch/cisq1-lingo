package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.TurnNumberOutOfRangeException;

import java.util.Objects;

public class Score {
    int score;
    int turns;

    public Score(Integer turns) {
        this.score = 0;
        this.turns = turns;
        if (this.turns > 5) {
            throw new TurnNumberOutOfRangeException();
        }
    }

    public int calculateScore() {
        return this.score = 5 * (5 - (this.turns + 1)) + 5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score1 = (Score) o;
        return score == score1.score &&
                turns == score1.turns;
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, turns);
    }

    @Override
    public String toString() {
        return "Score{" +
                "score=" + score +
                ", turns=" + turns +
                '}';
    }
}
