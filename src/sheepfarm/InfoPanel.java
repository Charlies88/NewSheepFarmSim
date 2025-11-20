package sheepfarm;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JLabel infoLabel;

    public InfoPanel() {
        setPreferredSize(new Dimension(200, 600)); // fixed width panel
        setBackground(new Color(50, 50, 50));
        infoLabel = new JLabel("<html>Click an object<br>to see info</html>");
        infoLabel.setForeground(Color.WHITE);
        add(infoLabel);
    }

    public void updateInfo(GameObject obj) {
        if (obj == null) {
            infoLabel.setText("<html>Click an object<br>to see info</html>");
            return;
        }

        StringBuilder sb = new StringBuilder("<html>");
        sb.append("Type: ").append(obj.getClass().getSimpleName()).append("<br>");
        sb.append("Position: ").append(String.format("(%.1f, %.1f)", obj.pos.x, obj.pos.y)).append("<br>");
        sb.append("Size: ").append(obj.size).append("<br>");

        if (obj instanceof Animal a) {
            sb.append("Health: ").append(a.health).append("<br>");
            sb.append("Alive: ").append(a.isAlive).append("<br>");
        }

        if (obj instanceof Plant p) {
            sb.append("Growth: ").append(String.format("%.1f", p.getGrowth())).append("<br>");
        }

        sb.append("</html>");
        infoLabel.setText(sb.toString());
    }
}
