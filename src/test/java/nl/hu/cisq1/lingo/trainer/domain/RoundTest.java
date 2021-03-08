package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundAlreadyFinishedException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundNotFinishedException;
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
        round.makeGuess("boord");
        assertTrue(round.roundFinished());
    }

    @Test
    @DisplayName("round is not finished based on guess attempt that has been made")
    void roundIsNotFinished() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.makeGuess("boord");
        assertFalse(round.roundFinished());
    }

    @Test
    @DisplayName("round is finished based on too many guesses")
    void roundIsFinishedTooManyAttempts() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        assertTrue(round.roundFinished());
    }

    @Test
    @DisplayName("round is lost when player has 5 turns and didnt guess the correct word")
    void roundIsLostOnTooManyTurns() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        assertTrue(round.roundLost());
    }

    @Test
    @DisplayName("round is lost when player has 5 turns and didnt guess the correct word")
    void roundIsNotLost() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("woord");
        assertFalse(round.roundLost());
    }

    @Test
    @DisplayName("Increase turn number when word has not been guessed")
    void increaseTurnNumber() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.makeGuess("boord");
        assertTrue(round.increaseTurn(1));
    }

    @Test
    @DisplayName("Dont increase turn number when word has been guessed")
    void dontIncreaseTurnNumber() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.makeGuess("woord");
        assertFalse(round.increaseTurn(1));
    }

    @Test
    @DisplayName("Dont increase turn number when turn number is 5 or greater than 5")
    void dontIncreaseTurnNumberBasedOnAmountOfTurns() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        round.makeGuess("boord");
        assertFalse(round.increaseTurn(1));
    }

    @Test
    @DisplayName("check if turn number matches")
    void checkCurrentTurnNumber() {
        Word word = new Word("woord");
        Round round = new Round(word);
        round.makeGuess("boord");
        round.makeGuess("woord");
        assertEquals(2, round.getCurrentTurn());
    }

    @ParameterizedTest
    @MethodSource("provideFeedbackExamples")
    @DisplayName("see all the feedback that has been given during a round")
    void getFeedbackFromCurrentRound(String wordToGuess, List<Feedback> feedbackList) {
        Word word = new Word(wordToGuess);
        Round round = new Round(word);
        round.makeGuess("kalkoen");
        round.makeGuess("vrijdag");
        assertEquals(feedbackList, round.getAllFeedback());
    }

    @Test
    @DisplayName("Throws exception when trying to make a guess after the round has been finished")
    void throwsRoundAlreadyFinishedExeption() {
        Word word = new Word("vrijdag");
        Round round = new Round(word);
        round.makeGuess("kalkoen");
        round.makeGuess("vrijdag");

        assertThrows(RoundAlreadyFinishedException.class, () -> {
            round.makeGuess("kalkoen");
        });
    }

    @Test
    @DisplayName("Throws exception when trying to make a guess after the round has been finished")
    void doesNotThrowRoundAlreadyFinishedExeption() {
        Word word = new Word("vrijdag");
        Round round = new Round(word);
        round.makeGuess("kalkoen");

        assertDoesNotThrow(() -> {
            round.makeGuess("vrijdag");
        });
    }

    @ParameterizedTest
    @MethodSource("provideHintExamples")
    @DisplayName("get feedback when a guess has been made")
    void getFeedback(String attempt, String wordToGuess, Feedback feedback) {
        Word word = new Word(wordToGuess);
        Round round = new Round(word);
        assertEquals(feedback.giveHint(wordToGuess), round.makeGuess(attempt).giveHint(wordToGuess));
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
                Arguments.of("woord", "boord", new Feedback("woord")),
                Arguments.of("dansje", "diesel", new Feedback("dansje")),
                Arguments.of("boom", "kies", new Feedback("boom"))
        );
    }

    static Stream<Arguments> provideFeedbackExamples() {
        return Stream.of(
                Arguments.of("vrijdag", List.of(new Round(new Word("vrijdag")).makeGuess("kalkoen"), new Round(new Word("vrijdag")).makeGuess("vrijdag"))),
                Arguments.of("dinsdag", List.of(new Round(new Word("dinsdag")).makeGuess("kalkoen"), new Round(new Word("dinsdag")).makeGuess("vrijdag"))),
                Arguments.of("maandag", List.of(new Round(new Word("maandag")).makeGuess("kalkoen"), new Round(new Word("maandag")).makeGuess("vrijdag")))
        );
    }

}