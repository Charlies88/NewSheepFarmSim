package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Grass extends Plant {

    public Grass(double x, double y, int size, BufferedImage sprite) {
        super(x, y, size, 10, sprite); // foodValue of 10

        // tweak physics if needed
        this.springStrength = 0.1;
        this.dampening = 0.8;
        this.pushForce = 0.3;
    }

    public boolean isEaten() { return eaten; }


    @Override
    public void render(Graphics2D g) {
        if (eaten) return; // don’t render if eaten
        int drawSize = getCurrentSize() * 2;
        g.drawImage(sprite, (int)(pos.x - drawSize/2), (int)(pos.y - drawSize/2), drawSize, drawSize, null);
    }

    public void eat() { 
        eaten = true; 
        if (foodComponent != null) foodComponent.consume(); 
    }


    
}
