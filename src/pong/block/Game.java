//Name - Connor Burns
//Date - 2/26/18
//Class - AP Comp Sci Period 7
//Lab  - Pong
package pong.block;

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
    // player
    private Paddle player;
    // List of balls. Currently unused, but multiple balls at once is completely supported!
    private ArrayList<Ball> balls;
    // Speed of player's paddle
    private int paddleSpeed=6;
    // Default speed of the ball
    private int defaultBallSpeed=4;
    // Speed of the ball. Init to default
    private int ballSpeed=defaultBallSpeed;
    // If this is true, the ball will go twice as fast
    // This is controlled with the up arrow by the player
    // That way, they can increase the rate of the game during slow moments
    private boolean speedUp=false;
    // Current level the player is on. This determines the strength of boxes, and possibly arrangement
    private int currentLevel=1;

    public Game() {
        // Init balls arrayList
        balls=new ArrayList<>();

        // Set frame properties
        setTitle("Block Breaker");
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
                    // Down arrow
                    case 40:
                        // Increase the level
                        currentLevel++;
                        // Clear the current blocks list
                        graphics.blocks.clear();
                        // Re-setup the blocks
                        graphics.setupBlocks(currentLevel);
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
        // Make an arrayList for blocks
        public ArrayList<Block> blocks=new ArrayList<>();

        // Lots of variables which control the state of the game
        // They do exactly what they are named
        private boolean firstTime=true;
        private boolean gameOver=false;
        private int gameOverCount=0;
        private int ballCount=0;
        private int ballMultiplier=1200;
        private int ballThreshold=0;
        private int ballMax=1;
        private int lastBlockHit;
        private int lastBlockHitCount=0;

        // Stores the current score
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
            // Cast to higher-level graphics object
            Graphics2D g2=(Graphics2D)g;

            // For some reason by default on some computers this is off.
            // When it is off, circles and other arcs draw in extreme low resolution (they look like hexagons!)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            // Do only on the first time
            if(firstTime) {
                // Clear the block array
                blocks.clear();
                // set up blocks
                setupBlocks(currentLevel);

                // Init player x and y
                player=new Paddle(0, 0);
                // Switch orientation from up and down, to left and right
                player.switchOrientation();
                player.setX(getWidth()/2-player.getWidth()/2);
                player.setY(getHeight()-player.getHeight()*2);

                // Next time a new ball should be added. CURRENTLY UNIMPLEMENTED
                ballThreshold=(int) Math.ceil(Math.random()*ballMultiplier);

                // Add a ball at a randomish height
                balls.add(new Ball(getWidth()/2, getHeight()-100));
                // Make the direction x random
                int dirX=(int)(Math.random()*2);
                if(dirX==0) dirX=-1;
                // Set the direction to this
                balls.get(0).setDirection(new Point(dirX, 1));
                // Make the deltaX very small so the ball essentially goes straight up/down the first time
                balls.get(0).setDeltaX(0.00001);

                // Make sure this isn't run again until the game is reset
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
                        ball.setDirection(new Point((int) ball.getDirection().getX(), -1));
                        ball.setDeltaX(Math.random()*Math.sqrt(2)/2+Math.sqrt(2)/2);
                        ball.setDeltaY(Math.sqrt(2-Math.pow(ball.getDeltaX(), 2)));
                    }

                    // Check for block collision
                    for(int i=0; i<blocks.size(); i++) {
                        // This portion doesn't make sense at all in code.
                        // The best way to understand how it works is on paper. If you'd like to see my math
                        // just ask me for my paper which I figured out how to do the box collision. It is a
                        // heck of a lot harder than it seems!
                        Block block=blocks.get(i);
                        // Get the block center
                        int blockCenterX=block.getX()+block.getWidth()/2;
                        int blockCenterY=block.getY()+block.getHeight()/2;
                        // Get the delta offset of the ball from the center on both axis
                        double deltaX=(Math.abs(blockCenterX-ball.getX()))/block.getWidth();
                        double deltaY=(Math.abs(blockCenterY-ball.getY()))/block.getHeight();
                        // Init a boolean based on deltas. This determines whether collision should
                        // be handled on the vertical (y) or horizonal (x) axis
                        boolean vertical=deltaX<deltaY;

                        // Check on x coordinate plane (bounce)
                        if(ball.getY()>block.getY()
                                &&ball.getY()<block.getY()+block.getHeight()
                                &&ball.getX()+ball.getSize()/2>block.getX()
                                &&ball.getX()-ball.getSize()/2<block.getX()+block.getWidth()
                                &&!vertical) {
                            // Bounce ball based on calculations
                            if(ball.getX()<block.getX()+block.getWidth()/2) {
                                ball.setDirection(new Point(-Math.abs((int) ball.getDirection().getX()), (int) ball.getDirection().getY()));
                                blockHit(i);
                            }
                            else if(ball.getX()>block.getX()+block.getWidth()/2) {
                                ball.setDirection(new Point(Math.abs((int) ball.getDirection().getX()), (int) ball.getDirection().getY()));
                                blockHit(i);
                            }
                            else {
                                ball.setDirection(new Point((int)(-ball.getDirection().getX()), (int)(-ball.getDirection().getY())));
                            }
                        }

                        // Check on y coordinate plane (bounce)
                        else if(ball.getX()>block.getX()
                                &&ball.getX()<block.getX()+block.getWidth()
                                &&ball.getY()+ball.getSize()/2>block.getY()
                                &&ball.getY()-ball.getSize()/2<block.getY()+block.getHeight()
                                &&vertical) {
                            // Bounce ball based on calculations
                            if(ball.getY()<block.getY()+block.getHeight()/2) {
                                ball.setDirection(new Point((int) ball.getDirection().getX(), -Math.abs((int) ball.getDirection().getY())));
                                blockHit(i);
                            }
                            else if(ball.getY()>block.getY()+block.getHeight()/2) {
                                ball.setDirection(new Point((int) ball.getDirection().getX(), Math.abs((int) ball.getDirection().getY())));
                                blockHit(i);
                            }
                            else {
                                ball.setDirection(new Point((int)(-ball.getDirection().getX()), (int)(-ball.getDirection().getY())));
                            }
                        }
                    }

                    // Check for gameOver conditions
                    if (ball.getY() - ball.getSize()/2 > getHeight()) {
                        gameOver = true;
                    }

                    // If the player wants to speed the ball up, increase its speed TEMPORARILY by 2
                    if(speedUp) ballSpeed*=2;
                    // Set the x and y coordinates of the ball based on deltas, direction, and previous position
                    ball.setX((ball.getX() + ball.getDirection().getX() * ballSpeed * ball.getDeltaX()));
                    ball.setY((ball.getY() + ball.getDirection().getY() * ballSpeed * ball.getDeltaY()));
                    // Reset the speed if it was sped up. That way it doesn't interfere with calculations
                    if(speedUp) ballSpeed/=2;
                }

                // Currently not implemented.
                // Used to progressively add more balls on an exponential-time curve
                if(ballCount>=ballThreshold) {
                    ballThreshold=(int) Math.ceil(Math.random()*ballMultiplier);
                    ballCount=0;

                    balls.add(randomBall());
                }
                // Increment ball frame counter
                if(balls.size()<ballMax) ballCount++;
            }
            else {
                // If its been 60 frames (a second), then...
                if(gameOverCount>60) {
                    // Reset all these instance variables so the next game will start similarly
                    gameOver=false;
                    gameOverCount=0;
                    firstTime=true;
                    balls=new ArrayList<>();
                    ballSpeed=defaultBallSpeed;
                    ballThreshold=0;
                    ballCount=0;
                    score=0;
                    currentLevel=1;
                }
                // Increase the gameOver count
                gameOverCount++;
            }

            // Increment the lastBlockHit counter
            lastBlockHitCount++;

            // If the last time a block has been hit was 2 seconds ago, there are no blocks left, and the player's
            // ball is near the bottom of the screen, then increase the level and set up the next level's blocks
            // on the screen
            if(lastBlockHitCount>120&&blocks.size()==0&&balls.get(0).getY()>getHeight()-getHeight()/10) {
                currentLevel++;
                lastBlockHitCount=0;
                setupBlocks(currentLevel);
            }

            // Draw the background
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw the player
            player.draw(g2);
            // Draw every ball
            for(Ball ball : balls) {
                ball.draw(g2, true);
            }
            // Draw every block
            for(Block block : blocks) {
                block.draw(g2);
            }

            // Draw the player's current score
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 24));
            g2.drawString(score+"", 20, 40);
        }
        // Handle when a block is hit
        private void blockHit(int i) {
            Block block=blocks.get(i);
            // Make sure the lastBlock that was hit wasn't the same block, unless the lastBlockHit was 4 frames ago
            if(lastBlockHit!=i||lastBlockHitCount>=4) {
                // If the strength is one, then...
                if (block.getStrength() <= 1) {
                    // Remove the block
                    blocks.remove(i);
                    // Increment the score
                    score++;
                }
                // If not, then...
                else {
                    // Decrement the current block strength
                    block.setStrength(block.getStrength() - 1);
                }
                // Make the last block that was hit equal to 'i'
                lastBlockHit=i;
            }
            // Last hit was 0 frames ago as of this call
            lastBlockHitCount=0;
        }

        // Set up level
        // Has potential for unique levels based on shape, rather than procedural-style generation
        public void setupBlocks(int level) {
            // Make a new list of balls
            ArrayList<Block> b=new ArrayList<>();

            // Set block constants for generation
            final int boxGap=10;
            final int boxWidth=30;
            final int boxHeight=15;
            final int boxNumX=getWidth()/(boxWidth+boxGap);
            final int boxNumY=5;
            final int boxBaseX=((getWidth())-(boxWidth+boxGap)*boxNumX+boxGap)/2;
            final int boxBaseY=50;

            // Loop in a 2d array
            for(int y=0; y<boxNumY; y++) {
                for(int x=0; x<boxNumX; x++) {
                    // Add a block at the current calculated x and y positions
                    b.add(new Block(boxBaseX+(boxWidth+boxGap)*x, boxBaseY+(boxHeight+boxGap)*y, boxWidth, boxHeight));
                    // Set the strength to the current level
                    b.get(b.size()-1).setStrength(level);
                }
            }

            // Set the blocks array to the new block array
            blocks=b;
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
