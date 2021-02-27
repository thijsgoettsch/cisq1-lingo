package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class GameIsNotActiveException extends RuntimeException {
    public GameIsNotActiveException() {
        super("No games has been started");
    }
}
