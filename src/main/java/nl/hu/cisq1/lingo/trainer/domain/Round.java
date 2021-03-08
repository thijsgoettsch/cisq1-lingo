package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.trainer.domain.exceptions.RoundAlreadyFinishedException;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@ToString
@EqualsAndHashCode
public class Round {

    private int currentTurn;
    private Word wordToGuess;

    private List<Feedback> feedbackList = new ArrayList<>();

    public Round(Word word) {
        this.wordToGuess = word;
        this.currentTurn = 0;
    }

    public boolean roundFinished() {
        return currentTurn >= 5 || feedbackList.stream().anyMatch(Feedback::isWordGuessed);
    }

    public Feedback makeGuess(String attempt) {
        if (!roundFinished()) {
            increaseTurn(1);
            Feedback feedback = new Feedback(attempt);
            feedback.giveHint(wordToGuess.getValue());
            feedbackList.add(feedback);
            return feedback;
        }
        throw new RoundAlreadyFinishedException();

    }

    public boolean increaseTurn(Integer amount) {
        if (!roundFinished()) {
            if (feedbackList.stream().noneMatch(Feedback::isWordGuessed)) {
                this.currentTurn += amount;
                return true;
            }
            return false;
        }
        return false;
    }

    public int getCurrentTurn() {
        return this.currentTurn;
    }

    public List<Feedback> getAllFeedback() {
        return this.feedbackList;
    }

    public Score roundScore() {
        return new Score(this.currentTurn);
    }

    public int getWordLength() {
        return this.wordToGuess.getLength();
    }

    public boolean roundLost() {
        if (this.currentTurn >= 5 && feedbackList.stream().noneMatch(Feedback::isWordGuessed)) {
            return true;
        }
        return false;
    }

}

