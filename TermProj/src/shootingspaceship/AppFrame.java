package shootingspaceship;

import javax.swing.*;

public class AppFrame extends JFrame {
	public AppFrame() {
		setTitle("Shooting Spaceship");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,600);
		setLocationRelativeTo(null); // 이거 뭔데
		
		showMainPage();
	}
	
	// 메인 메뉴 화면으로 전환
	public void showMainPage() {
		MainPage mainPanel = new MainPage(this); // this = AppFrame 자신 전달
		setContentPane(mainPanel); // 창의 내용을 메인화면으로 교체
		revalidate(); // 새로고침
		repaint();
	}
	
	// 게임(스테이지) 화면으로 전환
	public void showStage() {
		Shootingspaceship stagePanel = new Shootingspaceship(this); // AppFrame 자신을 전달
		setContentPane(stagePanel); // 창의 내용을 게임 화면으로 교체
		revalidate();
		repaint();
		stagePanel.start();
		stagePanel.requestFocusInWindow();
	}
	
	// 여기서 프로그램 시작
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new AppFrame().setVisible(true); // AppFrame 을 띄움
		});
	}
}
