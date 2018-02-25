package pong.original;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends JFrame {
    // Game ball
    private Ball ball;
    // Game first player (leftmost)
    private Paddle player1;
    // Game second player (rightmost)
    private Paddle player2;
    // Determines whether the 2nd player is an ai or not
    private boolean ai=false;
    public Game() {
        // Set frame properties
        setTitle("Classic Pong");
        // Original game resolution is usually 640x400
        setSize(640, 400);
        setResizable(false);
        // Don't destroy the process since that's what the startup screen is for
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create graphics instance (component-based)
        GameGraphics graphics = new GameGraphics();
        // Add it to the frame
        add(graphics);
        // Listen for key presses on the frame
        addKeyListener(new KeyListener() {
            // Detect key press (down only)
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    // Up Arrow
                    case 38:
                        if(!ai) player2.setMovement(new Point(0, -1));
                        break;
                    // Down Arrow
                    case 40:
                        if(!ai) player2.setMovement(new Point(0, 1));
                        break;
                    // W
                    case 87:
                        player1.setMovement(new Point(0, -1));
                        break;
                    // S
                    case 83:
                        player1.setMovement(new Point(0, 1));
                        break;
                    // A
                    case 65:
                        // Set ai mode to true
                        ai=true;
                        break;
                    case 86:
                        // Set ai mode to false
                        ai=false;
                        break;
                }
            }
            // Detect key press (up only)
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()) {
                    // Up Arrow
                    case 38:
                        player2.setMovement(new Point(0, 0));
                        break;
                    // Down Arrow
                    case 40:
                        player2.setMovement(new Point(0, 0));
                        break;
                    // W
                    case 87:
                        player1.setMovement(new Point(0, 0));
                        break;
                    // S
                    case 83:
                        player1.setMovement(new Point(0, 0));
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

    // Responsible for managing frames and animation
    class GameGraphics extends Component {
        // Paddle margin from side walls
        private final static int PADDLE_MARGIN=10;

        // Boolean for firstTime in frame write
        private boolean firstTime=true;
        // Current paddle speed
        private double paddleSpeed=6;
        // Current ball speed
        private double ballSpeed=4;
        // Maximum ball speed
        // The ball accelerates when it hits a paddle until it reaches the max
        private double ballMaxSpeed=8;
        // Timer used for running at 60 fps
        private Timer timer;
        // Variables used for gameOver tracking
        private boolean gameOver=false;
        private int gameOverCounter=0;
        // Font used for the score
        private Font scoreFont=new Font("Arial", Font.PLAIN, 16);
        private Font directionsFont=new Font("Arial", Font.PLAIN, 10);
        public GameGraphics() {
            // Create a new timer
            timer=new Timer("animation");
            // Then schedule repainting 60 times a second (60 fps)
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    repaint();
                }
            }, 0, (int)(1000/60.0));
        }
        public void paint(Graphics g) {
            // Cast to more advanced graphics
            Graphics2D g2=(Graphics2D)g;
            // For some reason by default on some computers this is off.
            // When it is off, circles and other arcs draw in extreme low resolution (they look like hexagons!)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the background as black
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw the central divider
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());

            // Code to be run only on the first frame of the game
            if(firstTime) {
                // Init all the important dynamic game aspects that don't initially need resetting
                ball=new Ball(getWidth()/2, getHeight()/2);
                ball.setDirection(randomDirectionPoint());
                player1=new Paddle(PADDLE_MARGIN, getHeight()/2-Paddle.PADDLE_HEIGHT/2);
                player2=new Paddle(getWidth()-Paddle.PADDLE_WIDTH-PADDLE_MARGIN, getHeight()/2-Paddle.PADDLE_HEIGHT/2);

                // It's not longer the first time, so set this to false
                firstTime=false;
            }
            if(!gameOver) {
                // Change player2 y if ai is on
                if(ai) {
                    // Default to 0
                    int direction=0;
                    // If the player needs to move up, set its direction to up
                    if(player2.getY()+player2.getHeight()/2>ball.getY()) direction=-1;
                    // If the player needs to move down, set its direction to down
                    else if(player2.getY()+player2.getHeight()/2<ball.getY()) direction=1;
                    // Set the movement vector of the player
                    player2.setMovement(new Point(0, direction));
                }

                // Perform paddle calculations
                if ((player1.getY() > 0 && player1.getY() + player1.getHeight() < getHeight()) || ((player1.getY() <= 0 && player1.getMovement().getY() > 0) || (player1.getY() + player1.getHeight() >= getHeight() && player1.getMovement().getY() < 0))) {
                    // Change y position based on movement direction
                    player1.setY((int) (player1.getY() + (player1.getMovement().getY() * paddleSpeed)));
                }
                if ((player2.getY() > 0 && player2.getY() + player2.getHeight() < getHeight()) || ((player2.getY() <= 0 && player2.getMovement().getY() > 0) || (player2.getY() + player2.getHeight() >= getHeight() && player2.getMovement().getY() < 0))) {
                    // Same thing as above, except give the paddle different speed parameters depending on what it "wants"
                    // Set a temporary paddle speed
                    double tempPS=paddleSpeed;
                    // Make the paddle go as fast as the ball until the ball surpasses 6 px/sec.
                    if(ai&&ballSpeed<6) paddleSpeed=ballSpeed;
                    // If its greater than six, the ai paddle speed should be the ball speed minus 1.5 px/sec.
                    else if(ai&&ballSpeed>6) paddleSpeed=ballSpeed-1.5;
                    // If it equals six, the ai paddle speed should be the ball speed minus 0.5 px/sec.
                    else if(ai) paddleSpeed=ballSpeed-0.5;
                    player2.setY((int) (player2.getY() + (player2.getMovement().getY() * paddleSpeed)));
                    // Reset paddle speed to what it was before this if statement
                    paddleSpeed=tempPS;
                }

                // Perform ball calculations
                if (ball.getY() - ball.getSize() / 2 <= 0) {
                    ball.setDirection(new Point((int) ball.getDirection().getX(), 1));
                } else if (ball.getY() + ball.getSize() / 2 >= getHeight()) {
                    ball.setDirection(new Point((int) ball.getDirection().getX(), -1));
                }
                // Check for paddle collision (player 1)
                if (ball.getX() - ball.getSize() / 2 < player1.getX() + player1.getWidth() && ball.getX() + ball.getSize() / 2 > player1.getX() && ball.getY() > player1.getY() && ball.getY() < player1.getY() + player1.getHeight()) {
                    ball.setDirection(new Point(1, (int) ball.getDirection().getY()));
                    if (ballSpeed <= ballMaxSpeed) ballSpeed += 1;
                }
                // Check for paddle collision (player 2)
                if (ball.getX() + ball.getSize() / 2 > player2.getX() && ball.getX() - ball.getSize() / 2 < player2.getX() + player2.getWidth() && ball.getY() > player2.getY() && ball.getY() < player2.getY() + player2.getHeight()) {
                    ball.setDirection(new Point(-1, (int) ball.getDirection().getY()));
                    if (ballSpeed <= ballMaxSpeed) ballSpeed += 1;
                }
                // Check for gameOver conditions
                if (ball.getX() + ball.getSize() / 2 < 0 || ball.getX() - ball.getSize() / 2 > getWidth()) {
                    gameOver = true;
                }

                // Change ball x and y based on current directional vector
                ball.setX((int) (ball.getX() + ball.getDirection().getX() * ballSpeed));
                ball.setY((int) (ball.getY() + ball.getDirection().getY() * ballSpeed));
            }
            // If the game is over, then...
            else {
                // Get the winner string
                String winner="Player 1 ";
                // If it went off the player 2's side, that player lost
                if(ball.getX()-ball.getSize()/2<0) {
                    winner="Player 2 ";
                }
                // Make a black background around the text
                g2.setColor(Color.BLACK);
                g2.fillRect(getWidth()/2-50, getHeight()/2-20, 100, 40);
                // Draw the text on top of the black background
                g2.setColor(Color.WHITE);
                drawCenteredString(winner+" wins!", getWidth()/2, getHeight()/2, new Font("Arial", Font.PLAIN, 24), g2);

                // Check if the counter has reached over 60 (will happen after a second)
                if(gameOverCounter>60) {
                    // Find who won and increment their score
                    if(ball.getX()-ball.getSize()/2<0) {
                        player2.incrementScore();
                    }
                    else {
                        player1.incrementScore();
                    }

                    // Reset all the variables that need to be reset before the next game reset
                    ballSpeed=3;
                    ball.setX(getWidth()/2);
                    ball.setY((int)(Math.random()*getHeight()+1));
                    gameOver=false;
                    gameOverCounter=0;
                    player1.setY(player1.getFirstY());
                    player2.setY(player2.getFirstY());
                    ball.setDirection(randomDirectionPoint());
                }
                // Increment the counter for the next execution frame
                gameOverCounter++;
            }

            // Draw the ball
            ball.draw(g2);

            // Draw the players
            player1.draw(g2);
            player2.draw(g2);

            // Draw the scores
            drawCenteredString(""+player1.getScore(), getWidth()/2-20, 25, scoreFont, g2);
            drawCenteredString(""+player2.getScore(), getWidth()/2+20, 25, scoreFont, g2);

            // Draw ai/1v1 mode directions
            if(ai) {
                drawCenteredString("Press 'v' for standard mode", getWidth()-100, 6, directionsFont, g2);
            }
            else {
                drawCenteredString("Press 'a' for ai mode", getWidth()-100, 6, directionsFont, g2);
            }

            // Draw current mode
            if(ai) g2.drawString("Mode: AI", 10, 10);
            else   g2.drawString("Mode: Standard", 10, 10);
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

        // Utility method for drawing a string based on central coordinates
        private void drawCenteredString(String s, int sX, int sY, Font f, Graphics g) {
            int w=getWidth();
            int h=getHeight();

            // Set the graphic's font to the provided one
            g.setFont(f);
            FontMetrics fm = g.getFontMetrics();
            // Calculate the string's x and y coordinates
            int x = (w - fm.stringWidth(s)) / 2;
            int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
            // Draw the string on the graphcis object
            Font pre=g.getFont();
            g.drawString(s, x-w/2+sX, y-h/2+sY);
            // Set the font to what it was before this method call
            g.setFont(pre);
        }
    }
}