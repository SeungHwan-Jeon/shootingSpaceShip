package shootingspaceship.model;

/*
 * Score 클래스
 * 플레이 중 내 점수를 관리하는 클래스
 * Enemy를 죽일 때마다 점수를 1씩 올리고 화면에 표시*/

public class Score {
    private int score = 0;
    public void increaseScore() { score++; }
    public int getScore() { return score; }
}
