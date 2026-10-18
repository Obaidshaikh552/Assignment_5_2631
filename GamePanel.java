import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/** Main game panel. COMPOSITION: owns a Bird, Pipes, Clouds and a ScoreManager. */
public class GamePanel extends JPanel implements ActionListener {
    public static final int WIDTH = 420, HEIGHT = 640, GROUND_Y = 560;
    private static final int PIPE_GAP = 150, PIPE_SPACING = 210;

    private final Random rnd = new Random();
    private final Timer timer = new Timer(16, this);          // ~60 FPS
    private final List<GameObject> scenery = new ArrayList<>();  // POLYMORPHISM: list of base type
    private final List<Pipe> pipes = new ArrayList<>();
    private final Bird bird = new Bird(90, HEIGHT / 2.0 - 40);
    private final ScoreManager scores = new ScoreManager();
    private GameState state = GameState.READY;
    private int groundOffset = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);

        for (int i = 0; i < 4; i++)
            scenery.add(new Cloud(rnd.nextInt(WIDTH), 40 + rnd.nextInt(200), rnd));

        // Anonymous inner classes for input handling
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) handleInput();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) { handleInput(); }
        });
        timer.start();
    }

    private void handleInput() {
        switch (state) {
            case READY:     startGame(); break;
            case PLAYING:   bird.flap(); break;
            case GAME_OVER: resetGame(); break;
        }
    }

    private void startGame() {
        state = GameState.PLAYING;
        bird.flap();
    }

    private void resetGame() {
        pipes.clear();
        bird.reset(HEIGHT / 2.0 - 40);
        scores.reset();
        state = GameState.READY;
    }

    private void spawnPipe() {
        int gapY = 90 + rnd.nextInt(GROUND_Y - PIPE_GAP - 180);
        double speed = 2.8 + Math.min(1.5, scores.getScore() * 0.08);   // gets faster
        pipes.add(new Pipe(WIDTH + 10, gapY, PIPE_GAP, GROUND_Y, speed));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (GameObject o : scenery) o.update();           // polymorphic call

        if (state == GameState.PLAYING) {
            bird.update();
            groundOffset = (groundOffset + 3) % 24;

            if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() < WIDTH - PIPE_SPACING)
                spawnPipe();

            Iterator<Pipe> it = pipes.iterator();
            while (it.hasNext()) {
                Pipe p = it.next();
                p.update();
                if (!p.isPassed() && p.getX() + p.getWidth() < bird.getX()) {
                    p.markPassed();
                    scores.addPoint();
                }
                if (p.collidesWith(bird)) state = GameState.GAME_OVER;   // Pipe checks its top and bottom parts separately
                if (p.isOffScreen()) it.remove();
            }
            if (bird.getY() < 0) bird.reset(0);          // ceiling: bump, don't die
            if (bird.getY() + bird.getHeight() > GROUND_Y)
                state = GameState.GAME_OVER;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setPaint(new GradientPaint(0, 0, new Color(95, 190, 240), 0, GROUND_Y, new Color(190, 235, 250)));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        for (GameObject o : scenery) o.draw(g);
        for (Pipe p : pipes) p.draw(g);

        // ground
        g.setColor(new Color(222, 200, 130));
        g.fillRect(0, GROUND_Y, WIDTH, HEIGHT - GROUND_Y);
        g.setColor(new Color(110, 190, 60));
        g.fillRect(0, GROUND_Y, WIDTH, 14);
        g.setColor(new Color(80, 150, 40));
        for (int x = -groundOffset; x < WIDTH; x += 24) g.fillRect(x, GROUND_Y, 12, 14);

        bird.draw(g);

        // HUD
        g.setFont(new Font("SansSerif", Font.BOLD, 44));
        drawCentered(g, String.valueOf(scores.getScore()), 80, Color.WHITE);
        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.setColor(new Color(60, 40, 10));
        g.drawString("Best: " + scores.getHighScore(), 14, GROUND_Y + 34);

        if (state != GameState.PLAYING) {
            g.setFont(new Font("SansSerif", Font.BOLD, state == GameState.GAME_OVER ? 34 : 20));
            if (state == GameState.GAME_OVER) drawCentered(g, "GAME OVER", 260, new Color(220, 50, 40));
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            drawCentered(g, state == GameState.READY ? state.getMessage() : "Press SPACE / Click to restart",
                         state == GameState.GAME_OVER ? 295 : 380, Color.DARK_GRAY);
        }
    }

    private void drawCentered(Graphics2D g, String text, int y, Color c) {
        FontMetrics fm = g.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(text)) / 2;
        g.setColor(new Color(0, 0, 0, 90));
        g.drawString(text, x + 2, y + 2);
        g.setColor(c);
        g.drawString(text, x, y);
    }
}
