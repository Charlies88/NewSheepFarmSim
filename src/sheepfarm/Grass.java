package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Grass extends Plant {
    private BufferedImage sprite;

    public Grass(double x, double y, int size, BufferedImage sprite) {
        super(x, y, size, 10); // foodValue of 10
        this.sprite = sprite;

        // tweak physics if you want
        this.springStrength = 0.1;
        this.dampening = 0.8;
        this.pushForce = 0.3;
    }

    @Override
    public void render(Graphics2D g) {
        // scale size by growth fraction (0–100)
        double scale = growth / 100.0;
        int drawSize = (int)(size * 2 * scale); // width/height of sprite
        int drawX = (int)(pos.x - drawSize / 2);
        int drawY = (int)(pos.y - drawSize / 2);

        g.drawImage(sprite, drawX, drawY, drawSize, drawSize, null);
    }

}
