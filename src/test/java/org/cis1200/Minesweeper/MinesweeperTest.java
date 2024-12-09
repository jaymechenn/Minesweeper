package org.cis1200.Minesweeper;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class MinesweeperTest {
    private Model model;

    @BeforeEach
    public void setUp() {
        /*
         Custom Board:
         -1  0  0  0  0  0  0  0  0 -1
          0 -1  0  0  0  0  0  0 -1  0
          0  0 -1  0  0  0  0 -1  0  0
          0  0  0 -1  0  0 -1  0  0  0
          0  0  0  0 -1 -1  0  0  0  0
          0  0  0  0 -1 -1  0  0  0  0
          0  0  0 -1  0  0 -1  0  0  0
          0  0 -1  0  0  0  0 -1  0  0
          0 -1  0  0  0  0  0  0 -1  0
         -1  0  0  0  0  0  0  0  0 -1
         */
        int[][] testBoard = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (i == j || i == 9 - j) {
                    testBoard[i][j] = -1;
                }
            }
        }
        model = new Model();
        model.getBoard().setBoard(testBoard);
        model.getBoard().populateBoard();
        model.setFirstTurn(false);
    }

    @Test
    public void testPlayTurnRevealsAllEmptyPlots() {
        model.playTurn(0, 4, true);
        assertEquals(0, model.getTile(0, 3));
        assertEquals(0, model.getTile(0, 4));
        assertEquals(0, model.getTile(0, 5));
        assertEquals(0, model.getTile(0, 6));
        assertEquals(0, model.getTile(1, 4));
        assertEquals(0, model.getTile(1, 5));
        assertEquals(1, model.getStackSize());
        assertTrue(model.getActive());
    }

    @Test
    public void testPlayTurnRevealsSingleNumberedPlot() {
        model.playTurn(4, 3, true);
        assertEquals(3, model.getTile(4, 3));
        assertEquals(1, model.getStackSize());
        assertTrue(model.getActive());
    }

    @Test
    public void testPlayTurnRevealsMineAndEndsGame() {
        model.playTurn(4, 4, true);
        assertEquals(-1, model.getTile(4, 4));
        assertEquals(1, model.getStackSize());
        assertFalse(model.getActive());
        model.playTurn(4, 5, true);
        assertEquals(-2, model.getTile(4, 5));
    }

    @Test
    public void testPlayFlagAndPlayTurnRemovesFlag() {
        model.playFlag(0, 0);
        assertEquals(-3, model.getTile(0, 0));

        model.playTurn(0, 0, true);
        assertEquals(-2, model.getTile(0, 0));
        assertEquals(1, model.getStackSize());
        assertTrue(model.getActive());
    }

    @Test
    public void testUndoWhenGameActive() {
        model.playTurn(4, 3, true);
        assertEquals(3, model.getTile(4, 3));
        model.undo();
        assertEquals(-2, model.getTile(4, 3));
        assertEquals(0, model.getStackSize());
    }

    @Test
    public void testUndoWhenGameEnded() {
        model.playTurn(4, 4, true);
        assertEquals(-1, model.getTile(4, 4));
        assertFalse(model.getActive());
        model.undo();
        assertEquals(-2, model.getTile(4, 4));
        assertEquals(0, model.getStackSize());
        assertTrue(model.getActive());
    }

    @Test
    public void testReset() {
        model.playTurn(4, 4, true);

        model.reset();
        assertEquals(-2, model.getTile(4, 4));
        assertTrue(model.getActive());
        assertEquals(0, model.getStackSize());
    }

    @Test
    public void testGenerateMines() {
        model.reset();
        model.generateMines(1, 1);
        model.getBoard().populateBoard();
        Board board = model.getBoard();
        int mineCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (board.get(i,j) == -1) {
                    mineCount++;
                }
            }
        }
        assertEquals(10, mineCount); // Adjust based on your mine generation logic
        assertEquals(0, board.get(1,1));
    }

    public void testRevealEdgeCell() {
        model.playTurn(0, 9, true);
        assertEquals(-1, model.getTile(0, 9), "Edge cell with mine should reveal correctly.");
        assertFalse(model.getActive(), "Game should end after revealing a mine.");
    }

    @Test
    public void testRevealCornerCell() {
        model.playTurn(0, 0, true);
        assertEquals(-1, model.getTile(0, 0), "Corner cell with mine should reveal correctly.");
        assertFalse(model.getActive(), "Game should end after revealing a mine.");
    }

    @Test
    public void testFlaggingMultipleCells() {
        model.playFlag(0, 0);
        model.playFlag(1, 1);
        model.playFlag(9, 9);
        assertEquals(-3, model.getTile(0, 0), "Cell (0,0) should be flagged.");
        assertEquals(-3, model.getTile(1, 1), "Cell (1,1) should be flagged.");
        assertEquals(-3, model.getTile(9, 9), "Cell (9,9) should be flagged.");
    }

    @Test
    public void testRecursiveRevealStopsAtMines() {
        model.playTurn(3, 3, true);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (Math.abs(i - 3) > 1 || Math.abs(j - 3) > 1) {
                    assertEquals(-2, model.getTile(i, j), "Distant cells should remain covered.");
                }
            }
        }
    }

    @Test
    public void testGameDoesNotEndOnFlaggedMine() {
        model.playFlag(0, 0); // Flag a mine
        model.playTurn(4, 4, true); // Click a different mine
        assertEquals(-1, model.getTile(4, 4), "Mine should be revealed.");
        assertFalse(model.getActive(), "Game should end after revealing a mine.");
    }

    @Test
    public void unflagCell() {
        model.playFlag(0, 0); // Flag a mine
        model.playTurn(0, 0, true); // Attempt to reveal flagged cell
        assertEquals(-2, model.getTile(0, 0), "Flagged cells should not be revealed.");
    }

    @Test
    public void testUndoMultipleSteps() {
        model.playTurn(4, 3, true);
        model.playTurn(3, 3, true);
        model.playTurn(2, 2, true);
        model.undo();
        assertEquals(-2, model.getTile(2, 2), "Last move should be undone.");
        model.undo();
        assertEquals(-2, model.getTile(3, 3), "Second-to-last move should be undone.");
        model.undo();
        assertEquals(-2, model.getTile(4, 3), "First move should be undone.");
        assertEquals(0, model.getStackSize(), "Undo stack should be empty after all undos.");
    }

    @Test
    public void testGameWinByRevealingAllNonMines() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (model.getBoard().get(i,j) != -1) {
                    model.playTurn(i, j, true);
                }
            }
        }
        assertTrue(model.checkWinner(), "Game should be won when all non-mine cells are revealed.");
        assertFalse(model.getActive(), "Game should end after winning.");
    }

    @Test
    public void testResetAfterWin() {
        testGameWinByRevealingAllNonMines();
        model.reset();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(-2, model.getTile(i, j), "All cells should be covered after reset.");
            }
        }
        assertTrue(model.getActive(), "Game should be active after reset.");
        assertEquals(0, model.getStackSize(), "Undo stack should be empty after reset.");
    }

    @Test
    public void testResetAfterLoss() {
        model.playTurn(0, 0, true); // Click a mine
        assertFalse(model.getActive(), "Game should end after revealing a mine.");
        model.reset();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(-2, model.getTile(i, j), "All cells should be covered after reset.");
            }
        }
        assertTrue(model.getActive(), "Game should be active after reset.");
        assertEquals(0, model.getStackSize(), "Undo stack should be empty after reset.");
    }

    @Test
    public void testMineCountConsistencyAfterReset() {
        model.reset();
        model.generateMines(5, 5);
        model.getBoard().populateBoard();
        int mineCount = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (model.getBoard().get(i,j) == -1) {
                    mineCount++;
                }
            }
        }
        assertEquals(10, mineCount, "Mine count should be consistent after reset and regeneration.");
    }

}
