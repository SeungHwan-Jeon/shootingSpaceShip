package shootingspaceship;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 플레이어 우주선 클래스
 * 좌우 이동이 가능하고, 총알(Shot)을 발사할 수 있음.
 */

public class Player {
    private int x_pos; // 플레이어의 x 좌표
    private int y_pos; // 플레이어의 y 좌표
    private int min_x; // 좌측 이동 제한 (경계)
    private int max_x; // 우측 이동 제한 (경계)
    private int shipType = 0; // 현재 선택된 우주선 타입 (0 ~ 2)
    public static Image[] shipImages = new Image[3];
    
    // static 초기화 블록에서 이미지 한 번만 로딩
    static {
        try {
            shipImages[0] = ImageIO.read(Player.class.getResource("/img/spaceship/spaceship1.png"));
            shipImages[1] = ImageIO.read(Player.class.getResource("/img/spaceship/spaceship2.png"));
            shipImages[2] = ImageIO.read(Player.class.getResource("/img/spaceship/spaceship3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // 생성자: 위치 및 경계값 초기화
    public Player(int x, int y, int min_x, int max_x) {
        x_pos = x;
        y_pos = y;
        this.min_x = min_x;
        this.max_x = max_x;
    }

    // 좌우 이동 처리 (왼쪽: 음수, 오른쪽: 양수)
    public void moveX(int speed) {
        x_pos += speed;
        
        // 화면 경계에 도달하면 위치 고정
        if( x_pos < min_x) x_pos = min_x;
        if( x_pos > max_x) x_pos = max_x;
    }

    // 현재 x 좌표 반환
    public int getX() {
        return x_pos;
    }

    // 현재 y 좌표 반환
    public int getY() {
        return y_pos;
    }

    // 현재 위치에서 Shot(총알) 생성
    public Shot generateShot(int missileLevel) {
        return new Shot(x_pos, y_pos, missileLevel);
    }

    
    // 플레이어 그리기
    public void drawPlayer(Graphics g) {
        Image img = shipImages[shipType];
        if (img != null) {
            g.drawImage(img, x_pos - 32, y_pos - 32, 64, 64, null);  // 고정 크기 출력
        } else {
            // 이미지 없으면 기본 도형으로 대체
            g.setColor(Color.red);
            int[] x_poly = {x_pos, x_pos - 10, x_pos, x_pos + 10};
            int[] y_poly = {y_pos, y_pos + 15, y_pos + 10, y_pos + 15};
            g.fillPolygon(x_poly, y_poly, 4);
        }
    }
    
 // shipType 변경 메서드
    public void setShipType(int type) {
    	this.shipType = type % 3; // 0~2 범위로 제한
    }

    // 현재 shipType 반환
    public int getShipType() {
    	return this.shipType;
    }
}