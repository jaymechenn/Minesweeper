package org.cis1200.Minesweeper;

import java.util.Stack;

public class Board {
    private int[][] tiles;
    private int size;

    /**
     * create board, represented by 2D Array of ints
     */
    public Board(int size) {
        this.size = size;
        tiles = new int[size][size];
        reset();
    }

    /**
     * getters & setters
     */
    public void set(int row, int col, int state) {
        tiles[row][col] = state;
    }
    public void setBoard (int[][] board) {
        tiles = board;
    }
    public int get(int row, int col) {
        return tiles[row][col];
    }
    public Board getCopy() {
        Board copy = new Board(size);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                copy.set(i, j, tiles[i][j]);
            }
        }
        return copy;
    }

    /**
     * reset board, all states become covered
     */
    public void reset() {
        // set variables
        size = 10;
        // set state of all tiles to covered
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                tiles[row][col] = -2;
            }
        }
    }

    /**
     * print board for testing
     */
    public void print() {
        for (int[] row : tiles) {
            for (int tile : row) {
                if (tile == -1) { System.out.print("X "); }
                else if (tile == 0) { System.out.print("- "); }
                else { System.out.print(tile + " "); }
            }
            System.out.println();
        }
    }

    /**
     *  set up board with mines, all covered
     */
    public void populateBoard() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // if not a mine, count surrounding mines
                if (!(tiles[row][col] == -1)) {
                    tiles[row][col] = countSurroundingMines(row, col);
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
                if (!(r < 0 || r >= size || c < 0 || c >= size) && tiles[row][col] == -1) {
                    count++;
                }
            }
        }
        return count;
    }


}
