package sheepfarm;

import java.awt.image.BufferedImage;

public class Grass extends Plant {

    public Grass(double x, double y, int size, BufferedImage sprite) {
        super(x, y, size, 10, sprite); // foodValue of 10

        // tweak physics if needed
        this.springStrength = 0.1;
        this.dampening = 0.8;
        this.pushForce = 0.3;
    }

}
