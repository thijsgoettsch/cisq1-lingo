package nl.hu.cisq1.lingo.trainer.domain;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
@ToString
@EqualsAndHashCode
public class Feedback {
    private String attempt;
    private List<Mark> marks;
    public Feedback(String attempt, List<Mark> marks) {
        this.attempt = attempt;
        this.marks = marks;
    }

    public boolean isWordGuessed() {
        return marks.stream().allMatch(Mark.CORRECT::equals);
    }

    public boolean guessIsInvalid() {
        if(this.marks.size() != this.attempt.length()) {
            return true;
        }
        return marks.stream().anyMatch(Mark.INVALID::equals);
    }

    public List<Character> giveHint(String wordToGuess) {
        List<Character> hint = new ArrayList<>();
        char[] attempt = this.attempt.toLowerCase().toCharArray();
        char[] correctWord = wordToGuess.toLowerCase().toCharArray();
        int minLength = Math.min(attempt.length, correctWord.length);

        for (int i = 0; i < minLength; i++) {
            if (attempt[i] == correctWord[i]) {
                // letter is correct
                hint.add(attempt[i]);
            } else {
                // letter is not correct
                hint.add('.');
            }
        }
        return hint;
    }
}
