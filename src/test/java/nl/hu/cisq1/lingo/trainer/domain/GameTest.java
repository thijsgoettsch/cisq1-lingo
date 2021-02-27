package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameIsNotActiveException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundNotFinishedException;
import nl.hu.cisq1.lingo.words.domain.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static final Player player = new Player("Speler1");
    private List<Round> rounds = new ArrayList<>();

    @BeforeEach
    void init() {
        this.rounds = new ArrayList<>();
    }

    @Test
    @DisplayName("start a new lingo game")
    void startNewLingoGame() {
        Word word = new Word("woord");
        Game lingo = new Game(player, rounds);
        lingo.startNewGame(word);

        assertEquals(1, lingo.getRounds().size());
    }

    @ParameterizedTest
    @MethodSource("provideFinishedRoundExamples")
    @DisplayName("start a new round in the game")
    void startNewRound(String wordToGuess, String attempt, List<Mark> marks) {
        Game lingo = new Game(player, rounds);
        lingo.startNewGame(new Word(wordToGuess));
        lingo.getRounds().get(0).getFeedback(attempt, marks);

        lingo.startNewRound(new Word("woord"));
        assertEquals(2, lingo.getRounds().size());
    }

    @Test
    @DisplayName("Throws GameIsNotActiveException when game has not been started")
    void shouldThrowExceptionWhenThereIsNoGameActive() {
        Game lingo = new Game(player, rounds);
        assertThrows(GameIsNotActiveException.class, () -> lingo.startNewRound(new Word("hallo")));
    }

    @Test
    @DisplayName("Should not throw GameIsNotActiveException when game has been started")
    void shouldNotThrowExceptionWhenThereIsNoGameActive() {
        Game lingo = new Game(player, rounds);
        lingo.startNewGame(new Word("woord"));
        lingo.getRounds().get(0).getFeedback("woord", List.of(Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT));

        assertDoesNotThrow(() -> {
            lingo.startNewRound(new Word("hallo"));
        });
    }

    @ParameterizedTest
    @MethodSource("provideUnfinishedRoundExamples")
    @DisplayName("throws an exception when trying to start a new round while there is still a round that has not been finished")
    void throwExceptionOnUnfinishedRound(String wordToGuess, String attempt, List<Mark> marks) {
        Word word = new Word(wordToGuess);
        Game lingo = new Game(player, rounds);

        lingo.startNewGame(new Word("woord"));
        lingo.getRounds().get(0).getFeedback("woord", List.of(Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT));

        Round newRound = new Round(word);
        newRound.getFeedback(attempt, marks);
        rounds.add(newRound);

        assertThrows(RoundNotFinishedException.class, () -> {
            lingo.startNewRound(word);
        });
    }

    @ParameterizedTest
    @MethodSource("provideFinishedRoundExamples")
    @DisplayName("should not throw an exception when trying to start a new round while all the rounds are finished")
    void shouldNotThrowExceptionOnFinishedRound(String wordToGuess, String attempt, List<Mark> marks) {
        Word word = new Word(wordToGuess);

        Game lingo = new Game(player, rounds);
        lingo.startNewGame(new Word("woord"));
        lingo.getRounds().get(0).getFeedback("woord", List.of(Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT));

        Round newRound = new Round(word);
        newRound.getFeedback(attempt, marks);
        rounds.add(newRound);

        assertDoesNotThrow(() -> {
            lingo.startNewRound(word);
        });
    }

    @Test
    @DisplayName("calculate the total score")
    void calculateScore() {
        Word word = new Word("test");
        Game lingo = new Game(player, rounds);

        Round newRound = new Round(word);
        newRound.getFeedback("woord", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        newRound.getFeedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));

        Round newRound2 = new Round(word);
        newRound2.getFeedback("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
        rounds.add(newRound);
        rounds.add(newRound2);

        assertEquals(45, lingo.calculateScore());
    }

    @Test
    @DisplayName("get the word length of current round")
    void getWordLength() {
        Word word = new Word("woord");
        Round round = new Round(word);
        rounds.add(round);
        Game lingo = new Game(player, rounds);
        assertEquals(5, lingo.currentWordLength());
    }

    @Test
    @DisplayName("get the current round")
    void getCurrentRound() {
        Word word = new Word("woord");
        Round round = new Round(word);
        rounds.add(round);
        Game lingo = new Game(player, rounds);
        assertEquals(new Round(new Word("woord")), lingo.getCurrentRound());
    }

    @Test
    @DisplayName("there is a game active")
    void gameIsActive() {
        Word word = new Word("woord");
        Game lingo = new Game(player, rounds);
        lingo.startNewGame(word);
        assertTrue(lingo.gameActive());
    }

    @Test
    @DisplayName("there isn't a game active")
    void gameIsNotActive() {
        Game lingo = new Game(player, rounds);
        assertFalse(lingo.gameActive());
    }

    static Stream<Arguments> provideFinishedRoundExamples() {
        return Stream.of(
                Arguments.of("tosti", "tosti", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of("hallo", "hallo", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of("zestig", "zestig", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))
        );
    }

    static Stream<Arguments> provideUnfinishedRoundExamples() {
        return Stream.of(
                Arguments.of("tosti", "josti", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of("hallo", "haloo", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT)),
                Arguments.of("bestig", "westig", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT))
        );
    }



}