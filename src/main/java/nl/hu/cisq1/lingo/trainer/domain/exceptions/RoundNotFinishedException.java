package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class RoundNotFinishedException extends RuntimeException {
    public RoundNotFinishedException() {
        super("Round is not finished yet");
    }
}
