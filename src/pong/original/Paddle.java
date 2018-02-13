package pong.original;

import java.awt.*;

public class Paddle {
    // Width and height constants for paddle
    public final static int PADDLE_WIDTH=10;
    public final static int PADDLE_HEIGHT=100;
    // Stores first x and y value set to paddle
    // Used for RESETTING
    private final int firstX;
    private final int firstY;
    // Current x and y position of paddle
    private int x;
    private int y;
    // Current width and height of paddle
    private int width;
    private int height;
    // Current vector value of movement i.e. (-1,1), (-1,-1), (1,1), (1,-1)
    private Point movement;
    // Current score of the paddle (player)
    private int score=0;
    public Paddle(int x, int y) {
        // Set all the instance variables to their initial values
        this.firstX=x;
        this.firstY=y;
        this.x=x;
        this.y=y;
        this.width=PADDLE_WIDTH;
        this.height=PADDLE_HEIGHT;
        this.movement=new Point(0, 0);
    }
    // Draw the paddle given a graphics object
    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(x, y, width, height);
    }

    // Various getters and setters
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public Point getMovement() {
        return movement;
    }
    public void setMovement(Point movement) {
        this.movement = movement;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void incrementScore() {
        this.score++;
    }
    public void decrementScore() {
        this.score--;
    }
    public int getFirstX() {
        return firstX;
    }
    public int getFirstY() {
        return firstY;
    }
    public void switchOrientation() {
        int preWidth=getWidth();
        setWidth(getHeight());
        setHeight(preWidth);
    }
}
