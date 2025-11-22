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
        g.drawImage(sprite, (int)pos.x - size, (int)pos.y - size, size * 2, size * 2, null);
    }
}
