//package chess;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class ChessMoveRules {
//
//    private final Collection<ChessMove> chessMoveCollection = new ArrayList<ChessMove>();
//    public ChessMoveRules () {
//
//    }
//
//    Collection<ChessMove> kingMoveRules(ChessPosition position, ChessGame.TeamColor color) {
//        return chessMoveCollection;
//    }
//
//    /**
//     * Returns all the possible positions that the queen can move to
//     * @param position position of chessPiece
//     * @param color team color of chess piece
//     * @return Collection of chessMoves
//     */
//    Collection<ChessMove> queenMoveRules(ChessPosition position, ChessGame.TeamColor color) {
//        return chessMoveCollection;
//    }
//
//    /**
//     * Returns all the possible positions that the bishop piece can move to
//     * @param position
//     * @param color
//     * @return Collection of chessMoves
//     */
//    Collection<ChessMove> bishopMoveRules(ChessPosition position, ChessGame.TeamColor color) {
//        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
//        for (int[] move : directions){
//            int row = position.getRow();
//            int col = position.getColumn();
//            while(true) {
//                row +=  move[0];
//                col += move[1];
//                //Check for out of bounds
//                if (row < 1 || row > 8 || col < 1 || col > 8) break;
//
//                ChessPosition newPosition = new ChessPosition(row, col);
//                ChessPiece pieceAtPosition = ChessBoard.getPiece(newPosition);
//                //Empty space
//                if (pieceAtPosition == null) {
//                    chessMoveCollection.add(new ChessMove(position, newPosition,null));
//                }
//                //Opponent occupies the space - capture it
//                else if (pieceAtPosition.getTeamColor() != color) {
//                    chessMoveCollection.add(new ChessMove(position, newPosition,null));
//                    break;
//                    }
//                else {break;} // Same color
//                }
//            }
//        return chessMoveCollection;
//    }
//    Collection<ChessMove> knightMoveRules(ChessPosition position, ChessGame.TeamColor color) {
//        int[][] directions = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {-1, 2}, {1, -2}, {-1, -2}};
//        for (int[] move : directions) {
//            int row = position.getRow();
//            int col = position.getColumn();
//            while(true){
//                row += move[0];
//                col += move[1];
//                if (row < 1 || row > 8 || col < 1 || col > 8) break;
//                ChessPosition newPosition = new ChessPosition(row, col);
//                ChessPiece pieceAtPosition = ChessBoard.getPiece(newPosition);
//                if (pieceAtPosition == null) chessMoveCollection.add(new ChessMove(position, newPosition, null));
//                else if (pieceAtPosition.getTeamColor() != color) {
//                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
//                    break;
//                }
//                else break;
//            }
//
//        }
//
//        return chessMoveCollection;
//    }
//    Collection<ChessMove> rookMoveRules(ChessPosition position, ChessGame.TeamColor color) {
//        return chessMoveCollection;
//    }
//    Collection<ChessMove> pawnMoveRules(ChessPosition position, ChessGame.TeamColor color) {
//        int row = position.getRow();
//        int col = position.getColumn();
//        if (color == ChessGame.TeamColor.WHITE){
//            //Check for diagonal attacks
//            ChessPosition newPosition = new ChessPosition(row+1, col-1);
//            if (ChessBoard.getPiece(newPosition) != null){
//                    if (ChessBoard.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
//                        if (row == 7) promotionMoves(position, newPosition);
//                        else chessMoveCollection.add(new ChessMove(position, newPosition, null));
//                }
//            }
//            newPosition = new ChessPosition(row+1, col+1);
//            if (ChessBoard.getPiece(newPosition) != null){
//                if (ChessBoard.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
//                    if (row == 7) promotionMoves(position, newPosition);
//                    else chessMoveCollection.add(new ChessMove(position, newPosition, null));                }
//            }
//            //Check for forward move
//            newPosition = new ChessPosition(row+1, col);
//            if (ChessBoard.getPiece(newPosition) == null){
//                if (row == 7) promotionMoves(position, newPosition);
//                else chessMoveCollection.add(new ChessMove(position, newPosition, null));
//                //First move can move forward 2
//                newPosition = new ChessPosition(row+2, col);
//                if (row == 2 && ChessBoard.getPiece(newPosition) == null){
//                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
//                }
//            }
//        }
//
//        //Black Pawn moves
//        else {
//            //Check for diagonal attack
//            ChessPosition newPosition = new ChessPosition(row-1, col-1);
//            if (ChessBoard.getPiece(newPosition) != null){
//                if (ChessBoard.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
//                    if (row == 2) promotionMoves(position, newPosition);
//                    else chessMoveCollection.add(new ChessMove(position, newPosition, null));
//                }
//            }
//            newPosition = new ChessPosition(row-1, col+1);
//            if (ChessBoard.getPiece(newPosition) != null){
//                if (ChessBoard.getPiece(newPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
//                    if (row == 2) promotionMoves(position, newPosition);
//                    else chessMoveCollection.add(new ChessMove(position, newPosition, null));                }
//            }
//            //Check for forward move
//            newPosition = new ChessPosition(row-1, col);
//            if (ChessBoard.getPiece(newPosition) == null){
//                if (row == 2) promotionMoves(position, newPosition);
//                else chessMoveCollection.add(new ChessMove(position, newPosition, null));
//                //First move can move forward 2
//                newPosition = new ChessPosition(row-2, col);
//                if (row == 7 && ChessBoard.getPiece(newPosition) == null){
//                    chessMoveCollection.add(new ChessMove(position, newPosition, null));
//                }
//            }
//        }
//        return chessMoveCollection;
//    }
//
//    private void promotionMoves(ChessPosition start, ChessPosition end) {
//        chessMoveCollection.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
//        chessMoveCollection.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
//        chessMoveCollection.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
//        chessMoveCollection.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
//    }
//
//    @Override
//    public int hashCode() {
//        return super.hashCode();
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        return super.equals(obj);
//    }
//}
