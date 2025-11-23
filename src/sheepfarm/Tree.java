package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tree extends Plant {
    private BufferedImage[] stages; // all growth stages
    private int currentStage = 0;

    public Tree(double x, double y, int size, BufferedImage[] stages) {
        super(x, y, size, 0, stages[0]); // initial sprite = first stage
        this.stages = stages;
    }

    @Override
    public void update(Game g) {
        super.update(g); // handles growth and physics
        currentStage = (int)((growth / getMaxGrowth()) * (stages.length - 1));
        sprite = stages[currentStage];
    }


    @Override
    public void render(Graphics2D g) {
        if (sprite == null) return;
        int drawSize = size * 2;
        g.drawImage(sprite, (int)(pos.x - drawSize/2), (int)(pos.y - drawSize/2), drawSize, drawSize, null);
    }
    
    public BufferedImage getCurrentStageSprite() {
        return stages[currentStage];
    }
 
    
    @Override
    public double getBaseInteractionRadius() {
        // make the interaction radius match the visual size (e.g., double the size)
        return size * 2;
    }

    
    
    @Override
    public boolean isPointInBase(double x, double y) {
        double dx = pos.x - x;
        double dy = pos.y - y;
        return dx*dx + dy*dy <= getBaseInteractionRadius() * getBaseInteractionRadius();
    }


}
