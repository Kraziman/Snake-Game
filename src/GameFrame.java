import javax.swing.*;
import java.net.URL;

public class GameFrame extends JFrame {
    URL iconURL = getClass().getResource("resources/icon.png");

    GameFrame(){
        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setIconImage(new ImageIcon(iconURL).getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }
}
