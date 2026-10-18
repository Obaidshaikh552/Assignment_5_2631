/** ENUM: the states the game can be in (with a field and a method). */
public enum GameState {
    READY("Press SPACE / Click to start"),
    PLAYING(""),
    GAME_OVER("Game Over! Press SPACE / Click to restart");

    private final String message;

    GameState(String message) { this.message = message; }

    public String getMessage() { return message; }
}
