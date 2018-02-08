package pong.original;

import java.awt.*;

public class Ball {
    // Size of ball
    private final static int BALL_SIZE=15;
    // Current x and y position
    private int x;
    private int y;
    // Current size of ball
    private int size=BALL_SIZE;
    // Current directional vector
    private Point direction;
    // Init with default values
    public Ball() {
        this(-BALL_SIZE, -BALL_SIZE);
    }
    // Init with x and y
    public Ball(int x, int y) {
        this.x=x;
        this.y=y;
        direction=new Point(0, 0);
    }
    // Draw the ball given a graphics object
    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillArc(x-size/2, y-size/2, size, size, 0, 360);
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
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public Point getDirection() {
        return direction;
    }
    public void setDirection(Point direction) {
        this.direction = direction;
    }
}