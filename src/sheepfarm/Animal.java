package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class Animal extends GameObject {

	protected double damageTimer = 0;
	
	protected double hunger = 0;         // increments each tick
	protected double hungerRate = 0.05;     // amount hunger increases per tick
	protected double hungerThreshold = 100; // when health starts declining
	
	
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

        if (!isAlive) {
            playAnimation("dead");
            return;
        }

        // Hunger increases
        hunger += hungerRate;

        if (hunger >= hungerThreshold) {
            damageTimer += 1.0 / 60.0; // assume 60 FPS
            if (damageTimer >= 1.0) { // 1 second interval
                takeDamage(1);        // damage once per second
                damageTimer = 0;
            }
        }

        // Movement
        pos.add(vel);

        // Animation
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

        // Think after physics
        think(g);
    }




    protected void eat(int foodValue) {
        health = Math.min(health + foodValue, maxHealth);
        hunger = Math.max(hunger - foodValue*10, 0); // reduce hunger by food value
    }



 // In Animal
    public void checkForFood(Game g) {
        // Only mark plants eaten if colliding, don't alter hunger
        if (!isAlive) return;

        for (GameObject obj : g.objects) {
            if (obj instanceof Plant && obj.foodComponent != null && !((Plant)obj).isEaten()) {
                Plant plant = (Plant)obj;
                double dx = plant.pos.x - pos.x;
                double dy = plant.pos.y - pos.y;
                double distance = Math.sqrt(dx*dx + dy*dy);

                if (distance <= size + plant.getBaseInteractionRadius()) {
                    plant.eat(); // mark plant eaten
                }
            }
        }
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
