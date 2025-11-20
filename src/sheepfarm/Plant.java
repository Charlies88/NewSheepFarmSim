package sheepfarm;

import java.awt.Graphics2D;

public abstract class Plant extends GameObject {
    protected Vector homePos;
    protected double growth = 0;
    protected double growthRate = 0.1;

    // physics
    protected double springStrength = 0.05;
    protected double dampening = 0.85;
    protected double pushForce = 0.2;

    public Plant(double x, double y, int size) {
        super(x, y, size);
        homePos = pos.copy();
    }

    public void push(double dx, double dy) {
        vel.add(new Vector(dx * pushForce, dy * pushForce));
    }

    @Override
    public void update(Game g) {
        grow();
        
        // gently move back to home
        Vector toHome = homePos.copy().subtract(pos).multiply(springStrength);
        vel.add(toHome);

        // apply friction/dampening
        vel.multiply(dampening);

        super.update(g);
    }

    protected void grow() {
        growth = Utils.clamp(growth + growthRate, 0, 100);
    }

    public double getGrowth() { return growth; }

    @Override
    public abstract void render(Graphics2D g);
}
