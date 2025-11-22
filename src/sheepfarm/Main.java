package sheepfarm;

import java.awt.BorderLayout;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sheep Farm Prototype");
        Game game = new Game();  

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Layout so game + info panel sit side by side
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);

        // --- Add info panel ---
    


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();
    }
}
