package shootingspaceship;

import java.awt.Graphics;
import java.awt.Color;

/**
 * 적 객체 클래스
 * 적은 일정 속도로 이동하며 화면 아래로 내려오고,
 * 플레이어나 총알(Shot)과 충돌할 수 있음.
 */

public class Enemy {

    private float x_pos; // 적의 x 좌표
    private float y_pos; // 적의 y 좌표
    private float delta_x; // x 방향 속도
    private float delta_y; // y 방향 속도 (아래로 내려오는 속도)
    private int max_x; // 화면의 최대 x 좌표
    private int max_y; // 화면의 최대 y 좌표
    private float delta_y_inc; // y 속도 증가값 (충돌 후 점점 빨라짐)
    private final int collision_distance = 10; // 충돌 판정 범위 (두 객체 간 거리)
    
    // 생성자: 위치, 속도, 제한 범위 설정
    public Enemy(int x, int y, float delta_x, float delta_y, int max_x, int max_y, float delta_y_inc) {
        x_pos = x;
        y_pos = y;
        this.delta_x = delta_x;
        this.delta_y = delta_y;
        this.max_x = max_x;
        this.max_y = max_y;
        this.delta_y_inc = delta_y_inc;
    }
    
    // 적 이동 처리
    public void move() {
        x_pos += delta_x;
        y_pos += delta_y;

        // 좌우 벽에 부딪히면 방향 전환
        if (x_pos < 0) {
            x_pos = 0;
            delta_x = -delta_x;
        } else if (x_pos > max_x) {
            x_pos = max_x;
            delta_x = -delta_x;
        }
        
        // 아래로 내려가다 화면을 벗어나면 맨 위로 다시 올라감
        // 단, 내려오는 속도는 증가함
        if (y_pos > max_y) {
            y_pos = 0;
            delta_y += delta_y_inc;
        }
    }

    // 적이 Shot과 충돌했는지 확인
    public boolean isCollidedWithShot(Shot[] shots) {
        for (Shot shot : shots) {
            if (shot == null) {
                continue;
            }
            if (-collision_distance <= (y_pos - shot.getY()) && (y_pos - shot.getY() <= collision_distance)) {
                if (-collision_distance <= (x_pos - shot.getX()) && (x_pos - shot.getX() <= collision_distance)) {
                    //collided.
                	// 충돌 시 shot에게 알려주고 true 반환
                    shot.collided();
                    return true;
                }
            }
        }
        return false;
    }

    // 적이 Player와 충돌했는지 확인
    public boolean isCollidedWithPlayer(Player player) {
        if (-collision_distance <= (y_pos - player.getY()) && (y_pos - player.getY() <= collision_distance)) {
            if (-collision_distance <= (x_pos - player.getX()) && (x_pos - player.getX() <= collision_distance)) {
                //충돌 발생
                return true;
            }
        }
        return false;
    }
    
    // 적 그리기 (노란색 4각형 화살표 모양)
    public void draw(Graphics g) {
        g.setColor(Color.yellow);
        int[] x_poly = {(int) x_pos, (int) x_pos - 10, (int) x_pos, (int) x_pos + 10};
        int[] y_poly = {(int) y_pos + 15, (int) y_pos, (int) y_pos + 10, (int) y_pos};
        g.fillPolygon(x_poly, y_poly, 4);
    }
}