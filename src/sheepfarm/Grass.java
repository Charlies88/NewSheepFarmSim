package sheepfarm;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Grass extends Plant {
    private BufferedImage sprite;

    public Grass(double x, double y, int size, BufferedImage sprite) {
        super(x, y, size);
        this.sprite = sprite;
    }

    @Override
    public void render(Graphics2D g) {
        if (sprite != null) {
            g.drawImage(sprite, (int)(pos.x - size/2), (int)(pos.y - size/2), size, size, null);
        } else {
            // fallback rectangle if no sprite
            g.setColor(new Color(60, 180, 75));
            g.fillRect((int)(pos.x - size/2), (int)(pos.y - size/2), size, size);
        }
    }

}
