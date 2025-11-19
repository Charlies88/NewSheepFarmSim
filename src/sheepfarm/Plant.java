package sheepfarm;

import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Plant extends GameObject {
    protected double growth = 0; // 0-100%
    protected double growthRate = 0.1;

    public Plant(double x, double y, int size) {
        super(x, y, size);
    }

    @Override
    public void update(Game g) {
        grow();
        super.update(g);
    }

    protected void grow() {
        growth = Utils.clamp(growth + growthRate, 0, 100);
    }

    public double getGrowth() { return growth; }

    @Override
    public abstract void render(Graphics2D g);
}
