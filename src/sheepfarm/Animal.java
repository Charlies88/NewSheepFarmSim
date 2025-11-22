package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class Animal extends GameObject {


    public boolean isAlive = true;
    protected Vector vel = new Vector();

    // Map of animation name -> frames
    protected Map<String, BufferedImage[]> animations = new HashMap<>();
    protected String currentAnim = "idle"; 
    protected int currentFrame = 0;
    protected int frameTick = 0;
    protected int frameDelay = 6;

    public Animal(double x, double y, int size, BufferedImage defaultSprite, int maxHealth) {
        super(x, y, size, maxHealth);
        animations.put("idle", new BufferedImage[]{defaultSprite});
    }


    public void setAnimation(String name, BufferedImage[] frames) {
        animations.put(name, frames);
    }

    public void playAnimation(String name) {
        if (!animations.containsKey(name)) return;
        if (!name.equals(currentAnim)) {
            currentAnim = name;
            currentFrame = 0;
            frameTick = 0;
        }
    }

    @Override
    public void update(Game g) {
        super.update(g);

        if (!isAlive) playAnimation("dead");

        pos.add(vel);

        // Animate
        BufferedImage[] frames = animations.getOrDefault(currentAnim, animations.get("idle"));
        if (frames.length > 1) {
            frameTick++;
            if (frameTick >= frameDelay) {
                frameTick = 0;
                currentFrame = (currentFrame + 1) % frames.length;
            }
        } else {
            currentFrame = 0;
        }

        applyFriction(0.95);
        resolveCollisions(g);
        think(g);
    }

    public void eat(int foodValue) {
        health += foodValue;
        if (health > maxHealth) health = maxHealth;
        System.out.println(getClass().getSimpleName() + " ate " + foodValue + " and now has " + health + " health.");
    }



    protected void applyFriction(double factor) {
        vel.multiply(factor);
    }

    protected abstract void think(Game g);

    @Override
    public void render(Graphics2D g) {
        BufferedImage[] frames = animations.getOrDefault(currentAnim, animations.get("idle"));
        g.drawImage(frames[currentFrame], (int)(pos.x - size), (int)(pos.y - size),
                    size*2, size*2, null);
    }

    protected void die() {
        isAlive = false;
        vel = new Vector();
    }
}
