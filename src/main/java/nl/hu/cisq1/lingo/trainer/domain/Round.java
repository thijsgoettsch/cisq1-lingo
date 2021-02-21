package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.List;

public class Round {
    private int currentRound;
    private int currentTurn;
    private Word wordToGuess;
    private Feedback feedback;

    public Round(Word word, Feedback feedback) {
        this.wordToGuess = word;
        this.feedback = feedback;
        this.currentRound = 1;
        this.currentTurn = 1;

    }

    public boolean increaseRound() {
        if (feedback.isWordGuessed()) {
            this.currentRound = this.currentTurn + 1;
            this.currentTurn = 1;
            return true;
        }
        return false;
    }

    public boolean increaseTurn() {
        if (!feedback.isWordGuessed()) {
            this.currentTurn = this.currentTurn + 1;
            return true;
        }
        return false;
    }

    public List<Character> giveFeedback(List<Character> previousHint) {
        return feedback.giveHint(previousHint, this.wordToGuess.getValue());
    }
}

