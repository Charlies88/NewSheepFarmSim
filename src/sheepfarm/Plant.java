package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Plant extends GameObject {
    protected Vector homePos;
    
    protected double growth = 0;
    private double maxGrowth = 100;    // fully grown size
    protected double growthRate = 0.005; // growth per update tick

    // physics
    protected double springStrength = 0.01;
    protected double dampening = 0.1;
    protected double pushForce = 0.01;
    protected boolean eaten = false;
    protected boolean rigidRoot = false;

    protected BufferedImage sprite;

    public Plant(double x, double y, int size, int foodValue, BufferedImage sprite) {
        super(x, y, size, foodValue);
        this.sprite = sprite;
        homePos = pos.copy();

        if (foodValue > 0) {
        	foodComponent = new FoodComponent((int)(foodValue * (size / 20.0)));

        }
    }

    public int getCurrentSize() {
        return Math.max(1, (int)(size * (growth / 100.0)));
    }
    
    public int getFoodValue() {
        if (foodComponent == null) return 0;

        // scale food value by growth (0.0–1.0) or size
        double scale = getGrowth() / getMaxGrowth(); // assuming getGrowth() returns current growth
        return (int)(foodComponent.getFoodValue() * scale);
    }


    @Override
    public double getBaseInteractionRadius() {
        return Math.max(5, getCurrentSize());
    }

    @Override
    public boolean isPointInBase(double x, double y) {
        // base is at the bottom of the sprite
        int drawSize = getCurrentSize() * 2;
        double baseX = pos.x;
        double baseY = pos.y + drawSize / 2; // move down to bottom
        double dx = baseX - x;
        double dy = baseY - y;
        double distance = Math.sqrt(dx*dx + dy*dy);
        return distance <= getBaseInteractionRadius();
    }

    @Override
    public void render(Graphics2D g) {
        if (sprite == null || isEaten()) return;

        int drawSize = getCurrentSize() * 2;
        g.drawImage(sprite, (int)(pos.x - drawSize/2), (int)(pos.y - drawSize/2), drawSize, drawSize, null);
    }


    public void push(double dx, double dy) {
        vel.add(new Vector(dx * pushForce, dy * pushForce));
    }

    @Override
    public void update(Game g) {
        // Grow
        grow();

        if (rigidRoot) {
            // Fully rooted: ignore velocity, keep position fixed
            vel.x = 0;
            vel.y = 0;
            pos.x = homePos.x;
            pos.y = homePos.y;
        } else {
            // Gentle spring-back to home position
            Vector toHome = homePos.copy().subtract(pos).multiply(springStrength);
            vel.add(toHome);

            // Apply friction/dampening
            vel.multiply(dampening);

            // Update position
            super.update(g);
        }
    }



    protected void grow() {
        growth = Utils.clamp(growth + growthRate, 0, 100);
    }
    
    public void setGrowth(double value) {
        this.growth = Math.min(value, maxGrowth);
    }


    public double getGrowth() { return growth; }
    
    public double getMaxGrowth() {
        return maxGrowth;
    }
    
    public boolean isEaten() {
        return eaten;
    }

    public void eat() {
        this.eaten = true;
    }
}
