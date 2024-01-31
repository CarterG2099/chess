package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
        teamTurn = team;
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
        ChessPiece pieceToCheck = board.getPiece(startPosition);
        Collection<ChessMove> validMoves = pieceToCheck.pieceMoves(board, startPosition);
        Iterator<ChessMove> iterator = validMoves.iterator();
        while (iterator.hasNext()) {
            ChessMove move = iterator.next();
            try {
                makeMove(move);
            } catch (InvalidTurnException ex) {
                return validMoves;
            } catch (InvalidMoveException ex) {
                iterator.remove(); // Safely remove the element
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException, InvalidTurnException{
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
        //Clone the board in case the move is invalid
        ChessBoard tempBoard = (ChessBoard) board.clone();
        board.removePiece(move.getStartPosition());
        if(move.getPromotionPiece() != null) board.addPiece(move.getEndPosition(), new ChessPiece(pieceToMove.getTeamColor(), move.getPromotionPiece()));
        else board.addPiece(move.getEndPosition(), pieceToMove);
        InvalidTurnException wrongTeam = new InvalidTurnException();
        //Make sure it is the right teams turn
        if(pieceToMove.getTeamColor() != getTeamTurn())
        {
            board = tempBoard;
            throw wrongTeam;
        }
        //Check to make sure move is even part of their moves
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        switch (pieceToMove.getPieceType()) {
            case KING:
                possibleMoves.addAll(new KingMovesCalculaor().pieceMoves(tempBoard, move.getStartPosition(), pieceToMove.getTeamColor()));
                break;
            case QUEEN:
                possibleMoves.addAll(new QueenMovesCalculator().pieceMoves(tempBoard, move.getStartPosition(), pieceToMove.getTeamColor()));
                break;
            case KNIGHT:
                possibleMoves.addAll(new KnightMovesCalculator().pieceMoves(tempBoard, move.getStartPosition(), pieceToMove.getTeamColor()));
                break;
            case BISHOP:
                possibleMoves.addAll(new BishopMovesCalculator().pieceMoves(tempBoard, move.getStartPosition(), pieceToMove.getTeamColor()));
                break;
            case ROOK:
                possibleMoves.addAll(new RookMovesCalculator().pieceMoves(tempBoard, move.getStartPosition(), pieceToMove.getTeamColor()));
                break;
            case PAWN:
                possibleMoves.addAll(new PawnMovesCalculaor().pieceMoves(tempBoard, move.getStartPosition(), pieceToMove.getTeamColor()));
                break;
        }
        InvalidMoveException invalidMove = new InvalidMoveException();
        if(!possibleMoves.contains(move) || isInCheck(pieceToMove.getTeamColor())) {
            board = tempBoard;
            throw invalidMove;
        }
        //After making the move, change the teams turn
        if(getTeamTurn() == TeamColor.WHITE) setTeamTurn(TeamColor.BLACK);
        else setTeamTurn(TeamColor.WHITE);
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
            if(kingPosition != null && endPosition.getRow() == kingPosition.getRow() && endPosition.getColumn() == kingPosition.getColumn()){
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
        boolean inCheck = true;
        if(isInCheck(teamColor)) {
            ChessBoard tempBoard = (ChessBoard) board.clone();
            ChessPosition kingPosition = null;
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition tempPosition = new ChessPosition(row, col);
                    ChessPiece tempPiece = board.getPiece(tempPosition);
                    if (tempPiece != null && tempPiece.getPieceType() == ChessPiece.PieceType.KING && tempPiece.getTeamColor() == teamColor) {
                        kingPosition = tempPosition;
                        Collection<ChessMove> kingPossibleMoves = new KingMovesCalculaor().pieceMoves(board, kingPosition, teamColor);
                        for (ChessMove move : kingPossibleMoves) {
                            try {
                                makeMove(move);
                            } catch (InvalidMoveException ex){
                                inCheck = true;
                            }
                        }
                    }
                }
            }
        }
        else inCheck = false;
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
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition tempPosition = new ChessPosition(row, col);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if (tempPiece != null && teamColor == tempPiece.getTeamColor()) validMoves.addAll(validMoves(tempPosition));
            }
        }
        if(validMoves.isEmpty()) return true;
        return false;
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
