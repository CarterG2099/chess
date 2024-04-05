package chess.MovesCalculator;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {

    private final Collection<ChessMove> chessMoveCollection = new ArrayList<ChessMove>();

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        int[][] directions = {{1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};
        return moveAllDirections(directions, board, position, color);

    }

    public Collection<ChessMove> moveAllDirections(int[][] directions, ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        for (int[] move : directions) {
            int row = position.getRow() + move[0];
            int col = position.getColumn() + move[1];
            if (row >= 1 && row <= 8 && col >= 1 && col <= 8) {
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);
                if (pieceAtPosition == null) chessMoveCollection.add(new ChessMove(position, newPosition, null));
                else if (pieceAtPosition.getTeamColor() != color)
                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
            }
        }
        return chessMoveCollection;
    }

    public void castling(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        //Add a check to pass tests that start king somewhere other than og
        if (position.getColumn() != 5) return;
        //Make sure rook has also not moved
        ChessPosition firstRookPosition = new ChessPosition(position.getRow(), position.getColumn() - 4);
        ChessPosition firstKnightPosition = new ChessPosition(position.getRow(), position.getColumn() - 3);
        ChessPosition firstBishopPosition = new ChessPosition(position.getRow(), position.getColumn() - 2);
        ChessPosition queenPosition = new ChessPosition(position.getRow(), position.getColumn() - 1);
        ChessPosition secondRookPosition = new ChessPosition(position.getRow(), position.getColumn() + 3);
        ChessPosition secondKnightPosition = new ChessPosition(position.getRow(), position.getColumn() + 2);
        ChessPosition secondBishopPosition = new ChessPosition(position.getRow(), position.getColumn() + 1);

        //Check if Rook is still there left side
        if (board.getPiece(firstRookPosition) != null && board.getPiece(firstRookPosition).getPieceType() == ChessPiece.PieceType.ROOK) {
            //Check if Rook has moved
            if (!board.getPiece(firstRookPosition).hasPieceMoved()) {
                //Check if adjacent positions are null
                if (board.getPiece(firstKnightPosition) == null && board.getPiece(firstBishopPosition) == null && board.getPiece(queenPosition) == null) {
                    //Make sure rook from castling move is not in danger now
                    ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() - 2);
                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
                }
            }
        }

        //Check if Rook is still there right side
        if (board.getPiece(secondRookPosition) != null && board.getPiece(secondRookPosition).getPieceType() == ChessPiece.PieceType.ROOK) {
            //Check if Rook has moved
            if (!board.getPiece(secondRookPosition).hasPieceMoved()) {
                //Check if adjacent positions are empty
                if (board.getPiece(secondKnightPosition) == null && board.getPiece(secondBishopPosition) == null) {
                    //Make sure rook from castling move is not in danger now
                    ChessPosition newPosition = new ChessPosition(position.getRow(), position.getColumn() + 2);
                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
                }
            }
        }

    }

}
