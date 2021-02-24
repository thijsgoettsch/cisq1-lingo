package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundNotFinishedException;
import nl.hu.cisq1.lingo.words.domain.Word;
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

    @ParameterizedTest
    @MethodSource("provideWordsExamples")
    @DisplayName("start a new lingo game")
    void startNewLingoGame(String wordToGuess, Round round) {
        Player player = new Player("Speler1");
        List<Round> rounds = new ArrayList<>();
        Word word = new Word(wordToGuess);
        Score score = new Score(0);
        Game lingo = new Game(player, rounds, score);

        assertEquals(lingo.startNewGame(word), round);
    }

    @ParameterizedTest
    @MethodSource("provideFinishedRoundExamples")
    @DisplayName("start a new round in the game")
    void startNewRound(String wordToGuess, String attempt, List<Mark> marks, Round round) {
        Player player = new Player("Speler1");
        List<Round> rounds = new ArrayList<>();
        Word word = new Word(wordToGuess);
        Score score = new Score(0);
        Game lingo = new Game(player, rounds, score);

        Round newRound = new Round(word);
        newRound.getFeedback(attempt, marks);
        rounds.add(newRound);

        assertEquals(lingo.startNewRound(word), round);
    }

    @ParameterizedTest
    @MethodSource("provideUnfinishedRoundExamples")
    @DisplayName("throws an exception when trying to start a new round while there is still a round that has not been finished")
    void throwExceptionOnUnfinishedRound(String wordToGuess, String attempt, List<Mark> marks, Round round) {
        Player player = new Player("Speler1");
        List<Round> rounds = new ArrayList<>();
        Word word = new Word(wordToGuess);
        Score score = new Score(25);

        Game lingo = new Game(player, rounds, score);

        Round newRound = new Round(word);
        newRound.getFeedback(attempt, marks);
        rounds.add(newRound);

        assertThrows(RoundNotFinishedException.class, () -> {
            lingo.startNewRound(word);
        });
    }


    static Stream<Arguments> provideFinishedRoundExamples() {
        return Stream.of(
                Arguments.of("tosti", "tosti", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT), new Round(new Word("tosti"))),
                Arguments.of("hallo", "hallo", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT), new Round(new Word("hallo"))),
                Arguments.of("zestig", "zestig", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT), new Round(new Word("zestig"))),
                Arguments.of("vier", "vier", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT), new Round(new Word("vier")))
        );
    }

    static Stream<Arguments> provideUnfinishedRoundExamples() {
        return Stream.of(
                Arguments.of("tosti", "josti", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT), new Round(new Word("tosti"))),
                Arguments.of("hallo", "haloo", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.PRESENT, Mark.CORRECT), new Round(new Word("hallo"))),
                Arguments.of("zestig", "westig", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT), new Round(new Word("zestig"))),
                Arguments.of("vier", "bier", List.of(Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT), new Round(new Word("vier")))
        );
    }

    static Stream<Arguments> provideWordsExamples() {
        return Stream.of(
                Arguments.of("woord", new Round(new Word("woord"))),
                Arguments.of("twee", new Round(new Word("twee"))),
                Arguments.of("zeven", new Round(new Word("zeven")))
        );
    }


}