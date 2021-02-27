package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameIsNotActiveException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundNotFinishedException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player player;
    private List<Round> rounds;
    private Round currentRound;
    private boolean isGameActive;
    private int totalScore;

    public Game(Player player, List<Round> rounds) {
        this.player = player;
        this.rounds = rounds;
        this.isGameActive = false;
    }

    public void startNewGame(Word word) {
        this.isGameActive = true;
        startNewRound(word);
    }

    public void startNewRound(Word word) {
        if (!isGameActive) {
            throw new GameIsNotActiveException();
        }
        if (!rounds.stream().allMatch(Round::roundFinished)) {
            throw new RoundNotFinishedException();
        }
        Round round = new Round(word);
        rounds.add(round);
    }

    public Round getCurrentRound() {
        for (Round round : rounds) {
            if (!round.roundFinished()) {
                this.currentRound = round;
            }
        }
        return this.currentRound;
    }

    public int currentWordLength() {
        int wordLength = 0;
        for (Round round : rounds) {
            wordLength = round.getWordLength();
        }
        return wordLength;
    }

    public int calculateScore() {
        for (int i = 0; i < rounds.size(); i++) {
            if(rounds.get(i).roundFinished()) {
                totalScore += rounds.get(i).roundScore().calculateScore();
            }
        }
        return totalScore;
    }

    public List<Round> getRounds() {
        return this.rounds;
    }

    public boolean gameActive() {
        return this.isGameActive;
    }

}
