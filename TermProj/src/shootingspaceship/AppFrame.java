package shootingspaceship;

import javax.swing.*;

/* 프로그램 모든 화면 전환을 관리하는 중앙 컨트롤러 역할 */

public class AppFrame extends JFrame {
	private int selectedShipType = 0; // 선택된 우주선
	private boolean[] unlockedShips = {true, false, false}; // 각 우주선 잠금 여부 (0번만 기본 해금)
	
    private int selectedMissileType = 0;          // 선택된 미사일
    private boolean[] unlockedMissiles = {true, false, false};
	
	public AppFrame() {
		// 창의 기본 정보 (제목, 크기, 배치 등)
		setTitle("Shooting Spaceship");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(530,530);
		setLocationRelativeTo(null); 
		
		showMainPage();
	}
	
    // 선택/잠금 상태 getter/setter
    public int getSelectedShipType() { return selectedShipType; }
    public void setSelectedShipType(int type) { this.selectedShipType = type; }
    public boolean[] getUnlockedShips() { return unlockedShips; }
    public void unlockShip(int type) { unlockedShips[type] = true; }

    public int getSelectedMissileType() { return selectedMissileType; }
    public void setSelectedMissileType(int type) { this.selectedMissileType = type; }
    public boolean[] getUnlockedMissiles() { return unlockedMissiles; }
    public void unlockMissile(int type) { unlockedMissiles[type] = true; }
    
	// 메인 화면으로 전환
    public void showMainPage() {
        setContentPane(new MainPage(this));
        revalidate(); repaint();
    }
    
	// 게임 화면으로 전환
    public void showStage() {
        Shootingspaceship game = new Shootingspaceship(this, selectedShipType, selectedMissileType);
        setContentPane(game);
        revalidate(); repaint();
        game.start();
        game.requestFocusInWindow();
    }
    
    
    // 탭 기반 디자인 선택 팝업
    public void showDesignSelectDialog() {
        DesignSelectDialog dialog = new DesignSelectDialog(this);
        dialog.setVisible(true);
    }
    
//    // 우주선 선택 다이얼로그
//    public void showShipSelectDialog() {
//        ShipSelectDialog dialog = new ShipSelectDialog(this);
//        dialog.setVisible(true);
//    }
	
	// 프로그램 시작 (main)
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new AppFrame().setVisible(true); // AppFrame 을 띄움
		});
	}
}
