package chess;

/**
 * Indicates an invalid move was made in a game
 */
public class InvalidTurnException extends InvalidMoveException {

    public InvalidTurnException() {
    }

    public InvalidTurnException(String message) {
        super(message);
    }
}
