package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.TurnNumberOutOfRangeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ScoreTest {

    @ParameterizedTest
    @MethodSource("provideTurnNumbers")
    @DisplayName("calculate score based on the amount of turns")
    void calculateScore(int turnNumber, int totalScore) {
        Score score = new Score(turnNumber);
        assertEquals(score.calculateScore(), totalScore);
    }

    @ParameterizedTest
    @MethodSource("provideTurnNumbersGreaterThanMaxTurns")
    @DisplayName("throws exeption when the turn number is higher than 5")
    void shouldThrowExceptionInvalidTurnNumer(int turnNumber) {
        assertThrows(TurnNumberOutOfRangeException.class, () -> new Score(turnNumber));
    }

    @ParameterizedTest
    @MethodSource("provideTurnNumbers")
    @DisplayName("throws exeption when the turn number is higher than 5")
    void shouldNotThrowExceptionCalculateScoreInvalidTurnNumer(int turnNumber) {
        Score score = new Score(turnNumber);
        assertDoesNotThrow(score::calculateScore);
    }

    static Stream<Arguments> provideTurnNumbersGreaterThanMaxTurns() {
        return Stream.of(
                Arguments.of(6),
                Arguments.of(7),
                Arguments.of(8)
        );
    }

    static Stream<Arguments> provideTurnNumbers() {
        return Stream.of(
                Arguments.of(0, 25),
                Arguments.of(1, 20),
                Arguments.of(2, 15),
                Arguments.of(3, 10),
                Arguments.of(4, 5)
                );
    }
}