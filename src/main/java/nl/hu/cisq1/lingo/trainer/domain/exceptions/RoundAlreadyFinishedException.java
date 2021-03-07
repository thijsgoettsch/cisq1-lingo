package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class RoundAlreadyFinishedException extends RuntimeException {
    public RoundAlreadyFinishedException() {
        super("Round is already finished yet");
    }
}
