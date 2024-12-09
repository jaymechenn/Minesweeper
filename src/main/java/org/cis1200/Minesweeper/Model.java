package org.cis1200.Minesweeper;

import java.util.Random;
import java.util.Stack;

public class Model {

    /**
     Board states:
     >0: number of mines (visible)
     -1: mine
     -2: covered (empty)
     -3: flagged (visible)
     */
    private int[][] board; // internal state
    private int[][] visibleBoard; // player's view
    /**
     * other variables
     */
    private int numMines; // number of mines
    private int size; // size of board
    private boolean gameActive; // tracks whether game has ended
    private Stack<int[][]> boardStack; // tracks previous boards for undo
    private boolean firstTurn; // tracks whether first turn to make sure first tile empty

    /**
     * constructor: start game with empty board
     */
    public Model() { reset(); }

    /**
     * run when tile is clicked
     * recursively searches for and uncovers adjacent tiles with 0 adjacent mines
     */
    public void playTurn(int row, int col, boolean newBoard) {
        // validate coordinates and game state
        if (!gameActive || row < 0 || row >= size || col < 0 || col >= size) {
            return; // end early if game inactive or coordinates out of bounds
        }
        // create new board
        if (firstTurn) {
            generateMines(row, col);
            populateBoard();
            firstTurn = false;
        }
        handleTile(row, col, newBoard);
        // user won -> end game
        if (checkWinner()) {
            gameActive = false;
        }
    }

    /**
     * reset board for a new game, all tiles start covered
     */
    public void reset() {
        // set variables
        numMines = 10;
        size = 10;
        gameActive = true;
        boardStack = new Stack<>();
        firstTurn = true;
        // create boards
        board = new int[size][size];
        visibleBoard = new int[size][size];
        // set state of all tiles to covered
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                visibleBoard[row][col] = -2;
            }
        }
    }
    /**
     * handle the tile clicked
     */
    public void handleTile(int row, int col, boolean newBoard) {
        // check state of tile that was clicked
        int tile = board[row][col];
        int visibleTile = visibleBoard[row][col];
        // tile already revealed -> do nothing
        if (visibleTile == tile) {
            return;
        }
        // tile is flagged -> remove flag
        else if (visibleTile == -3) {
            visibleBoard[row][col] = -2;
            return;
        }
        // add new board to stack
        if (newBoard) {
            saveBoardToStack();
        }
        // tile is covered -> reveal tile
        visibleBoard[row][col] = tile;
        // tile is a mine -> end game
        if (tile == -1) {
            gameActive = false;
        }
        // tile has no adjacent mines -> find and uncover all such adjacent tiles
        if (tile == 0) {
            for (int r = row - 1; r <= row + 1; r++) {
                for (int c = col - 1; c <= col + 1; c++) {
                    playTurn(r, c, false);
                }
            }
        }
    }

    /**
     * generate coordinates for mines, avoids first clicked tile
     */
    public void generateMines(int clickedRow, int clickedCol) {
        Random random = new Random();
        int[] mineCoords = new int[numMines];
        // generate 10 coordinates for mines
        int i = 0;
        while (i < numMines) {
            int mineRow = random.nextInt(size);
            int mineCol = random.nextInt(size);
            if (mineIsValid(mineRow, mineCol, clickedRow, clickedCol, mineCoords)) {
                mineCoords[i] = 10*mineRow + mineCol;
                board[mineRow][mineCol] = -1;
                i ++;
            }
        }
    }
    /**
     * helper function to check that mine coordinate is valid
     * must be a new coordinate
     * must not be the clicked coordinate
     */
    public boolean mineIsValid(int mineRow, int mineCol, int clickedRow, int clickedCol, int[] mineCoords) {
        // store coordinates with row in 10s place column in 1s places
        int mineCoord = 10*mineRow + mineCol;
        int clickedCoord = 10*clickedRow + clickedCol;
        int[] neighbors = { clickedCoord-10-1, clickedCoord-10, clickedCoord-10+1,
                clickedCoord-1, clickedCoord+1,
                clickedCoord+10-1, clickedCoord+10, clickedCoord+10+1 };
        // mine coordinate is clicked coordinate -> invalid
        if (mineCoord == clickedCoord) {
            return false;
        }
        // mine coordinate already has mine -> invalid
        for (int c : mineCoords) {
            if (c == mineCoord) {
                return false;
            }
        }
        // mine coordinate is adjacent to clicked coordinate -> invalid
        for (int n : neighbors) {
            if (n == mineCoord) {
                return false;
            }
        }
        return true;
    }

    /**
     *  set up board with mines, all covered
     */
    public void populateBoard() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // if not a mine, count surrounding mines
                if (!(board[row][col] == -1)) {
                    board[row][col] = countSurroundingMines(row, col);
                }
            }
        }
    }

    /**
     * count surrounding mines to determine number to be displayed
     */
    public int countSurroundingMines(int row, int col) {
        int count = 0;
        for (int r = row - 1; r <= row + 1; r++) {
            for (int c = col - 1; c <= col + 1; c++) {
                // validate coordinates, if mine found, update count
                if (!(r < 0 || r >= size || c < 0 || c >= size) && board[r][c] == -1) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * store current visible board to allow for undo function
     */
    public void saveBoardToStack() {
        int[][] copy = new int[size][size];
        for (int i = 0; i < 10; i++) {
            System.arraycopy(visibleBoard[i], 0, copy[i], 0, 10);
        }
        boardStack.push(copy);
    }

    /**
     * undoes last move by popping last board saved on stack
     */
    public void undo() {
        if (!boardStack.empty()) {
            visibleBoard = boardStack.pop();
            if (!gameActive) {
                gameActive = true;
            }
        }
    }

    /**
     * flag any covered element (-2 -> -3)
     */
    public void playFlag(int row, int col) {
        // validate coordinates, check that game is active
        if (!gameActive || row < 0 || row >= size || col < 0 || col >= size) {
            return;
        }
        // covered tile (-2) -> flag (-3), save old board
        if (visibleBoard[row][col] == -2) {
            saveBoardToStack();
            visibleBoard[row][col] = -3;
        }
    }

    /**
     * check if user has won by satisfying conditions:
     * no mine has been revealed
     * all remaining tiles are either covered or flagged
     */
    public boolean checkWinner() {
        int visible;
        int hidden;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                visible = visibleBoard[row][col];
                hidden = board[row][col];
                if ((hidden != -1) && (visible == -2 || visible == -3)) {
                    return false;
                }
            }
        }
        // System.out.println("you win!");
        return true;
    }

    /**
     * run game, does not depend on any other file
     */
    public static void main(String[] args) {
        // start new game
        Model model = new Model();
        // generate mines
        Random random = new Random();
        int clickedRow = random.nextInt(10);
        System.out.print(clickedRow + ", ");
        int clickedCol = random.nextInt(10);
        System.out.println(clickedCol);
        model.playTurn(clickedRow, clickedCol, true);
        // print coordinate clicked
        System.out.println("you clicked: (" + clickedRow + ", " + clickedCol + ")");
        // print board
        for (int[] row : model.board) {
            for (int tile : row) {
                if (tile == -1) { System.out.print("X "); }
                else if (tile == 0) { System.out.print("- "); }
                else { System.out.print(tile + " "); }
            }
            System.out.println();
        }
    }

    /**
     * getter methods for testing model
     */
    public int getTile(int row, int col) { return visibleBoard[row][col]; }
    public boolean getActive() { return gameActive; }
    public int getStackSize() { return boardStack.size(); }
    public int[][] getBoard() {
        int[][] copy = new int[size][size];
        System.arraycopy(board, 0, copy, 0, size);
        return copy;
    }
    /**
     * setter methods for testing
     */
    public void setFirstTurn(boolean firstTurn) { this.firstTurn = firstTurn; }
    public void setBoard(int[][] board) { this.board = board; }


}
