package shootingspaceship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * 게임의 메인 컨트롤러 클래스 (JPanel + 쓰레드)
 * 플레이어, 적, 총알 객체들을 관리하고
 * 키 입력 처리 및 화면 그리기 등을 담당함.
 */

public class Shootingspaceship extends JPanel implements Runnable {
	private AppFrame parentFrame;
    private Thread th; // 스레드 관련
    
    // 주요 게임 객체
    private Player player; // 플레이어 우주선
    private Shot[] shots; // 총알 배열 (최대 maxShotNum 개)
    private ArrayList enemies;  // 적 목록
    
    // 각종 게임 설정 값 (속도 등)
    private final int shotSpeed = -2; // 총알 속도 (위로 이동하므로 음수)
    private final int playerLeftSpeed = -2;  // 왼쪽 이동 속도
    private final int playerRightSpeed = 2;  // 오른쪽 이동 속도
    
    // 게임 화면 크기
    private final int width = 500; // 화면 너비
    private final int height = 500; // 화면 높이
    
    private final int playerMargin = 10; // 플레이어가 움직일 수 있는 최소 여백
    private final int enemyMaxDownSpeed = 1; // 적의 최대 아래 속도
    private final int enemyMaxHorizonSpeed = 1; // 적의 최대 좌우 속도
    private final int enemyTimeGap = 2000; // 적 생성 간격 (ms)
    private final float enemyDownSpeedInc = 0.3f;  // 적이 다시 등장할 때 y속도 증가량
    private final int maxEnemySize = 10;  // 등장 가능한 적 최대 수
    
    private int enemySize; // 현재 적 수
    private javax.swing.Timer timer;  // 일정 시간마다 적을 생성하는 Swing Timer
    private boolean playerMoveLeft; // 왼쪽 이동 키 눌림 여부
    private boolean playerMoveRight; // 오른쪽 이동 키 눌림 여부
    
    // 일정 시간마다 적을 생성하는 Swing Timer
    private Image dbImage; // 화면에 그릴 이미지
    private Graphics dbg; // dbImage에 그림을 그릴 그래픽 도구
    
    private Random rand; // 난수 생성기 (적 생성용)
    private int maxShotNum = 20; // 최대 총알 수
    private int missileLevel; // 현재 선택된 미사일 타입(레벨)
    
    // HUD 변수
    private Score score;   // 점수
    private int stageGold = 0; // 이번 스테이지에서 번 골드
    private int life = 3;  // 남은 목숨
    
    // 생성자: 게임 초기화
    public Shootingspaceship(AppFrame frame, int selectedShipType, int selectedMissileType) {
    	this.parentFrame = frame;
    	
        setBackground(Color.black); // 배경색 설정
        setPreferredSize(new Dimension(width, height)); // 패널 크기 설정
        
        // 플레이어 초기화: 화면 아래쪽 중앙에 생성
        player = new Player(width / 2, (int) (height * 0.9), playerMargin, width-playerMargin );
        player.setShipType(selectedShipType); // 우주선 선택
        missileLevel = selectedMissileType; // 미사일 선택
        
        shots = new Shot[ maxShotNum ]; // 총알 배열 초기화 (null로 가득 찬 상태)
        enemies = new ArrayList(); // 적 리스트 초기화
        enemySize = 0;  // 적 수 초기화
        rand = new Random(1); // 난수 생성기: 항상 같은 결과를 위해 시드 고정 (디버그 용이)
        
        // HUD 객체 초기화
        score = new Score();
        stageGold = 0;
        life = 3;
        
        // 일정 시간마다 새로운 적을 생성하는 타이머 시작
        timer = new javax.swing.Timer(enemyTimeGap, new addANewEnemy()); 
        timer.start();
        
        // 키보드 입력 처리 클래스 등록
        addKeyListener(new ShipControl());
        setFocusable(true);
    }

	// 게임 루프 시작
    public void start() {
        th = new Thread(this); // Runnable 구현 클래스니까 바로 스레드 생성 가능
        th.start(); // run() 호출됨
    }

    // 내부 클래스: 일정 시간마다 적 추가
    private class addANewEnemy implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
        	// 적 개수 제한 내에서만 생성
            if (++enemySize <= maxEnemySize) {
                float downspeed;
                
                // 속도가 0이 아닌 랜덤 y속도 구하기
                do {
                    downspeed = rand.nextFloat() * enemyMaxDownSpeed;
                } while (downspeed == 0);
                
                // -1.0 ~ +1.0 범위의 x 방향 속도 구함
                float horspeed = rand.nextFloat() * 2 * enemyMaxHorizonSpeed - enemyMaxHorizonSpeed;
                //System.out.println("enemySize=" + enemySize + " downspeed=" + downspeed + " horspeed=" + horspeed);

                // 적 객체 생성 및 리스트에 추가
                Enemy newEnemy = new Enemy((int) (rand.nextFloat() * width), 0, horspeed, downspeed, width, height, enemyDownSpeedInc);
                enemies.add(newEnemy);
            } else {
            	// 최대 적 수를 초과하면 더 이상 생성하지 않음
                timer.stop();
            }
        }
    }

    // 내부 클래스: 키 입력 처리기
    private class ShipControl implements KeyListener {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerMoveLeft = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    playerMoveRight = true;
                    break;
                case KeyEvent.VK_UP:
                    // generate new shot and add it to shots array
                	// 총알을 하나 생성해 빈 공간에 추가
                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] == null) {
                            shots[i] = player.generateShot(missileLevel);
                            break;
                        }
                    }
                    break;
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerMoveLeft = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    playerMoveRight = false;
                    break;
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }
    
    // 게임 루프 (계속 반복 실행됨)
    public void run() {
        //int c=0;
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY); // 성능 조정

        while (true) {
            //System.out.println( ++c );
            // do operations on shots in shots array
        	// 총알 이동
            for (int i = 0; i < shots.length; i++) {
                if (shots[i] != null) {
                    // move shot
                    shots[i].moveShot(shotSpeed);

                    // 화면 위로 나가면 제거
                    if (shots[i].getY() < 0) {                   
                        shots[i] = null;
                    }
                }
            }

            // 플레이어 이동
            if (playerMoveLeft) {
                player.moveX(playerLeftSpeed);
            } else if (playerMoveRight) {
                player.moveX(playerRightSpeed);
            }

            // 적 이동
            Iterator enemyList = enemies.iterator();
            while (enemyList.hasNext()) {
                Enemy enemy = (Enemy) enemyList.next();
                enemy.move();

                // 총알과 충돌 → 점수+골드
                if (enemy.isCollidedWithShot(shots)) {
                    score.increaseScore();
                    stageGold++;           // ⭐ 스테이지 내 골드
                    enemyList.remove();
                    continue;
                }
                
                // 플레이어와 충돌 → 목숨 감소
                if (enemy.isCollidedWithPlayer(player)) {
                    enemyList.remove();
                    life--;
                    if (life <= 0) {
                        endStageAndReturn("Game Over!");
                        return;
                    }
                }
            }

            repaint();  // 화면 다시 그리기

            // 스테이지 클리어 예시
            if (checkStageClear()) { // 조건이 true라면
            	endStageAndReturn("스테이지 클리어! 메인화면으로 돌아갑니다.");
                return; // 게임루프 끝내기
            }
  
            
            try {
                Thread.sleep(10); // 다음 프레임까지 잠시 대기
            } catch (InterruptedException ex) {
            }  
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        }
    }
    
    // 클리어 조건
    private boolean checkStageClear() {
        // "적이 한 번 이상 등장했고, 현재 적이 0마리면 클리어"
        return enemySize > 0 && enemies.isEmpty();
    }
    

    // 스테이지 종료 후 골드 누적 반영
    private void endStageAndReturn(String message) {
        parentFrame.addGold(stageGold); // 누적!
        JOptionPane.showMessageDialog(this, message + "\n획득 골드: " + stageGold);
        parentFrame.showMainPage();
    }
    
    // 더블 버퍼링용 이미지 초기화
    public void initImage(Graphics g) {
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }

        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

        dbg.setColor(getForeground());
        //paint (dbg);

        g.drawImage(dbImage, 0, 0, this);
    }

    // 실제 게임 화면 그리기
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // double buffering 쓰지 않을 경우
        player.drawPlayer(g);

        // 적 그리기 + 충돌 검사
        // 적 그리기 + 충돌검사 (이미 위에서 처리함)
        for (Object obj : enemies) {
            Enemy enemy = (Enemy) obj;
            enemy.draw(g);
        }

        // 총알 그리기
        for (int i = 0; i < shots.length; i++) {
            if (shots[i] != null) {
                shots[i].drawShot(g);
            }
        }
        
        // HUD (점수, 골드, 라이프) 출력
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score.getScore(), 10, 22);
        g.setColor(Color.YELLOW);
        g.drawString("Gold: " + stageGold, 10, 42);
        g.setColor(Color.PINK);
        g.drawString("Life: " + life, 10, 62);
    }
}