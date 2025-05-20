package shootingspaceship.model;

import java.awt.*;
import java.awt.Image;
import javax.imageio.ImageIO;

/*
 * Player 클래스
 * 실제 내 우주선의 위치, 스킨, 이동, 미사일 발사, 피격 판정 등 담당
 * 모든 내 우주선 동작의 기준이 되는 클래스 */

public class Player {
    private int x_pos, y_pos; // 내 위치
    private int min_x, max_x; // 좌우 이동 제한 (화면 경계)
    private int shipType = 0; // 현재 선택된 우주선 스킨 인덱스
    public static Image[] shipImages = new Image[3]; // 우주선 스킨 이미지

    static {
        try {
            shipImages[0] = ImageIO.read(Player.class.getResource("/img/spaceship/spaceship1.png"));
            shipImages[1] = ImageIO.read(Player.class.getResource("/img/spaceship/spaceship2.png"));
            shipImages[2] = ImageIO.read(Player.class.getResource("/img/spaceship/spaceship3.png"));
        } catch (Exception e) { e.printStackTrace(); }
    }

    // 게임 시작 시 플레이어 항상 아래쪽 중앙에 배치
    public Player(int x, int y, int min_x, int max_x) {
        x_pos = x; y_pos = y; this.min_x = min_x; this.max_x = max_x;
    }
    
    // 우주선 스킨 변경 및 조회 함수
    public void setShipType(int type) { this.shipType = type % 3; } // 잘못된 값 입력 방지. 스킨 수 늘면 조정 필요
    public int getShipType() { return this.shipType; }
    
    // 좌우 이동
    public void moveX(int speed) {
        x_pos += speed;
        if( x_pos < min_x) x_pos = min_x;
        if( x_pos > max_x) x_pos = max_x;
    }
    
    public int getX() { return x_pos; }
    public int getY() { return y_pos; }

    // 미사일 발사 : 현재 내 위치에서 지정한 미자일 스킨으로 Shot 생성
    public Shot generateShot(int missileLevel) { return new Shot(x_pos, y_pos, missileLevel); }
    // 더블 샷 기능 (2발 동시)
    public Shot[] doubleShot(int missileLevel) {
        return new Shot[] {
            new Shot(x_pos - 8, y_pos, missileLevel),
            new Shot(x_pos + 8, y_pos, missileLevel)
        };
    }

    // 실제로 그릴 때 (paintComponent 에서 호출)
    public void drawPlayer(Graphics g) {
        Image img = shipImages[shipType];
        if (img != null) {
            g.drawImage(img, x_pos - 32, y_pos - 32, 64, 64, null);
        } else {
            g.setColor(Color.red);
            int[] x_poly = {x_pos, x_pos - 10, x_pos, x_pos + 10};
            int[] y_poly = {y_pos, y_pos + 15, y_pos + 10, y_pos + 15};
            g.fillPolygon(x_poly, y_poly, 4);
        }
    }
    
    // 적 총알에 맞았는지 판정 (적 총알아 내 히트박스에 들어오면 true)
    public boolean isHit(int bulletX, int bulletY) {
        int playerWidth = 30, playerHeight = 30; // 히트박스 크기
        int px = this.x_pos, py = this.y_pos;
        return bulletX >= px && bulletX <= (px + playerWidth) && bulletY >= py && bulletY <= (py + playerHeight);
    }
    private boolean isDead = false;
    public void setDead() { isDead = true; }
    public boolean isDead() { return isDead; }
}
