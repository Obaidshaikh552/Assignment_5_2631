import java.io.*;

/** ENCAPSULATION + EXCEPTION HANDLING + FILE I/O: keeps score and saves the high score. */
public class ScoreManager {
    private static final String FILE = "highscore.txt";
    private int score;
    private int highScore;

    public ScoreManager() {
        highScore = load();
    }

    public void addPoint() {
        score++;
        if (score > highScore) {
            highScore = score;
            save();
        }
    }

    public void reset() { score = 0; }
    public int getScore() { return score; }
    public int getHighScore() { return highScore; }

    private int load() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {
            return Integer.parseInt(br.readLine().trim());
        } catch (IOException | NumberFormatException | NullPointerException e) {
            return 0;     // no file yet or corrupted file
        }
    }

    private void save() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE))) {
            pw.println(highScore);
        } catch (IOException e) {
            System.err.println("Could not save high score: " + e.getMessage());
        }
    }
}
