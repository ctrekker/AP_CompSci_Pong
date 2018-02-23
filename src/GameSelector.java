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

        pane.add(single, BorderLayout.WEST);
        pane.add(original, BorderLayout.CENTER);
        pane.add(block, BorderLayout.EAST);

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
