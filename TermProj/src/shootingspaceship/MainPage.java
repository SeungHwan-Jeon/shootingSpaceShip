package shootingspaceship;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/* 메뉴(JPanel)
 * 게임 시작, 디자인/스테이지/아이템 선택, 골드 표시, BGM 등 기타 설정 담당
 * 모든 버튼은 AppFrame의 showXXX() 메서드 불러 화면 바꿈 */

public class MainPage extends JPanel {
    private AppFrame parentFrame; // 상위 AppFrame (모든 상태 공유)
    private Clip bgmClip; // BGM 재생용
    private FloatControl volumeControl;
    private JSlider volumeSlider;
    private JLabel goldLabel; // 내 골드 보여주는 라벨

    public MainPage(AppFrame frame) {
        this.parentFrame = frame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // 현재 골드를 항상 보여줌 (AppFrame 에서 값 가져옴)
        goldLabel = new JLabel("My gold: " + parentFrame.getGold() + " G");
        goldLabel.setFont(new Font("Arial", Font.BOLD, 18));
        goldLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(goldLabel);
        add(Box.createRigidArea(new Dimension(0, 8)));

        // BGM 자동재생 & 볼륨 조절
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(getClass().getResource("/bgm/mainPageBGM.wav"));
            bgmClip = AudioSystem.getClip();
            bgmClip.open(in);
            if (bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl)bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
            }
            setClipVolume(50); // 초기 값 50%
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("BGM 재생 실패: " + e.getMessage());
        }

        // 게임 타이틀
        JLabel title = new JLabel("Shooting Spaceship");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createRigidArea(new Dimension(0, 30)));

        // 각 버튼 추가
        addButton("게임 시작", e -> { stopBGM(); parentFrame.showStage(); });
        addButton("디자인 선택", e -> parentFrame.showDesignSelectDialog());
        addButton("스테이지 선택", e -> parentFrame.showStageSelectDialog());
        addButton("아이템 선택", e -> parentFrame.showItemSelectDialog());
        addButton("게임 종료", e -> System.exit(0));

        // 볼륨 조절 슬라이더(JSlider)
        add(Box.createVerticalGlue());
        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sliderPanel.add(new JLabel("볼륨"));
        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.addChangeListener(e -> setClipVolume(volumeSlider.getValue()));
        sliderPanel.add(volumeSlider);
        add(sliderPanel);
        setClipVolume(50);
    }

    // 볼륨 추가 도우미
    private void addButton(String text, ActionListener al) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(al);
        add(btn);
        add(Box.createRigidArea(new Dimension(0,10)));
    }

    private void stopBGM() {
        if (bgmClip!=null && bgmClip.isRunning()) bgmClip.stop();
    }
    
    // 슬라이더 값을 진짜 컴퓨터 음량으로 바꿈 (0~100에 따른 소리)
    private void setClipVolume(int value) {
        if (volumeControl==null) return;
        float min = volumeControl.getMinimum(), max = volumeControl.getMaximum();
        float dB = min + (max-min)*(value/100f);
        volumeControl.setValue(dB);
    }
}
