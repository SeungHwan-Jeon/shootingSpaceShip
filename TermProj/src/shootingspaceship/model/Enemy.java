package shootingspaceship.model;

import java.awt.*;

/*
 * Enemy 클래스
 * 화면에 등장하는 적 한 마리를 뜻함
 * 총알이나 플레이어와 부딪히면 소멸(or 체력 감소)
 * 체력/스피드 등 다양한 정보 포함
 */

public class Enemy {
    // 위치, 속도, 경계
	protected float x_pos, y_pos;
    protected float delta_x, delta_y; // 이동 속도
    protected int max_x, max_y;
    protected float delta_y_inc; // 아래로 내려올 때 점점 빨라지는 속도 변화량
    protected int hp;
    protected boolean isDead = false;
    protected int radius; // 적 원형 히트박스 크기 (마음대로 조정 가능)

    public Enemy(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float delta_y_inc, int hp, int radius) {
        this.x_pos = x; this.y_pos = y;
        this.delta_x = delta_x; this.delta_y = delta_y;
        this.max_x = max_x; this.max_y = max_y;
        this.delta_y_inc = delta_y_inc;
        this.hp = hp;
        this.radius = radius; // 히트박스 크기!
    }
    
    // 원형 getter/setter 및 중심 반환 함수
    public void setRadius(int r) { radius = r; }
    public int getRadius() { return radius; }
    public Point getCenter() { return new Point((int)x_pos, (int)y_pos); }
    
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
            if (Math.abs(y_pos - shot.getY()) <= 10 && Math.abs(x_pos - shot.getX()) <= 10) {
                shot.collided();
                --hp;
                if (hp <= 0) isDead = true;
                return true;
            }
        }
        return false;
    }
    
    // Enemy와 Player의 **원형 히트박스 충돌**
    public boolean isCollidedWithPlayer(Player player) {
        double dist = getCenter().distance(player.getCenter());
        return dist < (this.radius + player.getRadius());
    }
    
    public void draw(Graphics g) {
        g.setColor(Color.yellow);
        int[] x_poly = {(int)x_pos, (int)x_pos - 10, (int)x_pos, (int)x_pos + 10};
        int[] y_poly = {(int)y_pos + 15, (int)y_pos, (int)y_pos + 10, (int)y_pos};
        g.fillPolygon(x_poly, y_poly, 4);
        
        // (디버깅 시 원형 히트박스)
         g.setColor(new Color(0,255,0,80));
         g.drawOval((int)x_pos - radius, (int)y_pos - radius, radius*2, radius*2);
    }
    public boolean isDead() { return isDead; }
    public int getX() { return (int)x_pos; }
    public int getY() { return (int)y_pos; }
}
