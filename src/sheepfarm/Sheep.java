package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sheep extends Animal {

    private Plant targetGrass = null;
    private boolean isFleeing = false;
    private int fleeTicks = 0;
    private static final int FLEE_DURATION = 80;

    public Sheep(double x, double y, int size,
                 BufferedImage[] walkRight,
                 BufferedImage[] walkLeft,
                 BufferedImage[] dead) {
        super(x, y, size, walkRight[0]);

        // Set animations in the parent
        setAnimation("walk_right", walkRight);
        setAnimation("walk_left", walkLeft);
        setAnimation("dead", dead);
        setAnimation("idle", new BufferedImage[]{walkRight[0]});

        // Start with idle animation by default
        playAnimation("idle");
    }

    @Override
    protected void think(Game g) {
        if (!isAlive) return;

        // Flee logic
        if (isFleeing) {
            fleeTicks--;
            if (fleeTicks <= 0) isFleeing = false;
            return;
        }

        // Seek nearest grass if not fleeing
        targetGrass = findNearestGrass(g);
        if (targetGrass != null) {
            moveTowards(targetGrass.pos, 0.2);
        }

        // Decide animation based on movement direction
        if (vel.x > 0.1) playAnimation("walk_right");
        else if (vel.x < -0.1) playAnimation("walk_left");
        else playAnimation("idle");
    }

    private Plant findNearestGrass(Game g) {
        Plant best = null;
        double bestDist = Double.MAX_VALUE;
        for (GameObject obj : g.objects) {
            if (!(obj instanceof Grass)) continue;
            double d = distanceTo(obj);
            if (d < bestDist) {
                bestDist = d;
                best = (Plant)obj;
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
        super.render(g); // animation is handled in parent
    }
}