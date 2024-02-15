package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final PieceType type;
    private final ChessGame.TeamColor color;
    private boolean hasMoved = false;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.color = pieceColor;
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
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    public boolean hasPieceMoved() {
        return hasMoved;
    }

    public void pieceMoved() {
        hasMoved = true;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (this.getPieceType()) {
            case KING:
                KingMovesCalculator kingMovesCalculator = new KingMovesCalculator();
                if (!hasMoved) kingMovesCalculator.castling(board, myPosition, this.color);
                return kingMovesCalculator.pieceMoves(board, myPosition, this.color);
            case QUEEN:
                QueenMovesCalculator queenMovesCalculator = new QueenMovesCalculator();
                return queenMovesCalculator.pieceMoves(board, myPosition, this.color);
            case BISHOP:
                BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator();
                return bishopMovesCalculator.pieceMoves(board, myPosition, this.color);
            case KNIGHT:
                KnightMovesCalculator knightMovesCalculator = new KnightMovesCalculator();
                return knightMovesCalculator.pieceMoves(board, myPosition, this.color);
            case ROOK:
                RookMovesCalculator rookMovesCalculator = new RookMovesCalculator();
                return rookMovesCalculator.pieceMoves(board, myPosition, this.color);
            case PAWN:
                PawnMovesCalculaor pawnMovesCalculator = new PawnMovesCalculaor();
                return pawnMovesCalculator.pieceMoves(board, myPosition, this.color);
        }
        return new ArrayList<>();
    }

    public String toString() {
        if (this.type == null) {
            return "Color=NULL Piece=NULL";
        } else {
            return this.color + "," + this.type + " ";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return Objects.equals(type, that.type) && Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color);
    }
}
