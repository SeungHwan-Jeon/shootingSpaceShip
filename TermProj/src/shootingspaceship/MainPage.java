package shootingspaceship;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/* 메인 메뉴 
 * - BGM 자동 재생 & 볼륨 조절
 * - 게임 시작 / 아이템 선택 / 스테이지 선택 / 종료*/

public class MainPage extends JPanel {
    private AppFrame parentFrame;
    private Clip bgmClip; // 브금
    private FloatControl volumeControl; // 볼륨 조절용
    private JSlider volumeSlider; // 슬라이더
   
    // 생성자에서 AppFrame을 전달받음
    public MainPage(AppFrame frame) {
        this.parentFrame = frame;
        
        // 기본 레이아웃 설정
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        // 1) BGM 로드 & 무한 반복
        try {
            AudioInputStream in = AudioSystem.getAudioInputStream(
                getClass().getResource("/bgm/mainPageBGM.wav"));
            bgmClip = AudioSystem.getClip();
            bgmClip.open(in);
            if (bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = 
                  (FloatControl)bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
            }
            bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("BGM 재생 실패: "+e.getMessage());
        }
        // BGM 끝
        
        // 2) 메인페이지 틀
        JLabel title = new JLabel("Shooting Spaceship");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createRigidArea(new Dimension(0,30)));

        // 3) 버튼들
        addButton("게임 시작", e -> { stopBGM(); parentFrame.showStage(); });
        addButton("디자인 선택", e -> parentFrame.showDesignSelectDialog());
        addButton("게임 종료", e -> System.exit(0));


        // 4) 볼륨 슬라이더
        add(Box.createVerticalGlue());
        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sliderPanel.add(new JLabel("볼륨"));
        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setClipVolume(volumeSlider.getValue());
            }
        });
        sliderPanel.add(volumeSlider);
        add(sliderPanel);
        setClipVolume(50);  // 초기값 50%
    }

    // 버튼 추가 메소드
    private void addButton(String text, ActionListener al) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(al);
        add(btn);
        add(Box.createRigidArea(new Dimension(0,10)));
    }
    
    // BGM 중지
    private void stopBGM() {
        if (bgmClip!=null && bgmClip.isRunning()) bgmClip.stop();
    }
    
    // 볼륨 세팅(0~100)
    private void setClipVolume(int value) {
        if (volumeControl==null) return;
        float min = volumeControl.getMinimum(), max = volumeControl.getMaximum();
        float dB = min + (max-min)*(value/100f);
        volumeControl.setValue(dB);
    }
   
}
