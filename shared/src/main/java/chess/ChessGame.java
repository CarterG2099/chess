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
    private TeamColor teamTurn = TeamColor.WHITE;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

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
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece pieceToCheck = board.getPiece(startPosition);
        if (pieceToCheck == null) return null;

        Collection<ChessMove> validMoves = pieceToCheck.pieceMoves(board, startPosition);
        Iterator<ChessMove> iterator = validMoves.iterator();

        while (iterator.hasNext()) {
            ChessMove move = iterator.next();
            ChessBoard tempBoard = (ChessBoard) board.clone();
            board.removePiece(move.getStartPosition()); //Remove the piece
            if (move.getPromotionPiece() != null)
                board.addPiece(move.getEndPosition(), new ChessPiece(pieceToCheck.getTeamColor(), move.getPromotionPiece())); //Add the promotion piece
            else board.addPiece(move.getEndPosition(), pieceToCheck); //Add the piece
            if (isInCheck(pieceToCheck.getTeamColor()))
                iterator.remove(); // Use iterator to safely remove the element if its in check
            if (pieceToCheck.getPieceType() == ChessPiece.PieceType.KING && !castling(move)) {
                iterator.remove(); //Check if king move is castling and if valid
            }
            board = tempBoard;
        }
        return validMoves;
    }

    /**
     * If valid castling move, move the rook
     *
     * @param move the Kings move that the rook position is calculated from
     * @return boolean if castling is valid or not
     */
    private boolean castling(ChessMove move) {
        //If moving left
        if (move.getStartPosition().getColumn() - move.getEndPosition().getColumn() == 2) {
            ChessPosition rookPosition = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn() - 4);
            ChessPosition newRookPosition = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn() - 1);
            ChessGame.TeamColor color = board.getPiece(rookPosition).getTeamColor();
            if (!rookInDanger(newRookPosition, color)) {
                ChessPiece rookToMove = board.getPiece(rookPosition);
                board.removePiece(rookPosition);
                board.addPiece(newRookPosition, rookToMove);
                return true;
            }
            //Rook in danger
            return false;
        }
        //Moving right
        else if (move.getStartPosition().getColumn() - move.getEndPosition().getColumn() == -2) {
            ChessPosition rookPosition = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn() + 3);
            ChessPosition newRookPosition = new ChessPosition(move.getStartPosition().getRow(), move.getStartPosition().getColumn() + 1);
            ChessGame.TeamColor color = board.getPiece(rookPosition).getTeamColor();
            if (!rookInDanger(newRookPosition, color)) {
                ChessPiece rookToMove = board.getPiece(rookPosition);
                board.removePiece(rookPosition);
                board.addPiece(newRookPosition, rookToMove);
                return true;
            }
            //Rook in danger
            return false;
        }
        //Not a castling move
        else return true;
    }

    /**
     * Determines if rook is in danger in order to validate castling move
     *
     * @param rookPosition the end position of the rook in a castling move
     * @param teamColor    same color as the rook being checked
     * @return true if the rook is in danger for castling
     */
    public boolean rookInDanger(ChessPosition rookPosition, TeamColor teamColor) {
        Collection<ChessMove> opponentMoves = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition tempPosition = new ChessPosition(row, col);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if (tempPiece != null && tempPiece.getTeamColor() != teamColor) {
                    switch (tempPiece.getPieceType()) {
                        case KING:
                            opponentMoves.addAll(new KingMovesCalculator().pieceMoves(board, tempPosition, tempPiece.getTeamColor()));
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

        for (ChessMove move : opponentMoves) {
            ChessPosition endPosition = move.getEndPosition();
            if (endPosition.getRow() == rookPosition.getRow() && endPosition.getColumn() == rookPosition.getColumn())
                return true;
        }
        return false;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
        //Clone the board in case the move is invalid
        ChessBoard tempBoard = (ChessBoard) board.clone();

        //Check for castling move (have to adjust to fit the tests instead of using my own moves that are created
        if (pieceToMove.getPieceType() == ChessPiece.PieceType.KING && Math.abs(move.getEndPosition().getColumn() - move.getStartPosition().getColumn()) > 1) {
            //Check king is not in check
            if (!isInCheck(pieceToMove.getTeamColor()))
                castling(move);
        }

        //Move the original piece
        board.removePiece(move.getStartPosition());

        //Check for promotion add
        if (move.getPromotionPiece() != null)
            board.addPiece(move.getEndPosition(), new ChessPiece(pieceToMove.getTeamColor(), move.getPromotionPiece()));
        else board.addPiece(move.getEndPosition(), pieceToMove);

        InvalidMoveException ex = new InvalidMoveException();
        //Make sure it is the right teams turn
        if (pieceToMove != null && pieceToMove.getTeamColor() != getTeamTurn()) {
            board = tempBoard;
            throw ex;
        }
        //Check to make sure move is even part of their moves
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        if (pieceToMove != null) {
            switch (pieceToMove.getPieceType()) {
                case KING:
                    KingMovesCalculator kingMovesCalculator = new KingMovesCalculator();
                    if (!pieceToMove.hasPieceMoved())
                        kingMovesCalculator.castling(tempBoard, move.getStartPosition(), pieceToMove.getTeamColor());
                    possibleMoves.addAll(kingMovesCalculator.pieceMoves(tempBoard, move.getStartPosition(), pieceToMove.getTeamColor()));
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
                    RookMovesCalculator rookMovesCalculator = new RookMovesCalculator();
                    possibleMoves.addAll(rookMovesCalculator.pieceMoves(tempBoard, move.getStartPosition(), pieceToMove.getTeamColor()));
                    break;
                case PAWN:
                    possibleMoves.addAll(new PawnMovesCalculaor().pieceMoves(tempBoard, move.getStartPosition(), pieceToMove.getTeamColor()));
                    break;
            }
        }
        if (!possibleMoves.contains(move) || isInCheck(pieceToMove.getTeamColor())) {
            board = tempBoard;
            throw ex;
        }
        //After making the move, change the teams turn
        if (getTeamTurn() == TeamColor.WHITE) setTeamTurn(TeamColor.BLACK);
        else setTeamTurn(TeamColor.WHITE);
        //Mark the piece as having moved
        pieceToMove.pieceMoved();
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
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition tempPosition = new ChessPosition(row, col);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if (tempPiece != null && tempPiece.getPieceType() == ChessPiece.PieceType.KING && tempPiece.getTeamColor() == teamColor)
                    kingPosition = tempPosition;
                if (tempPiece != null && tempPiece.getTeamColor() != teamColor) {
                    switch (tempPiece.getPieceType()) {
                        case KING:
                            opponentMoves.addAll(new KingMovesCalculator().pieceMoves(board, tempPosition, tempPiece.getTeamColor()));
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

        for (ChessMove move : opponentMoves) {
            ChessPosition endPosition = move.getEndPosition();
            if (kingPosition != null && endPosition.getRow() == kingPosition.getRow() && endPosition.getColumn() == kingPosition.getColumn())
                return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     * First check if in check and then if any move still leaves King in check - use isincheck on every move if one move is not in check then return false - otherwise return true. Could clone the board and then make the potential move and call isincheck, repeat by cloning board and making move.
     * Cloning is simpler but the other option is to undo a move. Make a move, call isincheck and undo move if neccessary
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean inCheck = true;
        if (isInCheck(teamColor)) {
            ChessPosition kingPosition;
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition tempPosition = new ChessPosition(row, col);
                    ChessPiece tempPiece = board.getPiece(tempPosition);
                    if (tempPiece != null && tempPiece.getPieceType() == ChessPiece.PieceType.KING && tempPiece.getTeamColor() == teamColor) {
                        kingPosition = tempPosition;
                        Collection<ChessMove> kingPossibleMoves = new KingMovesCalculator().pieceMoves(board, kingPosition, teamColor);
                        for (ChessMove move : kingPossibleMoves) {
                            try {
                                makeMove(move);
                            } catch (InvalidMoveException ex) {
                                inCheck = true;
                            }
                        }
                    }
                }
            }
        } else inCheck = false;
        return inCheck;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     * call valid moves and if empty then the game is over
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        ChessGame.TeamColor original = getTeamTurn();
        setTeamTurn(teamColor);
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition tempPosition = new ChessPosition(row, col);
                ChessPiece tempPiece = board.getPiece(tempPosition);
                if (tempPiece != null && teamColor == tempPiece.getTeamColor())
                    validMoves.addAll(validMoves(tempPosition));
            }
        }
        setTeamTurn(original);
        if (validMoves.isEmpty()) return true;
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
