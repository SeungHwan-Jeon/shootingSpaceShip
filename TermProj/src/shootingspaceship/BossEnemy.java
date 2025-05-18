package shootingspaceship;

import java.awt.*;
import java.util.ArrayList;

/*
 * BossEnemy 클래스
 * ENemy 클래스 상속하는 보스 몬스터
 * 체력, 스피드 일반 적보다 높고 총알 발사함*/

public class BossEnemy extends Enemy {
    private int shootCooldown = 0; // 총알 발사를 위한 쿨다운 카운트
    
    public BossEnemy(int x, int y, int hp, int speed) {
        super(x, y, 0, speed, 500, 500, 0, hp);
    }
    
    // 보스 행동  : 이동 + 총알 발사
    public void update(ArrayList<EnemyBullet> bullets) {
        move();				// 위에서 내려오는 이동
        shootCooldown++;	// 발사 쿨다운 카운터 증가
        
        // 쿨 다운 60 이상이면 총알을 하나 발사하고 쿨다운 초기화
        if (shootCooldown >= 60) {
            bullets.add(new EnemyBullet(getX(), getY()+20));
            shootCooldown = 0;
        }
    }
    
    
    // 보스 외형 
    @Override
    public void draw(Graphics g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(getX() - 20, getY() - 20, 40, 40);
    }
}
