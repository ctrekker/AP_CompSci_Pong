package pong.original;

import java.awt.*;
import java.util.ArrayList;

public class Ball {
    // Size of ball
    private final static int BALL_SIZE=15;
    // Current x and y position
    private double x;
    private double y;
    // Current size of ball
    private int size=BALL_SIZE;
    // Current directional vector
    private Point direction;

    private ArrayList<Point> trail;

    private double deltaX;
    private double deltaY;
    // Init with default values
    public Ball() {
        this(-BALL_SIZE, -BALL_SIZE);
    }
    // Init with x and y
    public Ball(int x, int y) {
        this.x=x;
        this.y=y;
        deltaX=1;
        deltaY=1;
        direction=new Point(1, 1);
        trail=new ArrayList<>();
    }
    // Draw the ball given a graphics object
    public void draw(Graphics2D g2) {
        draw(g2, false);
    }
    public void draw(Graphics2D g2, boolean drawTail) {
        g2.setColor(Color.WHITE);
        g2.fillArc((int)(x-size/2), (int)(y-size/2), size, size, 0, 360);
        if(drawTail) {
            int i = 100;
            for (int e = 0; e < trail.size(); e++) {
                Point p = trail.get(e);
                if (i > 0) {
                    g2.setColor(new Color(255, 255, 255, i));
                    g2.fillArc((p.x - size / 2), (p.y - size / 2), size, size, 0, 360);
                } else {
                    trail.remove(e);
                }
                i -= 2;
            }
        }
        g2.setColor(Color.WHITE);

        trail.add(0, new Point((int)x, (int)y));
        if(trail.size()>1) {
            Point p1=trail.get(0);
            Point p2=trail.get(1);
            int diffX=p1.x-p2.x;
            int diffY=p1.y-p2.y;

            trail.add(1, new Point(p1.x-diffX/2, p1.y-diffY/2));
        }
    }

    // Various getters and setters
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
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

    public double getDeltaY() {
        return deltaY;
    }

    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }
}