package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board = new ChessBoard();
    private TeamColor teamTurn;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() { return this.teamTurn; }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if(team == TeamColor.WHITE) teamTurn = TeamColor.BLACK;
        else teamTurn = TeamColor.WHITE;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     * Calls isInCheck and checkmate - write last
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece pieceToCheck = board.getPiece()
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) {
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
        board.removePiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), pieceToMove);
    }

    /*
     * Determines if the given team is in check
     * Call each pieces moves - nested loops
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> opponentMoves = new ArrayList<>();
        ChessPosition kingPosition = null;
        for(int row = 1; row <= 8; row++){
            for(int col = 1; col <= 8; col++){
                ChessPosition tempPosition = new ChessPosition(row, col);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if(tempPiece != null && tempPiece.getPieceType() == ChessPiece.PieceType.KING && tempPiece.getTeamColor() == teamColor) kingPosition = tempPosition;
                if(tempPiece != null && tempPiece.getTeamColor() != teamColor) {
                    switch (tempPiece.getPieceType()) {
                        case KING:
                            opponentMoves.addAll(new KingMovesCalculaor().pieceMoves(board, tempPosition, tempPiece.getTeamColor()));
                            break;
                        case QUEEN:
                            opponentMoves.addAll(new QueenMovesCalculator().pieceMoves(board, tempPosition, tempPiece.getTeamColor()));
                            break;
                        case KNIGHT:
                            opponentMoves.addAll(new KnightMovesCalculator().pieceMoves(board, tempPosition, tempPiece.getTeamColor()));
                            break;
                        case BISHOP:
                            opponentMoves.addAll(new BishopMovesCalculator().pieceMoves(board, tempPosition, tempPiece.getTeamColor()));
                            break;
                        case ROOK:
                            opponentMoves.addAll(new RookMovesCalculator().pieceMoves(board, tempPosition, tempPiece.getTeamColor()));
                            break;
                        case PAWN:
                            opponentMoves.addAll(new PawnMovesCalculaor().pieceMoves(board, tempPosition, tempPiece.getTeamColor()));
                            break;
                    }
                }
            }
        }

        for(ChessMove move : opponentMoves){
            ChessPosition endPosition = move.getEndPosition();
            if(endPosition.getRow() == kingPosition.getRow() && endPosition.getColumn() == kingPosition.getColumn()){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     * First check if in check and then if any move still leaves King in check - use isincheck on every move if one move is not in check then return false - otherwise return true. Could clone the board and then make the potential move and call isincheck, repeat by cloning board and making move.
     * Cloning is simpler but the other option is to undo a move. Make a move, call isincheck and undo move if neccessary
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessBoard tempBoard = (ChessBoard) board.clone();
        ChessPosition kingPosition = null;
        boolean inCheck = true;
        for(int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition tempPosition = new ChessPosition(row, col);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if (tempPiece != null && tempPiece.getPieceType() == ChessPiece.PieceType.KING && tempPiece.getTeamColor() == teamColor) {
                    kingPosition = tempPosition;
                    Collection<ChessMove> kingPossibleMoves = new KingMovesCalculaor().pieceMoves(board, kingPosition, teamColor);
                    for (ChessMove move : kingPossibleMoves) {
                        makeMove(move);
                        if (!isInCheck(teamColor)) inCheck = false;
                        board = (ChessBoard) tempBoard.clone();
                    }
                }
            }
        }
        return inCheck;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     * call valid moves and if empty then the game is over
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
