package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    private final Collection<ChessMove> chessMoveCollection = new ArrayList<ChessMove>();

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] move : directions){
            int row = position.getRow();
            int col = position.getColumn();
            while(true) {
                row += move[0];
                col += move[1];
                //Check for out of bounds
                if (row < 1 || row > 8 || col < 1 || col > 8) break;
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece pieceAtPosition = board.getPiece(newPosition);
                //Empty Space
                if (pieceAtPosition == null) chessMoveCollection.add(new ChessMove(position, newPosition, null));
                //Opponents piece capture and stop progress
                else if (pieceAtPosition.getTeamColor() != color) {
                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
                    break;
                }
                else break; //Same color
            }
        }
        return chessMoveCollection;
    }
}
