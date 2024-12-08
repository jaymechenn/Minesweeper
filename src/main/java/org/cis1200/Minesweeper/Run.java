package org.cis1200.Minesweeper;

import org.cis1200.Minesweeper.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Run implements Runnable {
    public void run() {
        // top level frame
        final JFrame frame = new JFrame("Minesweeper");
        frame.setLocation(300, 300);
        // rules pop up
        final JFrame rules = new JFrame("Welcome to Minesweeper!");
        rules.setLocation(300, 100);
        JTextArea rulesArea = new JTextArea();
        rulesArea.setEditable(false);
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);
        rulesArea.setText("  Hey there, welcome to Minesweeper!\n" +
                "   - Left click a tile to uncover it\n" +
                "   - Right click to flag a tile that you think is a mine\n" +
                "   - Uncovered tiles will display how many adjacent tiles are mines" +
                "   - Try to uncover all non-mine tiles\n" +
                "   You can reset or undo anytime. Good luck! : )");
        JScrollPane rulesAreaScrollPane = new JScrollPane(rulesArea);
        rules.add(rulesAreaScrollPane);
        rules.setSize(500, 150);
        rules.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rules.setVisible(true);
        // display status
        final JPanel statusPanel = new JPanel();
        frame.add(statusPanel, BorderLayout.SOUTH);
        final JLabel statusLabel = new JLabel("one sec...");
        statusPanel.add(statusLabel);
        statusPanel.setBackground(Color.PINK);
        // game board
        final View board = new View(statusLabel);
        frame.add(board, BorderLayout.CENTER);

        // control panel
        final JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.NORTH);
        controlPanel.setBackground(Color.PINK);
        // reset button
        final JButton reset = new JButton("Again!");
        reset.addActionListener(e -> board.reset());
        controlPanel.add(reset);
        // undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        controlPanel.add(undo);
        // display frame
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // begin game!
        board.reset();
    }
}
