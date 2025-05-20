package shootingspaceship.model;

import java.awt.*;
import java.awt.Image;
import javax.swing.ImageIcon;

/*
 * 플레이어가 발사하는 총알 객체
 * 한 번 발사되면 위로 이동, 적에 맞으면 사라짐
 * 각 미사일(스킨) 마다 디자인이 다름*/

public class Shot {
    private int x_pos, y_pos; // 위치
    private boolean alive; // 총알이 살아있는지 여부
    private int level; // 현재 미사일 스킨 인덱스
    
    private static Image[] missileImages = new Image[3];
    static {
        missileImages[0] = new ImageIcon(Shot.class.getResource("/img/shot/shot1.png")).getImage();
        missileImages[1] = new ImageIcon(Shot.class.getResource("/img/shot/shot2.png")).getImage();
        missileImages[2] = new ImageIcon(Shot.class.getResource("/img/shot/shot3.png")).getImage();
    }
    
    public static Image getMissileImage(int level) { return missileImages[level]; }

    // 총알이 처음 만들어 질 때
    public Shot(int x, int y, int level) {
        x_pos = x; y_pos = y; alive = true; this.level = level;
    }
    
    public int getY() { return y_pos; }
    public int getX() { return x_pos; }
    
    // 총알 이동
    public void moveShot(int speed) { y_pos += speed; }
    
    // 총알 그리기
    public void drawShot(Graphics g) {
        if (!alive) return; // 이미 맞아서 죽은 총알 그리지 않음
        Image img = missileImages[level];
        if (img != null) g.drawImage(img, x_pos - 8, y_pos - 16, 16, 32, null);
    }
    public void collided() { alive = false; }
    public boolean isAlive() { return alive; }
}
