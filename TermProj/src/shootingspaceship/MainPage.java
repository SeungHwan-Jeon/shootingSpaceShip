package shootingspaceship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 

import javax.sound.sampled.*;
import java.io.*;


public class MainPage extends JPanel {
    private AppFrame parentFrame;
    private Clip bgmClip; // 브금
    private FloatControl volumeControl; // 볼륨 조절용
    private JSlider volumeSlider; // 슬라이더
   
    // 생성자에서 AppFrame을 전달받음!
    public MainPage(AppFrame frame) {
        this.parentFrame = frame;

        // ===== BGM 재생 =====
        try {
            InputStream audioSrc = getClass().getResourceAsStream("/bgm/mainPageBGM.wav");
            if (audioSrc == null) {
                System.out.println("브금 파일을 찾을 수 없습니다.");
            } else {
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(audioIn);
                // 볼륨 컨트롤 얻기
                if (bgmClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    volumeControl = (FloatControl) bgmClip.getControl(FloatControl.Type.MASTER_GAIN);
                }
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY); // 무한 반복
            }
        } catch (Exception e) {
            System.out.println("브금 재생 실패: " + e.getMessage());
        }
        // ===== BGM 끝 =====
        
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        JLabel titleLabel = new JLabel("Shooting Spaceship");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));

        JButton startBtn = new JButton("게임 시작");
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(startBtn);

        startBtn.addActionListener(e -> {
            stopBGM();
            parentFrame.showStage();
        });

        JButton exitBtn = new JButton("게임 종료");
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(exitBtn);
        exitBtn.addActionListener(e -> System.exit(0));

        // ===== 소리 조절 슬라이더 (오른쪽 하단에 배치) =====
        // 슬라이더 범위 : 0(작음) ~ 100(큼)
        int sliderMin = 0;
        int sliderMax = 100;
        int sliderInit = 50; // 초기 값 50
        volumeSlider = new JSlider(JSlider.HORIZONTAL, sliderMin, sliderMax, sliderInit);

        // 실제 볼륨에 반영 (볼륨 설정 함수 따로 만들기)
        setClipVolume(sliderInit);

        volumeSlider.addChangeListener(e -> {
            int value = volumeSlider.getValue();
            setClipVolume(value);
        });

        // 오른쪽 하단에 배치: 별도 패널로 감싸서 레이아웃 관리
        JPanel sliderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        sliderPanel.add(new JLabel("볼륨"));
        sliderPanel.add(volumeSlider);

        // 메인 패널 하단에 슬라이더 추가
        add(Box.createVerticalGlue());
        add(sliderPanel);
    }

    // 볼륨 조절 메서드
    private void setClipVolume(int value) {
        if (volumeControl != null) {
            // FloatControl의 범위: min(작음, 일반적으로 -80.0f) ~ max(큼, 일반적으로 6.0f)
            float min = volumeControl.getMinimum(); // 대개 -80.0
            float max = volumeControl.getMaximum(); // 대개 6.0
            // 0~100값을 dB로 변환
            float dB = min + (max - min) * (value / 100.0f);
            volumeControl.setValue(dB);
        }
    }

    public void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
        }
    }
}
