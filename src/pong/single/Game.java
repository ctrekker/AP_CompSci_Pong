package pong.single;

import pong.original.Ball;
import pong.original.Paddle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends JFrame {
    private Paddle player;
    private ArrayList<Ball> balls;
    private int paddleSpeed=6;
    private int ballMaxSpeed=6;
    private int defaultBallSpeed=2;
    private int ballSpeed=defaultBallSpeed;

    public Game() {
        balls=new ArrayList<>();

        // Set frame properties
        setTitle("Classic Pong");
        // Original game resolution is usually 640x400
        setSize(480, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create graphics instance (component-based)
        GameGraphics graphics = new GameGraphics();
        // Add it to the frame
        add(graphics);
        addKeyListener(new KeyListener() {
            // Detect key press (down only)
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    // Left Arrow
                    case 37:
                        player.setMovement(new Point(-1, 0));
                        break;
                    // Right Arrow
                    case 39:
                        player.setMovement(new Point(1, 0));
                        break;
                }
            }
            // Detect key press (up only)
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()) {
                    // Left Arrow
                    case 37:
                        player.setMovement(new Point(0, 0));
                        break;
                    // Right Arrow
                    case 39:
                        player.setMovement(new Point(0, 0));
                        break;
                }
            }
            // Detect general typing action (unused)
            public void keyTyped(KeyEvent e) {

            }
        });

        // Make the frame visible
        setVisible(true);
    }
    private class GameGraphics extends Component {
        private boolean firstTime=true;
        private boolean gameOver=false;
        private int gameOverCount=0;
        private int ballCount=0;
        private int ballMultiplier=1200;
        private int ballThreshold=0;
        private int ballMax=1;

        private int score=0;

        public GameGraphics() {
            Timer t=new Timer("animation");
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    repaint();
                }
            }, 0, (int)(1000/60.0));
        }
        public void paint(Graphics g) {
            Graphics2D g2=(Graphics2D)g;

            if(firstTime) {
                player=new Paddle(0, 0);
                player.switchOrientation();
                player.setX(getWidth()/2-player.getWidth()/2);
                player.setY(getHeight()-player.getHeight()*2);

                ballThreshold=(int) Math.ceil(Math.random()*ballMultiplier);

                balls.add(new Ball(getWidth()/2, getHeight()/2));

                firstTime=false;
            }


            if(!gameOver) {
                // Perform paddle calculations
                if ((player.getX() > 0 && player.getX() + player.getWidth() < getWidth()) || ((player.getX() <= 0 && player.getMovement().getX() > 0) || (player.getX() + player.getWidth() >= getWidth() && player.getMovement().getX() < 0))) {
                    player.setX((int) (player.getX() + player.getMovement().getX() * paddleSpeed));
                    player.setY((int) (player.getY() + player.getMovement().getY() * paddleSpeed));
                }

                // Perform ball calculations
                for (Ball ball : balls) {
                    // Perform ball calculations
                    if (ball.getY() - ball.getSize() / 2 <= 0) {
                        ball.setDirection(new Point((int) ball.getDirection().getX(), Math.abs((int) ball.getDirection().getY())));
                    }
                    if (ball.getX() - ball.getSize() / 2 <= 0) {
                        ball.setDirection(new Point(Math.abs((int) ball.getDirection().getX()), (int) ball.getDirection().getY()));
                    } else if (ball.getX() + ball.getSize() / 2 >= getWidth()) {
                        ball.setDirection(new Point(-Math.abs((int) ball.getDirection().getX()), (int) ball.getDirection().getY()));
                    }

                    // Check for paddle collision
                    if (ball.getX() - ball.getSize() / 2 < player.getX() + player.getWidth() && ball.getX() + ball.getSize() / 2 > player.getX() && ball.getY() + ball.getSize() / 2 > player.getY() && ball.getY() + ball.getSize() / 2 < player.getY() + player.getHeight()) {
                        score++;

                        ball.setDirection(new Point((int) ball.getDirection().getX(), -1));
                        ball.setDeltaX(Math.random()*Math.sqrt(2)/2+Math.sqrt(2)/2);
                        ball.setDeltaY(Math.sqrt(2-Math.pow(ball.getDeltaX(), 2)));
                        if (ballSpeed < ballMaxSpeed) ballSpeed += 1;
                    }

                    // Check for gameOver conditions
                    if (ball.getY() - ball.getSize()/2 > getHeight()) {
                        gameOver = true;
                    }

                    ball.setX((ball.getX() + ball.getDirection().getX() * ballSpeed * ball.getDeltaX()));
                    ball.setY((ball.getY() + ball.getDirection().getY() * ballSpeed * ball.getDeltaY()));
                }


                if(ballCount>=ballThreshold) {
                    ballThreshold=(int) Math.ceil(Math.random()*ballMultiplier);
                    ballCount=0;

                    balls.add(randomBall());
                }
                if(balls.size()<ballMax) ballCount++;
            }
            else {
                if(gameOverCount>60) {
                    gameOver=false;
                    gameOverCount=0;
                    firstTime=true;
                    balls=new ArrayList<>();
                    ballSpeed=defaultBallSpeed;
                    ballThreshold=0;
                    ballCount=0;
                    score=0;
                }
                gameOverCount++;
            }

            // Draw the background
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());

            player.draw(g2);
            for(Ball ball : balls) {
                ball.draw(g2);
            }

            g2.setFont(new Font("Arial", Font.PLAIN, 24));
            g2.drawString(score+"", 20, 40);

//            System.out.println(balls.get(0).getDeltaX());
//            System.out.print("  "+balls.get(0).getDeltaY());
//            System.out.println("  "+balls.get(0).getDirection());
        }
        private Ball randomBall() {
            Ball b=new Ball((int)(Math.random()*getWidth()), (int)(Math.round(Math.random()*getHeight()/2)));
            b.setDirection(randomDirectionPoint());
            return b;
        }
        // Returns a directional vector in a random y=x direction
        private Point randomDirectionPoint() {
            // Local xy variables
            int randX;
            int randY;
            // Set them to either 1 or 0 (pseudo-random)
            randX=(int)Math.round(Math.random());
            randY=(int)Math.round(Math.random());
            // If they equal 0, change it to a -1
            if(randX==0) randX=-1;
            if(randY==0) randY=-1;

            // Return a point containing random -1s and 1s
            return new Point(randX, randY);
        }
    }
}
