package chess;

import java.util.ArrayList;
import java.util.Collection;

public class ChessMoveRules {

    private Collection<ChessMove> chessMoveCollection = new ArrayList<ChessMove>();
    public ChessMoveRules () {

    }

    Collection<ChessMove> kingMoveRules(ChessPosition position) {
        return chessMoveCollection;
    }

    Collection<ChessMove> queenMoveRules(ChessPosition position) {
        return chessMoveCollection;
    }
    Collection<ChessMove> bishopMoveRules(ChessPosition position) {
        int[] rowDirections = { 1, 1, -1, -1 };
        int[] colDirections = { -1, 1, -1, 1 };

        for (int i = 0; i < 4; i++) {
            int tempRow = position.getRow();
            int tempColumn = position.getColumn();

            while (tempRow + rowDirections[i] >= 1 && tempRow + rowDirections[i] <= 8 &&
                    tempColumn + colDirections[i] >= 1 && tempColumn + colDirections[i] <= 8) {
                tempRow += rowDirections[i];
                tempColumn += colDirections[i];

                ChessPosition endPosition = new ChessPosition(tempRow, tempColumn);
                chessMoveCollection.add(new ChessMove(position, endPosition, null));
            }
        }
            return chessMoveCollection;
    }
    Collection<ChessMove> knightMoveRules(ChessPosition position) {
        return chessMoveCollection;
    }
    Collection<ChessMove> rookMoveRules(ChessPosition position) {
        return chessMoveCollection;
    }
    Collection<ChessMove> pawnMoveRules(ChessPosition position) {
        return chessMoveCollection;
    }
}
