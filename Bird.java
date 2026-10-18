import java.awt.*;

/** INHERITANCE: Bird is-a GameObject. Also implements the Collidable interface. */
public class Bird extends GameObject implements Collidable {
    private static final double GRAVITY = 0.45;
    private static final double FLAP_POWER = -7.5;
    private double velocity;

    public Bird(double x, double y) {
        super(x, y, 34, 26);
    }

    public void flap() { velocity = FLAP_POWER; }

    public void reset(double startY) {
        y = startY;
        velocity = 0;
    }

    @Override
    public void update() {           // POLYMORPHISM: overriding
        velocity += GRAVITY;
        y += velocity;
    }

    @Override
    public void draw(Graphics2D g) {
        // tilt according to speed
        double angle = Math.max(-0.5, Math.min(1.2, velocity * 0.08));
        Graphics2D g2 = (Graphics2D) g.create();
        g2.rotate(angle, x + width / 2.0, y + height / 2.0);
        g2.setColor(new Color(255, 205, 40));
        g2.fillOval((int) x, (int) y, width, height);          // body
        g2.setColor(Color.WHITE);
        g2.fillOval((int) x + 20, (int) y + 4, 11, 11);        // eye
        g2.setColor(Color.BLACK);
        g2.fillOval((int) x + 25, (int) y + 8, 4, 4);
        g2.setColor(new Color(240, 100, 40));
        g2.fillPolygon(new int[]{(int) x + 30, (int) x + 42, (int) x + 30},
                       new int[]{(int) y + 12, (int) y + 16, (int) y + 20}, 3); // beak
        g2.setColor(new Color(230, 160, 20));
        g2.fillOval((int) x + 4, (int) y + 10, 14, 9);         // wing
        g2.dispose();
    }

    @Override
    public boolean collidesWith(Collidable other) {
        return other.collidesWith(this);   // let the other object decide (Pipe has a gap)
    }

    @Override
    public Rectangle getBounds() {   // slightly smaller hitbox = fairer game
        return new Rectangle((int) x + 3, (int) y + 3, width - 6, height - 6);
    }
}
