package shootingspaceship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DesignSelectDialog extends JDialog {
    private AppFrame parentFrame;
    private JLabel goldLabel; // 골드 표시 라벨
    
    // (1) 우주선
    private int selectedShipType;
    private boolean[] unlockedShips;
    private JRadioButton[] shipRadioButtons = new JRadioButton[3];
    private JButton[] shipBuyButtons = new JButton[3];
    private JLabel[] shipPriceLabels = new JLabel[3];
    private int[] shipPrices = {0, 1, 0};

    // (2) 미사일
    private int selectedMissileType;
    private boolean[] unlockedMissiles;
    private JRadioButton[] missileRadioButtons = new JRadioButton[3];
    private JButton[] missileBuyButtons = new JButton[3];
    private JLabel[] missilePriceLabels = new JLabel[3];
    private int[] missilePrices = {0, 0, 0};

    public DesignSelectDialog(AppFrame parent) {
        super(parent, "디자인 선택", true);
        this.parentFrame = parent;
        this.selectedShipType = parent.getSelectedShipType();
        this.unlockedShips = parent.getUnlockedShips();
        this.selectedMissileType = parent.getSelectedMissileType();
        this.unlockedMissiles = parent.getUnlockedMissiles();

        setLayout(new BorderLayout());
        // 탭 위에 골드 표시 라벨
        goldLabel = new JLabel("내 골드: " + parentFrame.getGold() + " G");
        goldLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel goldPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        goldPanel.add(goldLabel);
        add(goldPanel, BorderLayout.NORTH);
        JTabbedPane tabPane = new JTabbedPane();

        // 우주선 선택 탭
        tabPane.addTab("우주선", createShipPanel());
        // 미사일 선택 탭
        tabPane.addTab("미사일", createMissilePanel());

        setLayout(new BorderLayout());
        add(tabPane, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton selectBtn = new JButton("선택 완료");
        selectBtn.addActionListener(e -> {
            parentFrame.setSelectedShipType(selectedShipType);
            parentFrame.setSelectedMissileType(selectedMissileType);
            
            // 메인페이지 골드 갱신
            if (parentFrame.getContentPane() instanceof MainPage) {
                ((MainPage)parentFrame.getContentPane()).updateGoldDisplay();
            }
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
    
    private JPanel createShipPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 3; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ImageIcon icon = new ImageIcon(Player.shipImages[i].getScaledInstance(64, 64, Image.SCALE_SMOOTH));
            JLabel iconLabel = new JLabel(icon);

            shipRadioButtons[i] = new JRadioButton("우주선 " + (i+1));
            shipRadioButtons[i].setEnabled(unlockedShips[i]);
            shipRadioButtons[i].setSelected(i == selectedShipType);

            int idx = i;
            shipRadioButtons[i].addActionListener(e -> selectedShipType = idx);
            group.add(shipRadioButtons[i]);

            shipPriceLabels[i] = new JLabel(unlockedShips[i] ? "구매완료" : "가격: " + shipPrices[i] + " 골드");
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
                    updateGoldDisplay();
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

    private JPanel createMissilePanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 3; i++) {
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

    // 골드 라벨 갱신
    private void updateGoldDisplay() {
        goldLabel.setText("My gold: " + parentFrame.getGold() + " G");
    }

}
