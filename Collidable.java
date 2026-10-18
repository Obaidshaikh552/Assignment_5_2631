import java.awt.Rectangle;

/** INTERFACE: any object that can take part in collision detection. */
public interface Collidable {
    Rectangle getBounds();
    boolean collidesWith(Collidable other);
}
