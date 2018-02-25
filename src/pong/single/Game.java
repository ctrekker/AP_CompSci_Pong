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
    private boolean speedUp=false;

    public Game() {
        balls=new ArrayList<>();

        // Set frame properties
        setTitle("Single Player Pong");
        // Original game resolution is usually 640x400
        setSize(480, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

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
                    // Up arrow
                    case 38:
                        speedUp=true;
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
                    // Up arrow
                    case 38:
                        speedUp=false;
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
        // List of colors that the background will change to
        private Color[] colors={
                new Color(0, 0, 0),
                new Color(255, 0, 0),
                new Color(236, 137, 0),
                new Color(220, 217, 0),
                new Color(0, 198, 0),
                new Color(0, 0, 255),
                new Color(233, 0, 104),
                new Color(217, 0, 217)
        };

        // Lots and lots of instance variables
        // They do what they are called
        // More description can be found where they are used
        private boolean firstTime=true;
        private boolean gameOver=false;
        private int gameOverCount=0;
        private int ballCount=0;
        private int ballMultiplier=1200;
        private int ballThreshold=0;
        private int ballMax=1;
        private int score=0;
        private boolean fading=false;
        private int colorChangeThreshold=10;
        private int currentColorIndex=0;
        private int colorScore=0;
        private int colorPhase=0;
        private int defaultNextShrink=25;
        private int nextShrink=defaultNextShrink;

        public GameGraphics() {
            // Init a new timer to run 60 times a second
            Timer t=new Timer("animation");
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    repaint();
                }
            }, 0, (int)(1000/60.0));
        }
        public void paint(Graphics g) {
            // Cast to higher level graphics object
            Graphics2D g2=(Graphics2D)g;

            // For some reason by default on some computers this is off.
            // When it is off, circles and other arcs draw in extreme low resolution (they look like hexagons!)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Init only once
            if(firstTime) {
                // Set player x and y to appropriate locations and
                // switches orientation to horizontal
                player=new Paddle(0, 0);
                player.switchOrientation();
                player.setX(getWidth()/2-player.getWidth()/2);
                player.setY(getHeight()-player.getHeight()*2);

                // Unimplemented
                ballThreshold=(int) Math.ceil(Math.random()*ballMultiplier);

                // Add a new ball at a random location
                balls.add(new Ball((int)(Math.random()*getWidth()), (int)(Math.random()*(getHeight()/2))));

                // Make sure this isn't run until a new game is started
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
                        // If the score is greater than the next paddle shrinking score target, then...
                        if(score>=nextShrink) {
                            // Get the new width of the paddle based on an exponential loss function
                            int diff=player.getWidth()-(player.getWidth()/3)*2;
                            // Set the x and width
                            player.setX(player.getX()+diff/2);
                            player.setWidth((player.getWidth()/6)*5);

                            // Set the nextShrink score based on an exponential gain function
                            nextShrink*=2;
                        }

                        // Set the direction of the ball, then the delta to a appropriate values based on hit
                        ball.setDirection(new Point((int) ball.getDirection().getX(), -1));
                        ball.setDeltaX(Math.random()*Math.sqrt(2)/2+Math.sqrt(2)/2);
                        ball.setDeltaY(Math.sqrt(2-Math.pow(ball.getDeltaX(), 2)));
                        // Increment ball speed if it's not already at max
                        if (ballSpeed < ballMaxSpeed) ballSpeed += 1;
                    }

                    // Check for gameOver conditions
                    if (ball.getY() - ball.getSize()/2 > getHeight()) {
                        gameOver = true;
                    }

                    // See pong.block.Game for details on speedUp
                    if(speedUp) ballSpeed*=2;
                    ball.setX((ball.getX() + ball.getDirection().getX() * ballSpeed * ball.getDeltaX()));
                    ball.setY((ball.getY() + ball.getDirection().getY() * ballSpeed * ball.getDeltaY()));
                    if(speedUp) ballSpeed/=2;
                }


                // Not currently used, but can be configured in code to add a new ball at exponential loss function
                if(ballCount>=ballThreshold) {
                    ballThreshold=(int) Math.ceil(Math.random()*ballMultiplier);
                    ballCount=0;

                    balls.add(randomBall());
                }
                // Increase the ballCount for calculation in ballThreshold
                if(balls.size()<ballMax) ballCount++;
            }
            else {
                // Reset the game after a second of delay
                if(gameOverCount>60) {
                    gameOver=false;
                    gameOverCount=0;
                    firstTime=true;
                    balls=new ArrayList<>();
                    ballSpeed=defaultBallSpeed;
                    ballThreshold=0;
                    ballCount=0;
                    score=0;
                    colorScore=0;
                    currentColorIndex=0;
                    nextShrink=defaultNextShrink;
                }
                // Increment the count every frame the game is over, until reset (above)
                gameOverCount++;
            }

            // Draw the background
            g2.setColor(getCurrentColorIndex());
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw the player
            player.draw(g2);
            // Draw the balls
            for(Ball ball : balls) {
                ball.draw(g2, true);
            }
            // Draw the player's score
            g2.setFont(new Font("Arial", Font.PLAIN, 24));
            g2.drawString(score+"", 20, 40);
        }

        /*
        This is an extremely complicated method which gradually fades the background based on a
        linear function with an automatically calculated color delta per frame. Returns a color
        to be drawn as the background. This is called every frame
         */
        private Color getCurrentColorIndex() {
            // If we aren't fading, then...
            if(!fading) {
                // If the score isn't equal to the color score, and we are on a frame which we should change the background color, then...
                if(score!=colorScore&&score%colorChangeThreshold==0) {
                    // set fading to true
                    fading=true;
                }
            }
            // If we ARE fading...
            else {
                // If the score doesn't equal the colorScore, increment the currentColorIndex
                if(score!=colorScore) currentColorIndex++;
                // If we went off the end of the array, then reset the color index to the first array element
                if(currentColorIndex>=colors.length) currentColorIndex=0;
                // Set the color score to the score
                colorScore=score;

                // If the phase has surpassed 8-bit integer max, then...
                if(colorPhase>=255) {
                    // Set fading to false since we are done fading
                    fading=false;
                    // Change the current color phase to 0
                    colorPhase=0;
                }
                else {
                    // Get the previous color
                    int previousColorIndex=currentColorIndex-1;
                    // If we go to a negative index, roll back to the last array element
                    if(previousColorIndex<0) previousColorIndex=colors.length-1;

                    // Get the current color
                    Color current=colors[currentColorIndex];
                    // Get the previous color
                    Color previous=colors[previousColorIndex];

                    // Get the delta r, g, and b values. These are the separation between the
                    // values of the different colors.
                    // Range: -512<=(r,g,b)<=512
                    int r=colors[currentColorIndex].getRed()-colors[previousColorIndex].getRed();
                    int g=colors[currentColorIndex].getGreen()-colors[previousColorIndex].getGreen();
                    int b=colors[currentColorIndex].getBlue()-colors[previousColorIndex].getBlue();

                    // Increase the phase by increment of 5.
                    // Controls the speed of the fading
                    colorPhase+=5;

                    // Get the faded color based on the current phase
                    Color faded=new Color(
                            (int)(previous.getRed()+(r/255.0)*colorPhase),
                            (int)(previous.getGreen()+(g/255.0)*colorPhase),
                            (int)(previous.getBlue()+(b/255.0)*colorPhase));
                    // Return the faded color
                    return faded;
                }
            }
            // If nothing special happened this frame, then just return the current color based on the index
            return colors[currentColorIndex];
        }

        // Utility method for getting a random ball
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
