import java.awt.Graphics2D;
import java.awt.Rectangle;

/** ABSTRACTION: base class for everything drawn on screen. ENCAPSULATION: protected/private data with getters. */
public abstract class GameObject {
    protected double x, y;
    protected final int width, height;
    private static int objectCount = 0;   // STATIC member shared by all objects

    // Constructor overloading
    public GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        objectCount++;
    }

    public GameObject(int width, int height) {
        this(0, 0, width, height);
    }

    public abstract void update();               // each subclass moves differently
    public abstract void draw(Graphics2D g);     // each subclass draws differently

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public static int getObjectCount() { return objectCount; }
}
