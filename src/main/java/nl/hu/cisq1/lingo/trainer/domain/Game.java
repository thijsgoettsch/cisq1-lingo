package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundNotFinishedException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player player;
    private List<Round> rounds = new ArrayList<>();
    private Score score;
    public Game(Player player, List<Round> rounds, Score score) {
        this.player = player;
        this.rounds = rounds;
        this.score = score;
    }

    public Round startNewGame(Word word) {
        Round round = new Round(word);
        rounds.add(round);
        return round;
    }

    public Round startNewRound(Word word) {
        if (!rounds.stream().allMatch(Round::roundFinished)) {
            throw new RoundNotFinishedException();
        }
        Round round = new Round(word);
        rounds.add(round);
        return round;
    }

}
