package shootingspaceship;

import java.awt.*;

/*
 * Enemy 클래스
 * 화면에 등장하는 적 한 마리를 뜻함
 * 총알이나 플레이어와 부딪히면 소멸(or 체력 감소)
 * 체력/스피드 등 다양한 정보 포함*/

public class Enemy {
    // 위치, 속도, 경계
	protected float x_pos, y_pos;
    protected float delta_x, delta_y; // 이동 속도
    protected int max_x, max_y;
    protected float delta_y_inc; // 아래로 내려올 때 점점 빨라지는 속도 변화량
    
    protected final int collision_distance = 10; // 충돌 판정 거리 (= 이정도 가까우면 맞은 것)
    
    protected int hp;
    protected boolean isDead = false;

    public Enemy(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float delta_y_inc, int hp) {
        this.x_pos = x; this.y_pos = y;
        this.delta_x = delta_x; this.delta_y = delta_y;
        this.max_x = max_x; this.max_y = max_y;
        this.delta_y_inc = delta_y_inc;
        this.hp = hp;
    }
    
    // 적 이동 함수
    public void move() {
        x_pos += delta_x; y_pos += delta_y;
        if (x_pos < 0) { x_pos = 0; delta_x = -delta_x; } // 좌우 벽에 닿으면 반대로 튕김
        else if (x_pos > max_x) { x_pos = max_x; delta_x = -delta_x; }
        
        if (y_pos > max_y) { y_pos = 0; delta_y += delta_y_inc; } // 아래 화면 벗어나면 맨 위로 올라가고 속도 더 빨라짐
    }
    
    // 총알과 충돌 판정
    public boolean isCollidedWithShot(Shot[] shots) {
        for (Shot shot : shots) {
            if (shot == null) continue;
            if (!shot.isAlive()) continue;
            
            // Player와 shot의 위치가 10 이내면 맞은것
            if (Math.abs(y_pos - shot.getY()) <= collision_distance && Math.abs(x_pos - shot.getX()) <= collision_distance) {
                shot.collided();
                --hp;
                if (hp <= 0) isDead = true;
                return true;
            }
        }
        return false;
    }
    
    // Enemy와 Player의 위치가 충돌 거리 이내이면(겹치면) 충돌로 판정 
    public boolean isCollidedWithPlayer(Player player) {
        return (Math.abs(y_pos - player.getY()) <= collision_distance && Math.abs(x_pos - player.getX()) <= collision_distance);
    }
    public void draw(Graphics g) {
        g.setColor(Color.yellow);
        int[] x_poly = {(int)x_pos, (int)x_pos - 10, (int)x_pos, (int)x_pos + 10};
        int[] y_poly = {(int)y_pos + 15, (int)y_pos, (int)y_pos + 10, (int)y_pos};
        g.fillPolygon(x_poly, y_poly, 4);
    }
    public boolean isDead() { return isDead; }
    public int getX() { return (int)x_pos; }
    public int getY() { return (int)y_pos; }
}
