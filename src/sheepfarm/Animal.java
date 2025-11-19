package sheepfarm;

import java.awt.image.BufferedImage;

public abstract class Animal extends GameObject {
    public double health = 100;
    public boolean isAlive = true;
    protected BufferedImage sprite;
    protected int actionCooldown = 0;

    public Animal(double x, double y, int size, BufferedImage sprite) {
        super(x, y, size);
        this.sprite = sprite;
    }
    
    @Override
    public void update(Game g) {
        super.update(g); // moves object by vel
        if (!isAlive) return;

        think(g);

        resolveCollisions(g);  // <- add this line

        applyFriction(0.95);
    }


    // In GameObject or Animal
    protected void applyFriction(double factor) {
        vel.multiply(factor); // multiply velocity vector by <1 to slow it down
    }
    
    protected abstract void think(Game g);


    protected void die() {
        isAlive = false;
        vel = new Vector();
    }
}
