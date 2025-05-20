package shootingspaceship.ui;

import javax.swing.*;
import java.awt.*;

/*
 * 사용자가 4개의 스테이지 중 선택할 수 있도록 이미지+라디오 버튼으로 팝업 제공
 * AppFrame의 setSelectStage()로 결과 저장*/

public class StageSelectDialog extends JDialog {
    private AppFrame parentFrame;
    private int selectedIdx = -1; // 사용자가 고른 스테이지 (초기값 : 0)
    private String[] thumbPaths = {
            "/img/stage_desert.png",
            "/img/stage_forest.png",
            "/img/stage_sea.png",
            "/img/stage_tundra.png"
    };
    private String[] stageNames = {"사막", "숲", "바다", "툰드라"};

    
    public StageSelectDialog(AppFrame parent) {
        super(parent, "스테이지 선택", true);
        this.parentFrame = parent;
        setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(1, 4, 10, 10));
        ButtonGroup group = new ButtonGroup();
        JRadioButton[] radios = new JRadioButton[4];

        for (int i = 0; i < 4; ++i) {
            JPanel col = new JPanel(new BorderLayout());
            ImageIcon icon = new ImageIcon(getClass().getResource(thumbPaths[i]));
            
            JLabel imgLabel = new JLabel(icon);
            radios[i] = new JRadioButton(stageNames[i]);
            
            int idx = i;
            
            radios[i].addActionListener(e -> selectedIdx = idx);
            
            group.add(radios[i]);
            
            // 이미지, 라디오 버튼
            col.add(imgLabel, BorderLayout.CENTER);
            col.add(radios[i], BorderLayout.SOUTH);
            grid.add(col);
        }

        JButton selectBtn = new JButton("선택 완료");
        selectBtn.addActionListener(e -> {
            if (selectedIdx >= 0) {
                parentFrame.setSelectedStage(selectedIdx);
                dispose();
            }
        });

        add(new JLabel("스테이지를 선택하세요", SwingConstants.CENTER), BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(selectBtn, BorderLayout.SOUTH);

        setSize(500, 400);
        setLocationRelativeTo(parent);
    }
}
