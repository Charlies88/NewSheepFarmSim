package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Plant extends GameObject {
    protected Vector homePos;
    protected double growth = 0;
    protected double growthRate = 0.05;

    // physics
    protected double springStrength = 0.01;
    protected double dampening = 0.1;
    protected double pushForce = 0.01;

    protected BufferedImage sprite;

    public Plant(double x, double y, int size, int foodValue, BufferedImage sprite) {
        super(x, y, size, foodValue);
        this.sprite = sprite;
        homePos = pos.copy();

        if (foodValue > 0) {
            this.foodComponent = new FoodComponent(foodValue);
        }
    }

    public int getCurrentSize() {
        return Math.max(1, (int)(size * (growth / 100.0)));
    }

    @Override
    public double getBaseInteractionRadius() {
        return Math.max(5, getCurrentSize());
    }

    @Override
    public boolean isPointInBase(double x, double y) {
        double dx = pos.x - x;
        double dy = pos.y - y; // pos.y is base
        double distance = Math.sqrt(dx*dx + dy*dy);
        return distance <= getBaseInteractionRadius();
    }

    @Override
    public void render(Graphics2D g) {
        if (sprite == null) return;

        int drawSize = getCurrentSize() * 2;
        int drawX = (int)(pos.x - drawSize / 2);
        int drawY = (int)(pos.y - drawSize); // base at pos.y
        g.drawImage(sprite, drawX, drawY, drawSize, drawSize, null);

        // Optional: draw base circle for debugging
        // g.setColor(new Color(255, 0, 0, 100));
        // g.fillOval((int)(pos.x - getBaseInteractionRadius()), (int)(pos.y - getBaseInteractionRadius()),
        //            (int)(getBaseInteractionRadius()*2), (int)(getBaseInteractionRadius()*2));
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
}
