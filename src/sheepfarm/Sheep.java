package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sheep extends Animal {

    private Plant targetGrass = null;

    public Sheep(double x, double y, int size,
                 BufferedImage[] walkRight,
                 BufferedImage[] walkLeft,
                 BufferedImage[] dead) {
        super(x, y, size, walkRight[0], size); // pass default frame to Animal

        // Set animations in the parent
        setAnimation("walk_right", walkRight);
        setAnimation("walk_left", walkLeft);
        setAnimation("dead", dead);
        setAnimation("idle", new BufferedImage[]{walkRight[0]});

        playAnimation("idle");

        // Initialize health
        health = 100;
        maxHealth = 100;
    }

    @Override
    protected void think(Game g) {
        if (!isAlive) return;

        // Seek nearest grass
        targetGrass = findNearestGrass(g);
        if (targetGrass != null) {
            moveTowards(targetGrass.pos, 0.2);

            for (GameObject obj : g.objects) {
                if (obj.foodComponent != null && !obj.foodComponent.isConsumed() 
                    && this.distanceTo(obj) < size + obj.size) {
                    int foodValue = obj.foodComponent.consume(); // returns int
                    this.eat(foodValue);                          // pass int
                }
            }

        }

        // Decide animation based on horizontal movement
        if (vel.x > 0.1) playAnimation("walk_right");
        else if (vel.x < -0.1) playAnimation("walk_left");
        else playAnimation("idle");
    }

    private Plant findNearestGrass(Game g) {
        Plant best = null;
        double bestDist = Double.MAX_VALUE;
        for (GameObject obj : g.objects) {
            if (!(obj instanceof Plant)) continue;
            Plant p = (Plant)obj;
            if (p.foodComponent == null) continue; // skip if no food
            double d = distanceTo(p);
            if (d < bestDist) {
                bestDist = d;
                best = p;
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
        super.render(g); // animation handled in Animal
    }
}
