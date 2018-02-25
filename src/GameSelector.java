//Name - Connor Burns
//Date - 2/26/18
//Class - AP Comp Sci Period 7
//Lab  - Pong
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameSelector extends JFrame {
    public GameSelector() {
        // Set window parameters
        // NOTE: No setSize due to packing at end
        setTitle("Game selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Make a content pane w/ border layout
        JPanel pane=new JPanel(new BorderLayout());
        // Make 3 buttons, one for each game
        JButton single=new JButton("Single Player");
        JButton original=new JButton("One vs. One");
        JButton block=new JButton("Block Breaker");
        // Make a label that has all the instructions
        // NOTE: This is written in javax.swing supported HTML
        JLabel instructions=new JLabel("<html>" +
                "<b>General:</b><br>" +
                "Click on a button to open the game!<br><br>" +
                "<b>Single Player</b><br>" +
                " * Use the left and right arrow keys to control the player<br>" +
                " * Use the up arrow to speed the ball up<br><br>" +
                "<b>One vs. One</b><br>" +
                " * Use W/S and up/down arrow for controls<br><br>" +
                "<b>Block Breaker</b><br>" +
                " * Use the left and right arrow keys to control the player<br>" +
                " * Use the up arrow to speed the ball up<br>" +
                " * Use the down arrow to skip to the next level.<br>" +
                "       This is valid because scoring is based on block breaks, not level" +
                "</html>");

        // Add each component to their appropriate locations on the border pane
        pane.add(single, BorderLayout.WEST);
        pane.add(original, BorderLayout.CENTER);
        pane.add(block, BorderLayout.EAST);
        pane.add(instructions, BorderLayout.NORTH);

        // A bunch of action listeners which initialize a new game of each type, depending on which button was pressed
        single.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new pong.single.Game();
            }
        });
        original.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new pong.original.Game();
            }
        });
        block.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new pong.block.Game();
            }
        });

        // Add the pane to the contentFrame
        add(pane);
        // Pack the display
        // NOTE: Pack compresses the window size to its minimum width/height based on component constraints
        pack();

        // Make the frame visible
        setVisible(true);
    }
}
