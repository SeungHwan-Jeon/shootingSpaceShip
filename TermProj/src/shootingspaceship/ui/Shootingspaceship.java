package shootingspaceship.ui;

import shootingspaceship.model.BossEnemy;
import shootingspaceship.model.Enemy;
import shootingspaceship.model.EnemyBullet;
import shootingspaceship.model.Player;
import shootingspaceship.model.Score;
import shootingspaceship.model.Shot;
import shootingspaceship.model.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

/*
 * Shootingspaceship 클래스
 * 실제 게임 화면
 * 플레이어, 적, 미사일, 보스, 적 총알, HUD
 * 모든 요소 관리하고 화면에 그림
 * Runnable(스레드)로 무한 반복 실행*/

public class Shootingspaceship extends JPanel implements Runnable {
	// 전체 객체 및 상태 저장
	private AppFrame parentFrame;
	private Thread th;
	private Player player;
	private Shot[] shots;
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyBullet> enemyBullets;

	// 배경
	private int backgroundY = 0; // 배경의 시작 Y위치 (0 또는 -getHeight())
	private int backgroundSpeed = 1; // 배경이 올라가는 속도(1~2 추천)

	// 게임 설정
	private final int shotSpeed = -2;
	private final int playerLeftSpeed = -2;
	private final int playerRightSpeed = 2;
	private final int width = 500;
	private final int height = 500;
	private final int playerMargin = 10;
	private int maxShotNum = 20;
	private final int maxEnemySize = 10;
	private Random rand;

	// 적 생성 관련
	private int spawned = 0;
	private int totalToSpawn;
	private Timer enemySpawnTimer;
	private boolean bossSpawned = false;

	// HUD
	private Score score;
	private int stageGold = 0;
	private int life = 3;
	private int missileLevel;

	// 배경 이미지
	private Image[] backgrounds = { new ImageIcon(getClass().getResource("/img/desert.png")).getImage(),
			new ImageIcon(getClass().getResource("/img/forest.png")).getImage(),
			new ImageIcon(getClass().getResource("/img/sea.png")).getImage(),
			new ImageIcon(getClass().getResource("/img/tundra.png")).getImage() };
	private int selectedStage;

	// 스테이지 시스템 (난이도, 적 수 등)
	private Stage[] stages;
	private int currentStageIdx;
	private Stage currentStage;

	public Shootingspaceship(AppFrame frame, int selectedShipType, int selectedMissileType) {
		this.parentFrame = frame;
		setBackground(Color.black);
		setPreferredSize(new Dimension(width, height));

		// 플레이어 우주선 생성
		player = new Player(width / 2, (int) (height * 0.9), playerMargin, width - playerMargin);
		player.setShipType(selectedShipType);
		missileLevel = selectedMissileType;

		// 선택된 스테이지 번호 불러오기
		this.selectedStage = frame.getSelectedStage();

		// 총알, 적, 적총알 배열 초기화
		shots = new Shot[maxShotNum];
		enemies = new ArrayList<>();
		enemyBullets = new ArrayList<>();
		rand = new Random();

		// HUD 초기화
		score = new Score();
		stageGold = 0;
		life = 3;

		// Stage(난이도) 세팅 (적HP, 적speed, 마리 수, 보스여부, 보스HP, 보스speed)
		stages = new Stage[] { new Stage(1, 2, 10, false, 0, 0), // 0: 사막
				new Stage(2, 3, 15, true, 10, 2), // 1: 숲
				new Stage(3, 4, 20, true, 15, 3), // 2: 바다 (예시)
				new Stage(4, 5, 30, true, 30, 4) // 3: 툰드라 (예시)
		};
		currentStageIdx = selectedStage;
		currentStage = stages[currentStageIdx];

		totalToSpawn = currentStage.getMaxEnemies();
		spawned = 0;
		bossSpawned = false;
		startEnemySpawnTimer();

		addKeyListener(new ShipControl());
		setFocusable(true);
		SwingUtilities.invokeLater(() -> requestFocusInWindow());
	}

	private void startEnemySpawnTimer() {
		enemies.clear();
		if (enemySpawnTimer != null)
			enemySpawnTimer.stop();
		spawned = 0;
		bossSpawned = false;
		enemySpawnTimer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (spawned < totalToSpawn) {
					int x = rand.nextInt(width - 40) + 20;
					int y = 0;
					enemies.add(new Enemy(x, y, 0, currentStage.getEnemySpeed(), width, height, 0,
							currentStage.getEnemyHp()));
					spawned++;
					// 다음 등장까지 랜덤
					enemySpawnTimer.setDelay(400 + rand.nextInt(800));
				} else if (currentStage.hasBoss() && !bossSpawned) {
					enemies.add(new BossEnemy(width / 2, 0, currentStage.getBossHp(), currentStage.getBossSpeed()));
					bossSpawned = true;
					enemySpawnTimer.stop();
				} else {
					enemySpawnTimer.stop();
				}
			}
		});
		enemySpawnTimer.setInitialDelay(0);
		enemySpawnTimer.start();
	}

	public void start() {
		th = new Thread(this);
		th.start();
	}

	// 키보드 입력 처리
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
				// 미사일 아이템 구분 (싱글,더블 샷)
				String itemName = parentFrame.getSelectedItem() != null ? parentFrame.getSelectedItem().getName()
						: "item_defaultMissle";
				if (itemName.equals("item_doubleMissle")) {
					Shot[] doubleShot = player.doubleShot(missileLevel);
					for (int i = 0; i < shots.length - 1; ++i) {
						if (shots[i] == null && shots[i + 1] == null) {
							shots[i] = doubleShot[0];
							shots[i + 1] = doubleShot[1];
							break;
						}
					}
				} else {
					for (int i = 0; i < shots.length; ++i) {
						if (shots[i] == null) {
							shots[i] = player.generateShot(missileLevel);
							break;
						}
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

	private boolean playerMoveLeft = false;
	private boolean playerMoveRight = false;

	// 메인 게임 루프
	public void run() {
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		while (true) {

			// 모든 내 총알 이동(위로), 화면 벗어나면 제거
			for (int i = 0; i < shots.length; ++i) {
				if (shots[i] != null) {
					shots[i].moveShot(shotSpeed);
					if (shots[i].getY() < 0)
						shots[i] = null;
				}
			}
			if (playerMoveLeft)
				player.moveX(playerLeftSpeed);
			else if (playerMoveRight)
				player.moveX(playerRightSpeed);

			// 적/보스 이동 & 보스 총알 생성
			for (Enemy enemy : new ArrayList<>(enemies)) {
				if (enemy instanceof BossEnemy) {
					((BossEnemy) enemy).update(enemyBullets);
				} else {
					enemy.move();
				}
			}
			// 적 총알 이동+충돌
			for (Iterator<EnemyBullet> it = enemyBullets.iterator(); it.hasNext();) {
				EnemyBullet b = it.next();
				b.move();
				if (b.checkCollision(player) || !b.isAlive())
					it.remove();
			}
			// 적-총알, 적-플레이어 충돌
			Iterator<Enemy> enemyList = enemies.iterator();
			while (enemyList.hasNext()) {
				Enemy enemy = enemyList.next();
				// 적이 내 총알에 맞았는가
				if (enemy.isCollidedWithShot(shots)) {
					if (enemy.isDead()) {
						score.increaseScore();
						stageGold++;
						enemyList.remove();
						continue;
					}
				}
				// 적이 나와 충돌했는가
				if (enemy.isCollidedWithPlayer(player)) {
					enemyList.remove();
					life--;
					if (life <= 0 || player.isDead()) {
						endStageAndReturn("Game Over!");
						return;
					}
				}
			}

			backgroundY += backgroundSpeed;
			if (backgroundY >= getHeight()) {
				backgroundY = 0; // 한 화면을 다 지나가면 처음으로
			}

			repaint();

			// 스테이지 클리어
			if (checkStageClear()) {
				endStageAndReturn("스테이지 클리어! 메인화면으로 돌아갑니다.");
				return;
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException ex) {
			}
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	// 스테이지 클리어 조건
	private boolean checkStageClear() {
		boolean allSpawned = (spawned >= totalToSpawn) && (!currentStage.hasBoss() || bossSpawned);
		return allSpawned && enemies.isEmpty();
	}

	// 스테이지 종료, 메인화면 복귀, 골드 적립
	private void endStageAndReturn(String message) {
		parentFrame.addGold(stageGold);
		JOptionPane.showMessageDialog(this, message + "\n획득 골드: " + stageGold);
		parentFrame.showMainPage();
	}

	// 화면 그리기
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(backgrounds[selectedStage], 0, backgroundY, getWidth(), getHeight(), null);
		g.drawImage(backgrounds[selectedStage], 0, backgroundY - getHeight(), getWidth(), getHeight(), null);

//        g.drawImage(backgrounds[selectedStage], 0, 0, getWidth(), getHeight(), null);

		player.drawPlayer(g);
		for (Enemy enemy : enemies)
			enemy.draw(g);
		for (EnemyBullet b : enemyBullets)
			b.draw(g);
		for (int i = 0; i < shots.length; ++i)
			if (shots[i] != null)
				shots[i].drawShot(g);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.setColor(Color.WHITE);
		g.drawString("Score: " + score.getScore(), 10, 22);
		g.setColor(Color.YELLOW);
		g.drawString("Gold(스테이지): " + stageGold, 10, 42);
		g.setColor(Color.PINK);
		g.drawString("Life: " + life, 10, 62);
	}
}
