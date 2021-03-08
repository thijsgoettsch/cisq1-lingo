package nl.hu.cisq1.lingo.trainer.domain;

import lombok.Getter;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameIsLostException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.GameIsNotActiveException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundAlreadyFinishedException;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundNotFinishedException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Player player;
    private List<Round> rounds;
    private Round currentRound;
    private GameStatus gameStatus;
    @Getter
    private int totalScore;

    public Game(Player player, List<Round> rounds) {
        this.player = player;
        this.rounds = rounds;
        this.gameStatus = GameStatus.NOT_ACTIVE;
    }

    public void startNewGame(Word word) {
        this.gameStatus = GameStatus.ONGOING;
        startNewRound(word);
    }

    public void startNewRound(Word word) {
        if (this.gameStatus == GameStatus.NOT_ACTIVE) {
            throw new GameIsNotActiveException();
        }
        if (!rounds.stream().allMatch(Round::roundFinished)) {
            throw new RoundNotFinishedException();
        }
        if (gameLost() == GameStatus.LOST) {
            throw new GameIsLostException();
        }
        Round round = new Round(word);
        rounds.add(round);
    }

    public Round makeGuess(String attempt) {
        if (this.gameStatus == GameStatus.NOT_ACTIVE) {
            throw new GameIsNotActiveException();
        }
        if (rounds.stream().allMatch(Round::roundFinished)) {
            throw new RoundAlreadyFinishedException();
        }
        getCurrentRound();
        this.currentRound.makeGuess(attempt);
        return this.currentRound;
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
        for (Round round : rounds) {
            if (round.roundFinished()) {
                totalScore += round.roundScore().calculateScore();
            }
        }
        return totalScore;
    }

    public List<Round> getRounds() {
        return this.rounds;
    }

    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    public GameStatus gameLost() {
        if(this.rounds.stream().anyMatch(Round::roundLost)) {
            this.gameStatus = GameStatus.LOST;
        }
        return gameStatus;
    }

}
