package shootingspaceship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShipSelectDialog extends JDialog {
    private AppFrame parentFrame;
    private int selectedType;
    private boolean[] unlocked;
    private JRadioButton[] radioButtons = new JRadioButton[3];
    private JButton[] buyButtons = new JButton[3];
    private JLabel[] priceLabels = new JLabel[3];

    // 골드 관련 변수 (나중에 연동)
    private int[] prices = {0, 0, 0}; // 0골드로 일단 설정, 나중에 변수만 조정하면 됨

    public ShipSelectDialog(AppFrame parent) {
        super(parent, "우주선 선택", true);
        this.parentFrame = parent;
        this.selectedType = parent.getSelectedShipType();
        this.unlocked = parent.getUnlockedShips();

        setLayout(new BorderLayout());
        JPanel shipsPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        ButtonGroup group = new ButtonGroup();

        for (int i = 0; i < 3; ++i) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ImageIcon icon = new ImageIcon(Player.shipImages[i].getScaledInstance(64, 64, Image.SCALE_SMOOTH));
            JLabel iconLabel = new JLabel(icon);

            radioButtons[i] = new JRadioButton("우주선 " + (i+1));
            radioButtons[i].setEnabled(unlocked[i]);
            radioButtons[i].setSelected(i == selectedType);

            int idx = i; // for lambda
            radioButtons[i].addActionListener(e -> selectedType = idx);

            group.add(radioButtons[i]);

            priceLabels[i] = new JLabel(unlocked[i] ? "구매완료" : "가격: " + prices[i] + " 골드");
            buyButtons[i] = new JButton("구매");
            buyButtons[i].setEnabled(!unlocked[i]);
            buyButtons[i].addActionListener(e -> {
                unlocked[idx] = true;
                parent.unlockShip(idx);
                priceLabels[idx].setText("구매완료");
                buyButtons[idx].setEnabled(false);
                radioButtons[idx].setEnabled(true);
                radioButtons[idx].setSelected(true);
                selectedType = idx;
            });

            panel.add(iconLabel);
            panel.add(radioButtons[i]);
            panel.add(priceLabels[i]);
            panel.add(buyButtons[i]);
            shipsPanel.add(panel);
        }

        add(shipsPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton selectBtn = new JButton("선택 완료");
        selectBtn.addActionListener(e -> {
            parentFrame.setSelectedShipType(selectedType);
            dispose();
        });
        btnPanel.add(selectBtn);

        JButton cancelBtn = new JButton("취소");
        cancelBtn.addActionListener(e -> dispose());
        btnPanel.add(cancelBtn);

        add(btnPanel, BorderLayout.SOUTH);

        setSize(430, 340);
        setLocationRelativeTo(parent);
    }
}
