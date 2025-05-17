package shootingspaceship;

public class Score {
    private int score = 0;

    public void increaseScore() { score++; }
    public int getScore() { return score; }
    public void reset() { score = 0; }
}
