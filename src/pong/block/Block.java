package pong.block;

import java.awt.*;

public class Block {
    // Default values for a block
    private final static int DEFAULT_STRENGTH=1;
    private final static int DEFAULT_X=0;
    private final static int DEFAULT_Y=0;
    private final static int DEFAULT_WIDTH=200;
    private final static int DEFAULT_HEIGHT=100;

    // List of colors which the block can be
    // NOTE: This depends completely on the block's strength!
    // @see private int strength;
    private static Color[] colors={
            new Color(0, 198, 0),
            new Color(220, 217, 0),
            new Color(255, 0, 4),
            new Color(204, 0, 131),
            new Color(10, 0, 144)
    };

    // X pos, in pixels, of the box
    private int x;
    // Y pos, in pixels, of the box
    private int y;
    // Width, in pixels, of the box
    private int width;
    // Height, in pixels, of the box
    private int height;
    // The strength of the box.
    // Strength is the number of hits a box takes to destroy
    private int strength;

    // Make a variety of constructors for easier implementation
    public Block() {
        this(DEFAULT_X, DEFAULT_Y);
    }
    public Block(int x, int y) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    public Block(int x, int y, int width, int height) {
        this(x, y, width, height, DEFAULT_STRENGTH);
    }
    public Block(int x, int y, int width, int height, int strength) {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        this.strength=strength;
    }

    // Provide draw method for code organization
    public void draw(Graphics2D g2) {
        // Set the color to the color based on strength
        g2.setColor(getColor());
        // Fill a rectangle at box x and y
        g2.fillRect(x, y, width, height);
        // Set color to white
        g2.setColor(Color.WHITE);
        // If the box color is yellow, make the color black
        // This is because white text on a yellow background just doesn't work :)
        if(getColor()==colors[1]) g2.setColor(Color.BLACK);
        // Set the font to a smaller, unobtrusive font
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        // Draw the strength value within the box itself
        g2.drawString(strength+"", x+3, y+10);
    }

    // Returns a color based on strength
    public Color getColor() {
        // Return a color based on strength, but if the strength is greater than
        // the color array's length, loop back and recycle colors
        return colors[(strength-1)%colors.length];
    }
    // More getters and setters
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
    public int getStrength() {
        return strength;
    }
    public void setStrength(int strength) {
        this.strength = strength;
    }

    // Provide debug info when printed
    public String toString() {
        return "Block[x="+x+",y="+y+",width="+width+",height="+height+",strength="+strength+"];";
    }
}
