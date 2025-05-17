package shootingspaceship;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;

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
    private int level; // 미사일 레벨 (0,1,2)
    
    // 이미지 3개 저장할 배열 (static 한 번만 로드)
    private static Image[] missileImages = new Image[3];

    static {
        missileImages[0] = new ImageIcon(Shot.class.getResource("/img/shot/shot1.png")).getImage();
        missileImages[1] = new ImageIcon(Shot.class.getResource("/img/shot/shot2.png")).getImage();
        missileImages[2] = new ImageIcon(Shot.class.getResource("/img/shot/shot3.png")).getImage();
    }
    
    public static Image getMissileImage(int type) {
        return missileImages[type];
    }
    
    // 생성자: 위치 초기화, 살아있는 상태로 시작
    public Shot(int x, int y, int level) {
        x_pos = x;
        y_pos = y;
        alive = true;
        this.level = level;
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
        if (!alive) return;
        Image img = missileImages[level];
        if (img != null) {
            g.drawImage(img, x_pos - 8, y_pos - 16, 16, 32, null);  // 16x32 크기로 중심 정렬
        }
    }

    // 충돌 시 alive를 false로 변경
    public void collided() {
        alive = false;
    }
}