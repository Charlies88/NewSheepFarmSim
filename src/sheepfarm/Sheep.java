package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sheep extends Animal {

    private Plant targetGrass = null;

    public Sheep(double x, double y, int size,
                 BufferedImage[] walkRight,
                 BufferedImage[] walkLeft,
                 BufferedImage[] dead) {
        super(x, y, size, walkRight[0], 100); // default health = 100

        // Set animations
        setAnimation("walk_right", walkRight);
        setAnimation("walk_left", walkLeft);
        setAnimation("dead", dead);
        setAnimation("idle", new BufferedImage[]{walkRight[0]});

        playAnimation("idle");

        health = 100;
        maxHealth = 100;
    }

    @Override
    protected void think(Game g) {
        if (!isAlive) return;

        // Always move around or wander a bit
        wander(); // implement a simple wandering behavior if needed

        // Only seek and eat grass if hungry
        if (hunger >= hungerThreshold) {
            targetGrass = findNearestGrass(g);
            if (targetGrass != null) {
                moveTowards(targetGrass.pos, 0.2);

                double distance = distanceTo(targetGrass);
                if (distance <= size + targetGrass.getBaseInteractionRadius()) {
                    if (!targetGrass.isEaten()) {
                        targetGrass.eat();
                        eat(targetGrass.foodComponent.getFoodValue());
                        hunger = 0; // reset hunger after eating
                    }
                }
            }
        }

        // Animation
        if (vel.x > 0.1) playAnimation("walk_right");
        else if (vel.x < -0.1) playAnimation("walk_left");
        else playAnimation("idle");
    }

    // Example simple wandering
    private void wander() {
        // small random velocity change
        double dx = (Math.random() - 0.5) * 0.1;
        double dy = (Math.random() - 0.5) * 0.1;
        vel.add(new Vector(dx, dy));
    }


    private Plant findNearestGrass(Game g) {
        Plant nearest = null;
        double nearestDist = Double.MAX_VALUE;
        for (GameObject obj : g.objects) {
            if (obj instanceof Plant) {
                Plant p = (Plant) obj;
                if (p.foodComponent == null || p.isEaten()) continue;
                double d = distanceTo(p);
                if (d < nearestDist) {
                    nearestDist = d;
                    nearest = p;
                }
            }
        }
        return nearest;
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
