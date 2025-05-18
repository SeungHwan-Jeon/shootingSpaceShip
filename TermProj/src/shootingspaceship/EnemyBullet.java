package shootingspaceship;

import java.awt.*;

/*
 * EnemyBullet 클래스
 * 보스 또는 Enemy가 쏘는 총알 1발을 뜻함
 * EnemyBullet 객체는 EnemyBullet 리스트에 보관
 * 프레임 마다 이동, 충돌 검사, 소멸 처리*/

public class EnemyBullet {
    private int x, y, speed=5;
    private boolean alive = true;
    
    public EnemyBullet(int x, int y) { this.x=x; this.y=y; }
    
    public void move() { y += speed; if (y>600) alive = false;}
    
    public void draw(Graphics g) { if(alive) { g.setColor(Color.BLUE); g.fillOval(x, y, 5, 5); } }
    
    // 플레이어와 충돌 검사
    public boolean checkCollision(Player p) {
        if (alive && p.isHit(x, y)) { 
        	alive = false; 
        	p.setDead(); 
        	return true; 
        }
        
        return false;
    }
    public boolean isAlive() { return alive; }
}
