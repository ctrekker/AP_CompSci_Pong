import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameSelector extends JFrame {
    public GameSelector() {
        setTitle("Game selection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pane=new JPanel(new BorderLayout());
        JButton single=new JButton("Single Player");
        JButton original=new JButton("One vs. One");
        JButton block=new JButton("Block Breaker");
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

        pane.add(single, BorderLayout.WEST);
        pane.add(original, BorderLayout.CENTER);
        pane.add(block, BorderLayout.EAST);
        pane.add(instructions, BorderLayout.NORTH);

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

        add(pane);
        pack();

        setVisible(true);
    }
}
