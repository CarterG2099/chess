package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{

    private final Collection<ChessMove> chessMoveCollection = new ArrayList<>();
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        chessMoveCollection.addAll(new BishopMovesCalculator().pieceMoves(board, position, color));
        chessMoveCollection.addAll(new RookMovesCalculator().pieceMoves(board, position, color));

//        int[][] directions = {{1,-1},{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1}};
//        for(int[] move : directions){
//            int row = position.getRow();
//            int col = position.getColumn();
//            while(true){
//                row += move[0];
//                col += move[1];
//                //Out of bounds
//                if(row < 1 || row > 8 || col < 1 || col > 8) break;
//                ChessPosition newPosition = new ChessPosition(row, col);
//                ChessPiece pieceAtPosition = board.getPiece(newPosition);
//                //Empty space
//                if(pieceAtPosition == null) chessMoveCollection.add(new ChessMove(position, newPosition, null));
//                //Opponent team so capture and stop progress
//                else if(pieceAtPosition.getTeamColor() != color){
//                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
//                    break;
//                }
//                else break; //Same color
//            }
//        }
        return chessMoveCollection;
    }
}
