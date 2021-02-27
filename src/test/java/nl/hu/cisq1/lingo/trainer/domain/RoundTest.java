package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.TurnNumberOutOfRangeException;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {
    @Test
    @DisplayName("round is finished based on the word that has been guessed")
    void roundIsFinished() {
        Word word = new Word("boord");
        Round round = new Round(word);
        round.getFeedback("boord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(round.roundFinished());
    }

    @Test
    @DisplayName("round is not finished based on guess attempt that has been made")
    void roundIsNotFinished() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.getFeedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertFalse(round.roundFinished());
    }

    @Test
    @DisplayName("round is finished based on too many guesses")
    void roundIsNotFinishedTooManyAttempts() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.getFeedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(round.roundFinished());
    }

    @Test
    @DisplayName("Increase turn number when word has not been guessed")
    void increaseTurnNumber() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.getFeedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertTrue(round.increaseTurn(1));
    }

    @Test
    @DisplayName("Dont increase turn number when word has been guessed")
    void dontIncreaseTurnNumber() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.getFeedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertFalse(round.increaseTurn(1));
    }

    @Test
    @DisplayName("Dont increase turn number when turn number is 5 or greater than 5")
    void dontIncreaseTurnNumberBasedOnAmountOfTurns() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.getFeedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertFalse(round.increaseTurn(1));
    }

    @Test
    @DisplayName("check if turn number matches")
    void checkCurrentTurnNumber() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.getFeedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertEquals(2, round.getCurrentTurn());
    }

    @ParameterizedTest
    @MethodSource("provideFeedbackExamples")
    @DisplayName("see all the feedback that has been given during a round")
    void getFeedbackFromCurrentRound(String wordToGuess, List<Feedback> feedbackList) {
        Word word = new Word(wordToGuess);
        Round round = new Round(word);
        round.getFeedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        round.getFeedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        assertEquals(feedbackList, round.getAllFeedback());
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("get feedback when a guess has been made")
    void getFeedback(String attempt, String wordToGuess, List<Character> hint) {
        Word word = new Word(wordToGuess);
        Round round = new Round(word);
        assertEquals(round.getFeedback(attempt, List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)), hint);
    }

    @Test
    @DisplayName("get the length of the word that needs to be quessed")
    void getWordLength() {
        Word word = new Word("woord");
        Round round = new Round(word);
        assertEquals(5, round.getWordLength());
    }


    @Test
    @DisplayName("see if new score has been created based on the turns of a round")
    void getScoreOfRound() {
        Word word = new Word("test");
        Round round = new Round(word);
        round.increaseTurn(1);
        assertEquals(new Score(1), round.roundScore());
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("woord", "boord", List.of('.', 'o', 'o', 'r', 'd')),
                Arguments.of("dansje", "diesel", List.of('d', '.', '.', 's', '.', '.')),
                Arguments.of("boom", "kies", List.of('.', '.', '.', '.'))
        );
    }

    static Stream<Arguments> provideFeedbackExamples() {
        return Stream.of(
                Arguments.of("woord", List.of(new Feedback("boord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)), new Feedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))))
        );
    }
}