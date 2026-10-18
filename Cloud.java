import java.awt.*;
import java.util.Random;

/** Decorative object: shows polymorphism (same update/draw interface, different behaviour). */
public class Cloud extends GameObject {
    private final double speed;

    public Cloud(double x, double y, Random rnd) {
        super(x, y, 70 + rnd.nextInt(40), 28);
        this.speed = 0.3 + rnd.nextDouble() * 0.4;
    }

    @Override
    public void update() {
        x -= speed;
        if (x + width < 0) x = 500;     // wrap around
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(new Color(255, 255, 255, 190));
        g.fillOval((int) x, (int) y, width, height);
        g.fillOval((int) x + 15, (int) y - 12, width - 30, height);
    }
}
