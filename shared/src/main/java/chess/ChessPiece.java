package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessMoveRules moveRules = new ChessMoveRules(); // Create an instance of ChessMoveRules
        switch (this.getPieceType()) {
            case KING:
                return moveRules.kingMoveRules(myPosition);
            case QUEEN:
                return moveRules.queenMoveRules(myPosition);
            case BISHOP:
                return moveRules.bishopMoveRules(myPosition);
            case KNIGHT:
                return moveRules.knightMoveRules(myPosition);
            case ROOK:
                return moveRules.rookMoveRules(myPosition);
            case PAWN:
                return moveRules.pawnMoveRules(myPosition);
        }
        return new ArrayList<>();
    }
}
