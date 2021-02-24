package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class TurnNumberOutOfRangeException extends RuntimeException {
    public TurnNumberOutOfRangeException() {
        super("Turn number is out of range");
    }
}
