package nl.hu.cisq1.lingo.trainer.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    @DisplayName("Word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback("woord");
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word is not guessed if not all letters are correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback("woord");
        feedback.giveHint("boord");
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Guess is invalid if letters are invalid")
    void guessIsInvalid() {
        Feedback feedback = new Feedback("woord");
        assertTrue(feedback.guessIsInvalid());
    }

    @Test
    @DisplayName("Guess is not invalid if letters are valid")
    void guessIsNotInvalid() {
        Feedback feedback = new Feedback("woord");
        feedback.giveHint("woord");
        assertFalse(feedback.guessIsInvalid());
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("Give hint when an attempt has been made")
    void giveHint(String attempt, String wordToGuess, List<Character> hint) {
        Feedback feedback = new Feedback(attempt);
        assertEquals(feedback.giveHint(wordToGuess), hint);
    }

    @ParameterizedTest
    @MethodSource("provideMarkExamples")
    @DisplayName("generate marks when making a guess")
    void generateMarks(String wordToGuess, String attempt, List<Mark> marks) {
        Feedback feedback = new Feedback(attempt);
        assertEquals(feedback.generateMarks(wordToGuess), marks);
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("woord", "boord", List.of('.', 'o', 'o', 'r', 'd')),
                Arguments.of("dansje", "diesel", List.of('d', '.', '.', 's', '.', '.')),
                Arguments.of("boom", "kies", List.of('.', '.', '.', '.'))
        );
    }

    static Stream<Arguments> provideMarkExamples() {
        return Stream.of(
                Arguments.of("fiets", "fiets", List.of(Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT)),
                Arguments.of("biertje", "viertje", List.of(Mark.ABSENT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of("geel", "fiets", List.of(Mark.INVALID,Mark.INVALID,Mark.INVALID,Mark.INVALID, Mark.INVALID)),
                Arguments.of("auto", "outo", List.of(Mark.PRESENT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT))
                );
    }
}