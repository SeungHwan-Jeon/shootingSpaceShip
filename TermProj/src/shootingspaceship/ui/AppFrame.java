package shootingspaceship.ui;

import javax.swing.*;

import shootingspaceship.model.Item;

/*
 * 프로그램 전체 대표하는 창(Frame) 
 * 여기서 현재골드 / 선택한 우주선 / 미사일 / 스테이지 / 아이템 등
 * 모든 상태와 화면전환 관리
 */

public class AppFrame extends JFrame {
	private int gold = 0; // 플레이어가 가진 골드 (총 골드 제외 이 값만 참조 / 항상 최신 유지)
	
	// 우주선 / 미사일 선택 및 잠금.구매 여부 저장 
    private int selectedShipType = 0;
    private boolean[] unlockedShips = {true, false, false};
    private int selectedMissileType = 0;
    private boolean[] unlockedMissiles = {true, false, false};
    
    // 스테이지 및 아이템 선택 저장
    private int selectedStage = 0;
    private Item selectedItem = null;

    // 프로그램 시작 시 가장 먼저 호출
    public AppFrame() {
        setTitle("Shooting Spaceship");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(530, 530);
        setLocationRelativeTo(null);
        showMainPage(); // 메인 페이지 먼저 띄움
    }

    // 골드 관련 메서드 (이걸로 골드 상태 조작)
    public int getGold() { return gold; }
    public void addGold(int amount) { gold += amount; } // 골드 획득
    public boolean spendGold(int amount) { // 골드 차감
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    // 디자인/아이템/스테이지 선택 관련
    public int getSelectedShipType() { return selectedShipType; }
    public void setSelectedShipType(int type) { this.selectedShipType = type; }
    public boolean[] getUnlockedShips() { return unlockedShips; }
    public void unlockShip(int type) { unlockedShips[type] = true; }

    public int getSelectedMissileType() { return selectedMissileType; }
    public void setSelectedMissileType(int type) { this.selectedMissileType = type; }
    public boolean[] getUnlockedMissiles() { return unlockedMissiles; }
    public void unlockMissile(int type) { unlockedMissiles[type] = true; }

    public int getSelectedStage() { return selectedStage; }
    public void setSelectedStage(int stage) { this.selectedStage = stage; }

    public Item getSelectedItem() { return selectedItem; }
    public void setSelectedItem(Item item) { this.selectedItem = item; }

    // --- 화면 전환 메서드들
    // 메인 화면
    public void showMainPage() {
        setContentPane(new MainPage(this));
        revalidate(); repaint();
    }
    
    // 게임 화면(Shootingspaceship)
    public void showStage() {
        Shootingspaceship game = new Shootingspaceship(this, selectedShipType, selectedMissileType); // 현재 선택된 우주선, 미사일, 스테이지 상태 전달
        setContentPane(game);
        revalidate(); repaint();
        game.start();
        game.requestFocusInWindow();
    }

    // 디자인 (우주선/미사일) 팝업창 띄우기
    public void showDesignSelectDialog() {
        DesignSelectDialog dialog = new DesignSelectDialog(this); // this = AppFrame
        dialog.setVisible(true);
    }

    // 스테이지 배경 선택 팝업창
    public void showStageSelectDialog() {
        StageSelectDialog dialog = new StageSelectDialog(this);
        dialog.setVisible(true);
    }

    // 아이템 선택 팝업창
    public void showItemSelectDialog() {
        ItemSelectDialog dialog = new ItemSelectDialog(this);
        dialog.setVisible(true);
    }
}
