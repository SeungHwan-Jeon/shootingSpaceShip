package shootingspaceship; 

public class Launcher {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new shootingspaceship.ui.AppFrame().setVisible(true);
        });
    }
}
