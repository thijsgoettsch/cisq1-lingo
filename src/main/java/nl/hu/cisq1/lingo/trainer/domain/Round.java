package nl.hu.cisq1.lingo.trainer.domain;

import nl.hu.cisq1.lingo.trainer.domain.Feedback;
import nl.hu.cisq1.lingo.words.domain.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Round {

    private int currentTurn;
    private Word wordToGuess;

    private List<Feedback> feedbackList = new ArrayList<>();

    public Round(Word word) {
        this.wordToGuess = word;
        this.currentTurn = 0;
    }

    public boolean roundFinished() {
        if (currentTurn >= 5 || feedbackList.stream().anyMatch(Feedback::isWordGuessed)) {
            return true;
        }
        return false;
    }

    public List<Character> getFeedback(String attempt, List<Mark> marks) {
        Feedback feedback = new Feedback(attempt, marks);
        feedbackList.add(feedback);
        if (!roundFinished()) {
            increaseTurn(1);
        }
        return feedback.giveHint(this.wordToGuess.getValue());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Round round = (Round) o;
        return currentTurn == round.currentTurn &&
                Objects.equals(wordToGuess, round.wordToGuess) &&
                Objects.equals(feedbackList, round.feedbackList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTurn, wordToGuess, feedbackList);
    }

    @Override
    public String toString() {
        return "Round{" +
                "currentTurn=" + currentTurn +
                ", wordToGuess=" + wordToGuess +
                ", feedbackList=" + feedbackList +
                '}';
    }
}

