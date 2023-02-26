
// Create a tic-tac-toe class that will enable you to write a program to play tic-tac-toe.
// The class contains a private 3 by 3 two-dimensional array.
// Use an enumeration to represent the value in each cell of the array.
// The enumeration’s constants should be named X, O, & EMPTY.
// The constructor should initialize the board elements to EMPTY.
// Allow two players. Whenever the first player moves, place an X in the specified square,
// and place an O for 2nd player moves.
// Each move must be to an empty square.
// After each move, determine whether the game has been won and whether it’s a draw.

package chapter7;

public class XandO {

    private static enum Status {
        X("X"),
        O("O"),
        EMPTY(" ");

        private String value;

        private Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

    };

    private static enum GameState {
        X_WON("X won!"),
        O_WON("O won!"),
        DRAW("Draw"),
        IN_PROGRESS("");

        private String value;

        private GameState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

    };

    // Constructor is unecessary, starting values will never change
    private Status[][] board = new Status[][] {
            { Status.EMPTY, Status.EMPTY, Status.EMPTY },
            { Status.EMPTY, Status.EMPTY, Status.EMPTY },
            { Status.EMPTY, Status.EMPTY, Status.EMPTY }
    };
    private boolean[] unwinnable_lines = new boolean[] { false, false, false, false, false, false, false, false };
    private int turn_count = 0;
    private boolean finished = false;

    public void move(int x, int y) {
        if (finished) {
            throw new IllegalStateException("Game is already finished");
        }
        if (board[x][y] == Status.EMPTY) {
            if (turn_count % 2 == 0) {
                board[x][y] = Status.X;
            } else {
                board[x][y] = Status.O;
            }
            turn_count++;
        } else {
            throw new IllegalArgumentException("Cannot move to a non-empty square");
        }
        printBoard();
        gameDone();
    }

    private void gameDone() {
        GameState state = determineGameStatus();
        switch (state) {
            case X_WON:
                this.finished = true;
                System.out.println(state);
                break;
            case O_WON:
                this.finished = true;
                System.out.println(state);
                break;
            case DRAW:
                this.finished = true;
                System.out.println(state);
                break;
            case IN_PROGRESS:
                System.out.println(String.format("%s's turn", (turn_count % 2 == 0 ? "X" : "O")));
                break;
        }
    }

    private GameState determineGameStatus() {
        // Check for draw by no valid moves
        if (turn_count == 9) {
            return GameState.DRAW;
        }

        // Check if all lines are unwinnable
        for (int i = 0; i < 8; i++) {
            if (!unwinnable_lines[i]) {
                break;
            }
            if (i >= 7) {
                return GameState.DRAW;
            }
        }

        // Check rows
        for (int row = 0; row < 3; row++) {
            // Check if row has already been determined to be unwinnable
            if (unwinnable_lines[row]) {
                continue;
            } else {
                // Here we are primarily checking if the row is unwinnable
                // If a row contains both X and O, it is unwinnable
                // and we break early
                int x_count = 0;
                int o_count = 0;
                rowchk: for (int column = 0; column < 3; column++) {
                    switch (board[row][column]) {
                        case X:
                            x_count++;
                            break;
                        case O:
                            o_count++;
                            break;
                        case EMPTY:
                            break rowchk;
                    }
                    if (x_count > 0 && o_count > 0) {
                        unwinnable_lines[row] = true;
                        break rowchk;
                    }
                }
                if (x_count == 3) {
                    return GameState.X_WON;
                } else if (o_count == 3) {
                    return GameState.O_WON;
                }
            }
        }

        // Check columns
        for (int column = 0; column < 3; column++) {
            // Check if column has already been determined to be unwinnable
            if (unwinnable_lines[column + 3]) {
                continue;
            } else {
                // Here we are primarily checking if the column is unwinnable
                // If a column contains both X and O, it is unwinnable
                // and we break early
                int x_count = 0;
                int o_count = 0;
                colchk: for (int row = 0; row < 3; row++) {
                    switch (board[row][column]) {
                        case X:
                            x_count++;
                            break;
                        case O:
                            o_count++;
                            break;
                        case EMPTY:
                            break colchk;
                    }
                    if (x_count > 0 && o_count > 0) {
                        unwinnable_lines[column + 3] = true;
                        break colchk;
                    }
                }
                if (x_count == 3) {
                    return GameState.X_WON;
                } else if (o_count == 3) {
                    return GameState.O_WON;
                }
            }
        }

        // Check diagonals
        if (!unwinnable_lines[6]) {
            int x_count = 0;
            int o_count = 0;
            for (int i = 0; i < 3; i++) {
                switch (board[i][i]) {
                    case X:
                        x_count++;
                        break;
                    case O:
                        o_count++;
                        break;
                    case EMPTY:
                        break;
                }
                if (x_count > 0 && o_count > 0) {
                    unwinnable_lines[6] = true;
                    break;
                }
            }
            if (x_count == 3) {
                return GameState.X_WON;
            } else if (o_count == 3) {
                return GameState.O_WON;
            }
        }
        if (!unwinnable_lines[7]) {
            int x_count = 0;
            int o_count = 0;
            for (int i = 0; i < 3; i++) {
                switch (board[i][2 - i]) {
                    case X:
                        x_count++;
                        break;
                    case O:
                        o_count++;
                        break;
                    case EMPTY:
                        break;
                }
                if (x_count > 0 && o_count > 0) {
                    unwinnable_lines[7] = true;
                    break;
                }
            }
            if (x_count == 3) {
                return GameState.X_WON;
            } else if (o_count == 3) {
                return GameState.O_WON;
            }
        }

        return GameState.IN_PROGRESS;
    }

    public void printBoard() {
        System.out.println(
                String.format(
                        "┌─┬─┬─┐\n│%s│%s│%s│\n├─┼─┼─┤\n│%s│%s│%s│\n├─┼─┼─┤\n│%s│%s│%s│\n└─┴─┴─┘",
                        board[0][0].value, board[0][1].value, board[0][2].value,
                        board[1][0].value, board[1][1].value, board[1][2].value,
                        board[2][0].value, board[2][1].value, board[2][2].value));
    }

    public static void main(String[] args) {
        XandO game = new XandO();
        game.printBoard();
        game.move(0, 0);
        game.move(0, 1);
        game.move(1, 1);
        game.move(0, 2);
        game.move(2, 2);
    }
}
