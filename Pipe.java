import java.awt.*;

/** INHERITANCE + INTERFACE: a pair of pipes with a gap the bird must pass through. */
public class Pipe extends GameObject implements Collidable {
    private final int gapY;      // top of gap
    private final int gapSize;
    private final int groundY;
    private final double speed;
    private boolean passed = false;

    public Pipe(double x, int gapY, int gapSize, int groundY, double speed) {
        super(x, 0, 60, groundY);
        this.gapY = gapY;
        this.gapSize = gapSize;
        this.groundY = groundY;
        this.speed = speed;
    }

    @Override
    public void update() { x -= speed; }

    @Override
    public void draw(Graphics2D g) {
        int px = (int) x;
        drawPipe(g, px, 0, width, gapY, true);
        drawPipe(g, px, gapY + gapSize, width, groundY - (gapY + gapSize), false);
    }

    // private helper (encapsulation)
    private void drawPipe(Graphics2D g, int px, int py, int w, int h, boolean top) {
        g.setColor(new Color(80, 190, 70));
        g.fillRect(px, py, w, h);
        g.setColor(new Color(50, 140, 45));
        g.fillRect(px - 4, top ? py + h - 22 : py, w + 8, 22);   // cap
        g.setColor(new Color(30, 90, 30));
        g.drawRect(px, py, w, h);
    }

    /** Collision is checked against two rectangles (top pipe and bottom pipe). */
    @Override
    public boolean collidesWith(Collidable other) {
        Rectangle b = other.getBounds();
        Rectangle top = new Rectangle((int) x, 0, width, gapY);
        Rectangle bottom = new Rectangle((int) x, gapY + gapSize, width, groundY - gapY - gapSize);
        return top.intersects(b) || bottom.intersects(b);
    }

    public boolean isOffScreen() { return x + width < 0; }
    public boolean isPassed() { return passed; }
    public void markPassed() { passed = true; }
}
