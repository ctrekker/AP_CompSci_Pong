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

    // List of points which will be drawn as the trail (if draw(g2, true) is called)
    private ArrayList<Point> trail;

    // These deltas are used to move the ball at a different angle than 45 degrees.
    // Since Point can hold only integers, the point value is multiplied by this boolean
    // value to determine the realDelta
    private double deltaX;
    private double deltaY;
    // Init with default values
    public Ball() {
        this(-BALL_SIZE, -BALL_SIZE);
    }
    // Init with x and y
    public Ball(int x, int y) {
        // Set local instance variables to defaults and provided values
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
    // Draw the ball given the graphics object AND whether a tail should be drawn
    public void draw(Graphics2D g2, boolean drawTail) {
        // Set the color to white
        g2.setColor(Color.WHITE);
        // Fill an arc (the actual ball) at current ball position
        g2.fillArc((int)(x-size/2), (int)(y-size/2), size, size, 0, 360);
        // Draw a tail if drawTail is true
        if(drawTail) {
            // The current opacity of the previous ball position
            int i = 100;
            // Loop through trail (list of points)
            for (int e = 0; e < trail.size(); e++) {
                // Get the current point
                Point p = trail.get(e);
                // Make sure the opacity isn't less than 0. If it is, that would throw an exception (without the if)
                if (i > 0) {
                    // Set the color to white, with opacity of 'i'
                    g2.setColor(new Color(255, 255, 255, i));
                    // Fill an arc with the previous location of that ball w/ current color
                    g2.fillArc((p.x - size / 2), (p.y - size / 2), size, size, 0, 360);
                }
                // If the opacity is less than zero, remove that element from the list
                else {
                    trail.remove(e);
                }
                // Subtract 2 from the opacity value each time
                i -= 2;
            }
        }
        // Set the color back to white.
        // NOTE: By default, colors have an opacity of 255
        g2.setColor(Color.WHITE);

        // Add a new point to the array to be drawn the next time draw() is called
        trail.add(0, new Point((int)x, (int)y));
        // Draw an additional ball in between the first two balls to reduce frame weirdness
        if(trail.size()>1) {
            // Get their points
            Point p1=trail.get(0);
            Point p2=trail.get(1);
            // Find the difference between them
            int diffX=p1.x-p2.x;
            int diffY=p1.y-p2.y;

            // Draw the ball at half the distance
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