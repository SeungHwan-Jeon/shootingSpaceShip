/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package shootingspaceship;

import java.awt.Graphics;
import java.awt.Color;

/**
 * Shot 클래스
 * 플레이어가 발사하는 총알 객체.
 * 일정 속도로 위쪽으로 움직이며, 적과 충돌할 수 있음.
 */

public class Shot {

    private int x_pos; // 총알의 x 좌표
    private int y_pos; // 총알의 y 좌표
    private boolean alive; // 충돌 여부 (true면 살아있음, false면 충돌하여 제거됨)
    private final int radius = 3; // 총알의 크기 (반지름)

    // 생성자: 위치 초기화, 살아있는 상태로 시작
    public Shot(int x, int y) {
        x_pos = x;
        y_pos = y;
        alive = true;
    }
    
    // y 좌표 반환
    public int getY() {
        return y_pos;
    }

    // x 좌표 반환
    public int getX() {
        return x_pos;
    }
    
    // 총알을 위로 이동시킴 (speed는 음수)
    public void moveShot(int speed) {
        y_pos += speed;
    }

    // 총알을 그리기 (노란색 원)
    public void drawShot(Graphics g) {
        if (!alive) {
            return;
        }
        g.setColor(Color.yellow);
        g.fillOval(x_pos, y_pos, radius, radius);
    }

    // 충돌 시 alive를 false로 변경
    public void collided() {
        alive = false;
    }
}
