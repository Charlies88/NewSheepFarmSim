package sheepfarm;

import java.awt.*;

public class UIManager {
    Game game;
    public UIManager(Game g) { this.game = g; }
    public void update() { /* handle UI logic */ }
    public void render(Graphics2D g) { /* draw health bars, menus, etc */ }
}
