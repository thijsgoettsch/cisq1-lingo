package nl.hu.cisq1.lingo.trainer.domain.exceptions;

public class GameIsLostException extends RuntimeException {
    public GameIsLostException() {
        super("The game has been lost");
    }
}
