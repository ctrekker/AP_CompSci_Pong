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
    private Paddle player;
    private ArrayList<Ball> balls;
    private int paddleSpeed=6;
    private int defaultBallSpeed=4;
    private int ballSpeed=defaultBallSpeed;
    private boolean speedUp=false;
    private int currentLevel=1;

    public Game() {
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
                        currentLevel++;
                        graphics.blocks.clear();
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
        public ArrayList<Block> blocks=new ArrayList<>();

        private boolean firstTime=true;
        private boolean gameOver=false;
        private int gameOverCount=0;
        private int ballCount=0;
        private int ballMultiplier=1200;
        private int ballThreshold=0;
        private int ballMax=1;
        private int lastBlockHit;
        private int lastBlockHitCount=0;

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

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if(firstTime) {
                blocks.clear();
                // set up blocks
                setupBlocks(currentLevel);

                player=new Paddle(0, 0);
                player.switchOrientation();
                player.setX(getWidth()/2-player.getWidth()/2);
                player.setY(getHeight()-player.getHeight()*2);

                ballThreshold=(int) Math.ceil(Math.random()*ballMultiplier);

                balls.add(new Ball(getWidth()/2, getHeight()-100));
                int dirX=(int)(Math.random()*2);
                if(dirX==0) dirX=-1;
                balls.get(0).setDirection(new Point(dirX, 1));
                balls.get(0).setDeltaX(0.00001);

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
                        Block block=blocks.get(i);
                        int blockCenterX=block.getX()+block.getWidth()/2;
                        int blockCenterY=block.getY()+block.getHeight()/2;
                        double deltaX=(Math.abs(blockCenterX-ball.getX()))/block.getWidth();
                        double deltaY=(Math.abs(blockCenterY-ball.getY()))/block.getHeight();
                        boolean vertical=deltaX<deltaY;

                        // Check on x coordinate plane (bounce)
                        if(ball.getY()>block.getY()
                                &&ball.getY()<block.getY()+block.getHeight()
                                &&ball.getX()+ball.getSize()/2>block.getX()
                                &&ball.getX()-ball.getSize()/2<block.getX()+block.getWidth()
                                &&!vertical) {
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

                    if(speedUp) ballSpeed*=2;
                    ball.setX((ball.getX() + ball.getDirection().getX() * ballSpeed * ball.getDeltaX()));
                    ball.setY((ball.getY() + ball.getDirection().getY() * ballSpeed * ball.getDeltaY()));
                    if(speedUp) ballSpeed/=2;
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
                    currentLevel=1;
                }
                gameOverCount++;
            }

            lastBlockHitCount++;

            if(lastBlockHitCount>120&&blocks.size()==0&&balls.get(0).getY()>getHeight()-getHeight()/10) {
                currentLevel++;
                lastBlockHitCount=0;
                setupBlocks(currentLevel);
            }

            // Draw the background
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, getWidth(), getHeight());

            player.draw(g2);
            for(Ball ball : balls) {
                ball.draw(g2, true);
            }

            for(Block block : blocks) {
                block.draw(g2);
            }

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 24));
            g2.drawString(score+"", 20, 40);
        }
        private void blockHit(int i) {
            Block block=blocks.get(i);
            if(lastBlockHit!=i||lastBlockHitCount>=4) {
                if (block.getStrength() <= 1) {
                    blocks.remove(i);
                    score++;
                } else {
                    block.setStrength(block.getStrength() - 1);
                }
                lastBlockHit=i;
            }
            lastBlockHitCount=0;
        }

        public void setupBlocks(int level) {
            ArrayList<Block> b=new ArrayList<>();

            //if(level<=1) {
                final int boxGap=10;
                final int boxWidth=30;
                final int boxHeight=15;
                final int boxNumX=getWidth()/(boxWidth+boxGap);
                final int boxNumY=5;
                final int boxBaseX=((getWidth())-(boxWidth+boxGap)*boxNumX+boxGap)/2;
                final int boxBaseY=50;

                for(int y=0; y<boxNumY; y++) {
                    for(int x=0; x<boxNumX; x++) {
                        b.add(new Block(boxBaseX+(boxWidth+boxGap)*x, boxBaseY+(boxHeight+boxGap)*y, boxWidth, boxHeight));
                        b.get(b.size()-1).setStrength(level);
                    }
                }
            //}

            blocks=b;
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
