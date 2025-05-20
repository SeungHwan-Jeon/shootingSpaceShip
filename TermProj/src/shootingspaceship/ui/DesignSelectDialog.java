package shootingspaceship.ui;

import javax.swing.*;

import shootingspaceship.model.Player;
import shootingspaceship.model.Shot;

import java.awt.*;
import java.awt.event.*;

/*
 * 우주선 / 미사일 스킨 고르고 잠겨 있으면 골드로 구매하는 화면
 * JTabbedPane 으로 우주선 / 미사일 번갈아 볼 수 있음
 * 골드, 잠금/해제, 현재 선택상태 모두 연동*/

public class DesignSelectDialog extends JDialog {
    private AppFrame parentFrame; // 골드, 잠금 등 상태를 가져오려면 필요
  
    private JLabel goldLabel; // 내 골드 표시
    
    // 선택/해금 정보 임시 저장 (팝업 최종 선택 완료때 ApPFrame에 반영)
    private int selectedShipType;
    private boolean[] unlockedShips;
    private int selectedMissileType;
    private boolean[] unlockedMissiles;
    
    // 각 가격, 라디오버튼, 구매버튼 | 가격 및 종류 추가 시 수정 필요
    private int[] shipPrices = {0, 0, 30};
    private int[] missilePrices = {0, 0, 40};
    private JRadioButton[] shipRadioButtons = new JRadioButton[3];
    private JButton[] shipBuyButtons = new JButton[3];
    private JLabel[] shipPriceLabels = new JLabel[3];
    private JRadioButton[] missileRadioButtons = new JRadioButton[3];
    private JButton[] missileBuyButtons = new JButton[3];
    private JLabel[] missilePriceLabels = new JLabel[3];

    public DesignSelectDialog(AppFrame parent) {
        super(parent, "디자인 선택", true);
        this.parentFrame = parent;
        
        // AppFrame 에서 현재 선택/해금 상태 가져옴(clone을 통해 실제 선택 완료 전까지 업데이트 되지 않음)
        this.selectedShipType = parent.getSelectedShipType(); 
        this.unlockedShips = parent.getUnlockedShips().clone();
        this.selectedMissileType = parent.getSelectedMissileType();
        this.unlockedMissiles = parent.getUnlockedMissiles().clone();

        setLayout(new BorderLayout());
        
        // 내 골드 표시 
        goldLabel = new JLabel("My Gold: " + parentFrame.getGold() + " G");
        goldLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel goldPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        goldPanel.add(goldLabel);
        add(goldPanel, BorderLayout.NORTH);
        
        // 탭 생성 (우주선, 미사일)
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("우주선", createShipPanel());
        tabPane.addTab("미사일", createMissilePanel());
        add(tabPane, BorderLayout.CENTER);

        // 선택 완료/취소 버튼   이 부분도 질문
        JPanel btnPanel = new JPanel();
        JButton selectBtn = new JButton("선택 완료");
        selectBtn.addActionListener(e -> {
            parentFrame.setSelectedShipType(selectedShipType);
            parentFrame.setSelectedMissileType(selectedMissileType);
            parentFrame.getUnlockedShips()[0] = unlockedShips[0];
            parentFrame.getUnlockedShips()[1] = unlockedShips[1];
            parentFrame.getUnlockedShips()[2] = unlockedShips[2];
            parentFrame.getUnlockedMissiles()[0] = unlockedMissiles[0];
            parentFrame.getUnlockedMissiles()[1] = unlockedMissiles[1];
            parentFrame.getUnlockedMissiles()[2] = unlockedMissiles[2];
            dispose();
        });
        btnPanel.add(selectBtn);
        JButton cancelBtn = new JButton("취소");
        cancelBtn.addActionListener(e -> dispose());
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        setSize(480, 350);
        setLocationRelativeTo(parent);
    }

    // 우주선 스킨 탭 생성
    private JPanel createShipPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10)); // 우주선 늘어나면 다른 레이아웃 필요할수도?
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 3; ++i) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ImageIcon icon = new ImageIcon(Player.shipImages[i].getScaledInstance(64, 64, Image.SCALE_SMOOTH));
            JLabel iconLabel = new JLabel(icon);

            shipRadioButtons[i] = new JRadioButton("우주선 " + (i+1));
            shipRadioButtons[i].setEnabled(unlockedShips[i]);
            shipRadioButtons[i].setSelected(i == selectedShipType);

            int idx = i;
            shipRadioButtons[i].addActionListener(e -> selectedShipType = idx);
            group.add(shipRadioButtons[i]);

            // 가격 라벨 : 구매 안했으면 가격 표시
            shipPriceLabels[i] = new JLabel(unlockedShips[i] ? "구매완료" : "가격: " + shipPrices[i] + " 골드");
            
            // 구매버튼 (구매 했다면 비활성화)
            shipBuyButtons[i] = new JButton("구매");
            shipBuyButtons[i].setEnabled(!unlockedShips[i]);
            shipBuyButtons[i].addActionListener(e -> {
                int price = shipPrices[idx];
                if (parentFrame.spendGold(price)) {
                    unlockedShips[idx] = true;
                    parentFrame.unlockShip(idx);
                    shipPriceLabels[idx].setText("구매완료");
                    shipBuyButtons[idx].setEnabled(false);
                    shipRadioButtons[idx].setEnabled(true);
                    shipRadioButtons[idx].setSelected(true);
                    selectedShipType = idx;
                    updateGoldDisplay(); // 골드 갱신
                } else {
                    JOptionPane.showMessageDialog(this, "골드가 부족합니다!");
                }
            });

            row.add(iconLabel);
            row.add(shipRadioButtons[i]);
            row.add(shipPriceLabels[i]);
            row.add(shipBuyButtons[i]);
            panel.add(row);
        }
        return panel;
    }

    // 미사일 스킨 탭 생성
    private JPanel createMissilePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 3; ++i) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ImageIcon icon = new ImageIcon(Shot.getMissileImage(i).getScaledInstance(32, 32, Image.SCALE_SMOOTH));
            JLabel iconLabel = new JLabel(icon);

            missileRadioButtons[i] = new JRadioButton("미사일 " + (i+1));
            missileRadioButtons[i].setEnabled(unlockedMissiles[i]);
            missileRadioButtons[i].setSelected(i == selectedMissileType);

            int idx = i;
            missileRadioButtons[i].addActionListener(e -> selectedMissileType = idx);
            group.add(missileRadioButtons[i]);

            missilePriceLabels[i] = new JLabel(unlockedMissiles[i] ? "구매완료" : "가격: " + missilePrices[i] + " 골드");
            missileBuyButtons[i] = new JButton("구매");
            missileBuyButtons[i].setEnabled(!unlockedMissiles[i]);
            missileBuyButtons[i].addActionListener(e -> {
                int price = missilePrices[idx];
                if (parentFrame.spendGold(price)) {
                    unlockedMissiles[idx] = true;
                    parentFrame.unlockMissile(idx);
                    missilePriceLabels[idx].setText("구매완료");
                    missileBuyButtons[idx].setEnabled(false);
                    missileRadioButtons[idx].setEnabled(true);
                    missileRadioButtons[idx].setSelected(true);
                    selectedMissileType = idx;
                    updateGoldDisplay();
                } else {
                    JOptionPane.showMessageDialog(this, "골드가 부족합니다!");
                }
            });

            row.add(iconLabel);
            row.add(missileRadioButtons[i]);
            row.add(missilePriceLabels[i]);
            row.add(missileBuyButtons[i]);
            panel.add(row);
        }
        return panel;
    }

    // 골드 라밸 갱신 (구매 시 실시간으로 업데이트)
    private void updateGoldDisplay() {
        goldLabel.setText("내 골드: " + parentFrame.getGold() + " G");
    }
}
