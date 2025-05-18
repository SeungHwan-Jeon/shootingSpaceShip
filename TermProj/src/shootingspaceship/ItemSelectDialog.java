package shootingspaceship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * 플레이어가 게임 시작 전에 사용할 아이템 선택할 수 있는 팝업창
 * 아이템마다 그림, 설명 버튼 젝오
 * 선택 결과 AppFrame 등 메인 게임에 저장*/

public class ItemSelectDialog extends JDialog {
    private AppFrame parentFrame;
    private int selectedItemIdx = -1;
    private ItemStorage itemStorage = new ItemStorage();

    public ItemSelectDialog(AppFrame parent) {
        super(parent, "아이템 선택", true);
        this.parentFrame = parent;
        setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(1, itemStorage.getStorage().size(), 10, 10));
        ButtonGroup group = new ButtonGroup();
        JRadioButton[] radios = new JRadioButton[itemStorage.getStorage().size()];

        for (int i = 0; i < itemStorage.getStorage().size(); ++i) {
            JPanel col = new JPanel(new BorderLayout());
            Item item = itemStorage.getStorage().get(i);
            JLabel imgLabel = new JLabel(item.getImage());
            radios[i] = new JRadioButton(item.getName());
            int idx = i;
            radios[i].addActionListener(e -> selectedItemIdx = idx);
            group.add(radios[i]);
            col.add(imgLabel, BorderLayout.CENTER);
            col.add(radios[i], BorderLayout.SOUTH);
            grid.add(col);
        }

        JButton selectBtn = new JButton("선택 완료");
        selectBtn.addActionListener(e -> {
            if (selectedItemIdx >= 0) {
                parentFrame.setSelectedItem(itemStorage.getStorage().get(selectedItemIdx));
                dispose();
            }
        });

        add(new JLabel("아이템을 선택하세요", SwingConstants.CENTER), BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(selectBtn, BorderLayout.SOUTH);

        setSize(420, 280);
        setLocationRelativeTo(parent);
    }
}
