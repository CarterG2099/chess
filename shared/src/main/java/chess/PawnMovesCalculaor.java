package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculaor implements PieceMovesCalculator {

    private final Collection<ChessMove> chessMoveCollection = new ArrayList<ChessMove>();

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        int row = position.getRow();
        int col = position.getColumn();
        if (color == ChessGame.TeamColor.WHITE) {
            //Check for diagonal attacks
            ChessPosition newPosition = new ChessPosition(row + 1, col - 1);
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (row == 7) promotionMoves(position, newPosition);
                else chessMoveCollection.add(new ChessMove(position, newPosition, null));
            }
            newPosition = new ChessPosition(row + 1, col + 1);
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (row == 7) promotionMoves(position, newPosition);
                else chessMoveCollection.add(new ChessMove(position, newPosition, null));
            }
            //Check for forward move
            newPosition = new ChessPosition(row + 1, col);
            if (board.getPiece(newPosition) == null) {
                if (row == 7) promotionMoves(position, newPosition);
                else chessMoveCollection.add(new ChessMove(position, newPosition, null));
                //First move can move forward 2
                newPosition = new ChessPosition(row + 2, col);
                if (row == 2 && board.getPiece(newPosition) == null) {
                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
                }
            }
        }

        //Black Pawn moves
        else {
            //Check for diagonal attack
            ChessPosition newPosition = new ChessPosition(row - 1, col - 1);
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (row == 2) promotionMoves(position, newPosition);
                else chessMoveCollection.add(new ChessMove(position, newPosition, null));
            }
            newPosition = new ChessPosition(row - 1, col + 1);
            if (board.getPiece(newPosition) != null && board.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                if (row == 2) promotionMoves(position, newPosition);
                else chessMoveCollection.add(new ChessMove(position, newPosition, null));
            }
            //Check for forward move
            newPosition = new ChessPosition(row - 1, col);
            if (board.getPiece(newPosition) == null) {
                if (row == 2) promotionMoves(position, newPosition);
                else chessMoveCollection.add(new ChessMove(position, newPosition, null));
                //First move can move forward 2
                newPosition = new ChessPosition(row - 2, col);
                if (row == 7 && board.getPiece(newPosition) == null) {
                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
                }
            }
        }
        return chessMoveCollection;
    }

    private void promotionMoves(ChessPosition start, ChessPosition end) {
        chessMoveCollection.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        chessMoveCollection.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
        chessMoveCollection.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        chessMoveCollection.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
    }
}
