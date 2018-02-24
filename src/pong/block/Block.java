package pong.block;

import java.awt.*;

public class Block {
    private final static int DEFAULT_STRENGTH=1;
    private final static int DEFAULT_X=0;
    private final static int DEFAULT_Y=0;
    private final static int DEFAULT_WIDTH=200;
    private final static int DEFAULT_HEIGHT=100;
    private final static int BOX_RADIUS=5;

    private static Color[] colors={
            new Color(0, 198, 0),
            new Color(220, 217, 0),
            new Color(255, 0, 4),
            new Color(204, 0, 131),
            new Color(10, 0, 144)
    };

    private int x;
    private int y;
    private int width;
    private int height;
    private int strength;

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

    public void draw(Graphics2D g2) {
        g2.setColor(getColor());
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.WHITE);
        if(getColor()==colors[1]) g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString(strength+"", x+3, y+10);
    }

    public Color getColor() {
        return colors[(strength-1)%colors.length];
    }

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

    public String toString() {
        return "Block[x="+x+",y="+y+",width="+width+",height="+height+",strength="+strength+"];";
    }
}
