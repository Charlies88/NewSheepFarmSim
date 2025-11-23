package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tree extends Plant {
    private BufferedImage[] stages; // all growth stages
    private int currentStage = 0;
    private int stumpTimer = 0; // for stage 3 disappearance

    public Tree(double x, double y, int size, BufferedImage[] stages, int foodValue) {
        super(x, y, size, foodValue, stages[0]); // give base stage a food value
        this.stages = stages;
        if (foodValue > 0) {
            foodComponent = new FoodComponent(foodValue);
        }
    }

    @Override
    public void update(Game g) {
        // Grow naturally up to maxGrowth
        if (currentStage < 3) { // stages 0-2 grow
            growth = Math.min(growth + growthRate, getMaxGrowth());
        }

        // Update stage based on growth
        if (growth < getMaxGrowth() * 0.33) {
            currentStage = 0;
        } else if (growth < getMaxGrowth() * 0.66) {
            currentStage = 1;
        } else if (growth < getMaxGrowth()) {
            currentStage = 2;
        } else {
            currentStage = 2; // fully grown tree
        }

        sprite = stages[currentStage];

        // Handle eaten
        if (eaten) {
            switch (currentStage) {
                case 0: // Stage 0: sprout disappears
                    g.objects.remove(this);
                    return;
                case 1: // Stage 1: sapling
                case 2: // Stage 2: full tree
                    growth = Math.max(0, growth - 20); // reduce growth when eaten
                    eaten = false;
                    break;
                case 3: // Stage 3: stump
                    stumpTimer++;
                    if (stumpTimer > 500) g.objects.remove(this);
                    eaten = false;
                    break;
            }
        }

        // Tree is fully rooted: ignore velocity/position updates
        vel.x = 0;
        vel.y = 0;
        pos.x = homePos.x;
        pos.y = homePos.y;
    }


    @Override
    public void eat() {
        eaten = true;
    }

    @Override
    public void render(Graphics2D g) {
        if (sprite == null) return;
        int drawSize = size * 2;
        g.drawImage(sprite, (int)(pos.x - drawSize/2), (int)(pos.y - drawSize/2), drawSize, drawSize, null);
    }

    @Override
    public double getBaseInteractionRadius() {
        return size * 2;
    }

    @Override
    public boolean isPointInBase(double x, double y) {
        double dx = pos.x - x;
        double dy = pos.y - y;
        return dx*dx + dy*dy <= getBaseInteractionRadius() * getBaseInteractionRadius();
    }

    public BufferedImage getCurrentStageSprite() {
        return stages[currentStage];
    }
}
