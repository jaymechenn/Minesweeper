package org.cis1200.Minesweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class View extends JPanel {
    // variables to make game and track status (win/loss)
    private Model grid;
    private JLabel status;
    boolean CtrlPressed = false;
    // constants
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    private int size = 10;

    /**
     * draw game board
     */
    public View(JLabel status) {
        // draw border around board, focusable
        setBorder(BorderFactory.createLineBorder(Color.WHITE));
        setFocusable(true);
        grid = new Model();
        this.status = status;
        /**
         * update model upon mouseclick
         */
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();
                int row = p.y / 50;
                int col = p.x / 50;
                // right click -> flag
                if (e.getButton() == MouseEvent.BUTTON3 || (CtrlPressed && e.getButton() == MouseEvent.BUTTON1)) {
                    grid.playFlag(row, col);
                }
                // left click -> uncover
                else {
                    grid.playTurn(row, col, true);
                }
                // update states and display
                updateStatus();
                repaint();
            }
        });
        /**
         * track whether contol is pressed (allow for control-click for right click
         */
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    CtrlPressed = true;
                }
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    CtrlPressed = false;
                }
            }
        });
    }

    /**
     * reset game, reset status
     */
    public void reset() {
        grid.reset();
        status.setText("Mines: 10");
        repaint();
        requestFocusInWindow();
    }

    /**
     * undo button
     */
    public void undo() {
        grid.undo();
        repaint();
        updateStatus();
        requestFocusInWindow();
    }

    /**
     * update status of game and JLabel based on current status of game
     */
    public void updateStatus() {
        // user wins -> display win message
        if(grid.checkWinner()) {
            status.setText("You win! Play again :D");
        }
        // user loses -> display lose message
        else if (!grid.getActive()) {
            status.setText("Uh oh! That's a bunny. No worries, just restart or undo.");
        }
        // game not over -> display number of mines
        else {
            status.setText("⋆ ˚｡⋆୨୧˚ Bunnies: 10 ˚୨୧⋆｡˚ ⋆");
        }
    }

    /**
     * get size of board for testing
     */
    public Dimension getPreferredSize() { return new Dimension(WIDTH, HEIGHT); }

    /**
     * draw game board
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // random num generator to generate cute symbols
        Random random = new Random();
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 30));
        // draw grid
        for (int i = 1; i <= size; i ++) {
            g.setColor(Color.WHITE);
            g.drawLine(i*50, 0, i*50, HEIGHT);
            g.drawLine(0, i*50, WIDTH, i*50);
        }
        // draw numbers
        for (int row = 0; row < size; row ++) {
            for (int col = 0; col < size; col ++) {
                int state = grid.getTile(row, col);
                // empty (no adjacent mines)
                if (state == 0) {
                    g.setColor(Color.PINK);
                    g.fillRect(col*50+1, row*50+1, 48, 48);
                    g.setColor(Color.WHITE);
                    int teehee = random.nextInt(3);
                    if (teehee == 0) {
                        g.drawString("❀", col*50+10, row*50+35);
                    } else if (teehee == 1) {
                        g.drawString("❁", col*50+10, row*50+35);
                    } else if (teehee == 2) {
                        g.drawString("✿", col*50+10, row*50+35);
                    }
                }
                // number
                else if (state > 0) {
                    g.setColor(Color.PINK);
                    g.fillRect(col*50+1, row*50+1, 48, 48);
                    g.setColor(Color.WHITE);
                    g.drawString(Integer.toString(state), col*50+17, row*50+35);
                }
                // flag
                else if (state == -3) {
                    g.setColor(Color.PINK);
                    g.drawString("❤", col*50+10, row*50+35);
                }
                // mine
                else if (state == -1) {
                    g.setColor(Color.ORANGE);
                    g.fillRect(col*50+1, row*50+1, 48, 48);
                    g.setColor(Color.WHITE);
                    g.drawString("\uD83D\uDC30", col*50+10, row*50+35);
                }
            }
        }
    }
}
