package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.TurnNumberOutOfRangeException;

import java.util.Objects;
@ToString
@EqualsAndHashCode
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


}
