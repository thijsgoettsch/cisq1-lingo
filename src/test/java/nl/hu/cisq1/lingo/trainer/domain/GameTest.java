package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameIsNotActiveException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundAlreadyFinishedException;
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
    void startNewRound(String wordToGuess, String attempt) {
        Game lingo = new Game(player, rounds);
        lingo.startNewGame(new Word(wordToGuess));
        lingo.getRounds().get(0).makeGuess(attempt);

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
        lingo.getRounds().get(0).makeGuess("woord");

        assertDoesNotThrow(() -> {
            lingo.startNewRound(new Word("hallo"));
        });
    }

    @ParameterizedTest
    @MethodSource("provideUnfinishedRoundExamples")
    @DisplayName("throws an exception when trying to start a new round while there is still a round that has not been finished")
    void throwExceptionOnUnfinishedRound(String wordToGuess, String attempt) {
        Word word = new Word(wordToGuess);
        Game lingo = new Game(player, rounds);

        lingo.startNewGame(new Word("woord"));
        lingo.getRounds().get(0).makeGuess("woord");

        Round newRound = new Round(word);
        newRound.makeGuess(attempt);
        rounds.add(newRound);

        assertThrows(RoundNotFinishedException.class, () -> {
            lingo.startNewRound(word);
        });
    }

    @ParameterizedTest
    @MethodSource("provideFinishedRoundExamples")
    @DisplayName("should not throw an exception when trying to start a new round while all the rounds are finished")
    void shouldNotThrowExceptionOnFinishedRound(String wordToGuess, String attempt) {
        Word word = new Word(wordToGuess);

        Game lingo = new Game(player, rounds);
        lingo.startNewGame(new Word("woord"));
        lingo.getRounds().get(0).makeGuess("woord");

        Round newRound = new Round(word);
        newRound.makeGuess(attempt);
        rounds.add(newRound);

        assertDoesNotThrow(() -> {
            lingo.startNewRound(word);
        });
    }

    @Test
    @DisplayName("calculate the total score")
    void calculateScore() {
        Word word = new Word("boord");
        Game lingo = new Game(player, rounds);

        Round newRound = new Round(word);
        newRound.makeGuess("woord");
        newRound.makeGuess("boord");

        Round newRound2 = new Round(word);
        newRound2.makeGuess("boord");
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

    @Test
    @DisplayName("get the total score of the game")
    void getTotalScore() {
        Game lingo = new Game(player, rounds);
        assertEquals(0, lingo.getTotalScore());
    }

    @ParameterizedTest
    @MethodSource("provideFinishedRoundExamples")
    @DisplayName("make a guess")
    void makeGuess(String wordToGuess, String attempt) {
        Game lingo = new Game(player, rounds);
        lingo.startNewGame(new Word(wordToGuess));

        Round actual = new Round(new Word(wordToGuess));
        actual.makeGuess(attempt);

        assertEquals(lingo.makeGuess(attempt), actual);
    }

    @Test
    @DisplayName("make a guess shold throw game is not active exception because there is no game active")
    void makeGuessShouldThrowGameIsNotActiveException() {
        Game lingo = new Game(player, rounds);
        assertThrows(GameIsNotActiveException.class, () -> {
            lingo.makeGuess("kalkoen");
        });
    }

    @Test
    @DisplayName("make a guess shold throw round already finished exception because a round is already finished")
    void makeGuessShouldThrowRoundAlreadyFinishedException() {
        Game lingo = new Game(player, rounds);
        lingo.startNewGame(new Word("woord"));
        lingo.makeGuess("woord");
        assertThrows(RoundAlreadyFinishedException.class, () -> {
            lingo.makeGuess("woord");
        });
    }

    static Stream<Arguments> provideFinishedRoundExamples() {
        return Stream.of(
                Arguments.of("tosti", "tosti"),
                Arguments.of("hallo", "hallo"),
                Arguments.of("zestig", "zestig")
        );
    }

    static Stream<Arguments> provideUnfinishedRoundExamples() {
        return Stream.of(
                Arguments.of("tosti", "josti"),
                Arguments.of("hallo", "haloo"),
                Arguments.of("bestig", "westig")
        );
    }



}