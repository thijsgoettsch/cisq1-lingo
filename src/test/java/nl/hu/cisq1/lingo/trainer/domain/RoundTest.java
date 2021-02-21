package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @Test
    @DisplayName("increase round number when the word has been guessed")
    void increaseRoundNumber() {
        Word word = new Word("boord");
        Feedback feedback = new Feedback("boord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        Round round = new Round(word, feedback);
        assertTrue(round.increaseRound());
    }

    @Test
    @DisplayName("Dont increase round number when the word has not been guessed")
    void dontIncreaseRoundNumber() {
        Word word = new Word("woord");
        Feedback feedback = new Feedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        Round round = new Round(word, feedback);
        assertFalse(round.increaseRound());
    }

    @Test
    @DisplayName("increase turn number when the word has not been guessed")
    void increaseTurnNumber() {
        Word word = new Word("neef");
        Feedback feedback = new Feedback("hout", List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT));
        Round round = new Round(word, feedback);
        assertTrue(round.increaseTurn());
    }

    @Test
    @DisplayName("do not increase turn number when the word has been guessed")
    void dontIncreaseTurnNumber() {
        Word word = new Word("neef");
        Feedback feedback = new Feedback("neef", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        Round round = new Round(word, feedback);
        assertFalse(round.increaseTurn());
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("Give hint when an attempt has been made")
    void giveHint(String attempt, Word wordToGuess, List<Character> hint) {

        Word word = new Word(wordToGuess.getValue());
        Feedback feedback = new Feedback(attempt, List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT));
        Round round = new Round(word, feedback);

        assertEquals(round.giveFeedback(List.of('.', '.', 'o', '.', '.')), hint);
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("woord", "boord", List.of('.', 'o', 'o', 'r', 'd')),
                Arguments.of("dansje", "diesel", List.of('d', '.', '.', 's', '.', '.')),
                Arguments.of("boom", "kies", List.of('.', '.', '.', '.'))
        );
    }
}