package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import static ui.EscapeSequences.*;

public class ChessBoardUI {
    public static void main(String[] args) {
        drawChessBoard();
    }

    public static void drawChessBoard() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
    }
}

//How to get user input
// Scanner scanner = new Scanner(System.in);
// String line = scanner.nextLine();