package org.cis1200.Gardensweeper;

import javax.swing.*;
import java.awt.*;

public class Run implements Runnable {
    public void run() {
        // top level frame
        final JFrame frame = new JFrame("ₓ˚. ୭ ˚○◦˚.˚◦○˚ ୧ .˚ₓ ");
        frame.setLocation(500, 300);
        Font font = new Font("Comic Sans MS", Font.PLAIN, 15);
        // rules pop up
        final JFrame rules = new JFrame("*:･ﾟ✧*:･ﾟ Minesweeper *:･ﾟ✧*:･ﾟ");
        rules.setLocation(500, 60);
        JTextArea rulesArea = new JTextArea();
        rulesArea.setEditable(false);
        rulesArea.setLineWrap(true);
        rulesArea.setWrapStyleWord(true);
        rulesArea.setFont(font);
        rulesArea.setText(
                "  Hey there! Could you help me plant my garden? \n" +
                        "     ❀  Left click a field to plant a flower (reveal)\n" +
                        "     ❁  Right click any fields that are bunnies (flag)\n" +
                        "     ✿  Uncovered fields show how many bunnies live nearby\n" +
                        "     ❀  Help me plant my garden without bothering the bunnies\n" +
                        "   Thank you so much! ˚୨୧⋆｡˚ ⋆"
        );
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
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        statusPanel.setFont(font);
        // game board
        final View board = new View(statusLabel);
        frame.add(board, BorderLayout.CENTER);

        // control panel
        final JPanel controlPanel = new JPanel();
        frame.add(controlPanel, BorderLayout.NORTH);
        controlPanel.setBackground(Color.PINK);
        // reset button
        final JButton reset = new JButton("✧*̥˚  Again!  *̥˚✧");
        reset.setFont(font);
        reset.addActionListener(e -> board.reset());
        controlPanel.add(reset);
        // undo button
        final JButton undo = new JButton("✧*̥˚  Undo  *̥˚✧");
        undo.setFont(font);
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
