package sheepfarm;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Sheep extends Animal {

    private int woolAmount = 3;
    private boolean isSheared = false;
    
    

    // Flee / frightened timers
    private boolean isFleeing = false;
    private int fleeTicks = 0;
    private static final int FLEE_DURATION = 80;

    // Grass target
    private Plant targetGrass = null;

    public Sheep(double x, double y, int size, BufferedImage sprite) {
        super(x, y, size, sprite);
    }

    @Override
    protected void think(Game g) {
        if (!isAlive) return;

        // 2️⃣ Seek grass if hungry
        if (!isFleeing) {
            targetGrass = findNearestGrass(g);
            if (targetGrass != null) {
                moveTowards(targetGrass.pos, 0.2);
            } 
        }

        // 3️⃣ Countdown fleeing
        if (isFleeing) {
            fleeTicks--;
            if (fleeTicks <= 0) isFleeing = false;
        }
    }


    private Plant findNearestGrass(Game g) {
        Plant best = null;
        double bestDist = Double.MAX_VALUE;
        for (GameObject obj : g.objects) {
            if (!(obj instanceof Grass)) continue;
            Grass grass = (Grass)obj;
            double d = distanceTo(grass);
            if (d < bestDist) {
                bestDist = d;
                best = grass;
            }
        }
        return best;
    }

    private void moveTowards(Vector target, double speed) {
        Vector dir = new Vector(target.x - pos.x, target.y - pos.y).normalize();
        vel.add(dir.multiply(speed));
    }

    @Override
    public void render(Graphics2D g) {
        if (sprite != null) {
            g.drawImage(sprite, (int)(pos.x - size), (int)(pos.y - size), size*2, size*2, null);
        } else {
            g.setColor(isAlive ? Color.WHITE : Color.GRAY);
            g.fillOval((int)(pos.x - size), (int)(pos.y - size), size*2, size*2);
        }
    }

}
