package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.TurnNumberOutOfRangeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @Test
    @DisplayName("calculate score based on the amount of turns")
    void calculateScore() {
        Score score = new Score(3);
        assertEquals(score.calculateScore(), 15);
    }

    @Test
    @DisplayName("throws exeption when the turn number is higher than 5")
    void calculateScoreInvalidTurnNumer() {
        Score score = new Score(6);
        assertThrows(TurnNumberOutOfRangeException.class, score::calculateScore);
    }
}