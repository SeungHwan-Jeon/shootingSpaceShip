package shootingspaceship;

/*
 * Stage 클래스
 * 각 스테이지의 난이도, 적 특성, 보스 특성 한 번에 관리
 * 게임 시작 시 Stage[]로 한 번에 관리*/

public class Stage {
    private int enemyHp;
    private int enemySpeed;
    private int maxEnemies;
    private boolean hasBoss;
    private int bossHp;
    private int bossSpeed;

    public Stage(int enemyHp, int enemySpeed, int maxEnemies, boolean hasBoss, int bossHp, int bossSpeed) {
        this.enemyHp = enemyHp;
        this.enemySpeed = enemySpeed;
        this.maxEnemies = maxEnemies;
        this.hasBoss = hasBoss;
        this.bossHp = bossHp;
        this.bossSpeed = bossSpeed;
    }
    
    public int getEnemyHp() { return enemyHp; }
    public int getEnemySpeed() { return enemySpeed; }
    public int getMaxEnemies() { return maxEnemies; }
    public boolean hasBoss() { return hasBoss; }
    public int getBossHp() { return bossHp; }
    public int getBossSpeed() { return bossSpeed; }
}
