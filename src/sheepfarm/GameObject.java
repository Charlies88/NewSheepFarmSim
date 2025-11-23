package sheepfarm;

import java.awt.Graphics2D;

public abstract class GameObject {
    public Vector pos;
    public Vector vel = new Vector();
    public int size;
    
	public int health;
	public int maxHealth;
	
    public FoodComponent foodComponent; // optional

    public GameObject(double x, double y, int size, int maxHealth) {
        this.pos = new Vector(x, y);
        this.size = size;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }
    
    /** Base interaction radius, defaults to size */
    public double getBaseInteractionRadius() {
        return size;
    }

    /** Check if a point (x,y) is within base interaction circle */
    public boolean isPointInBase(double x, double y) {
        double dx = x - pos.x;
        double dy = y - pos.y;
        return dx*dx + dy*dy <= getBaseInteractionRadius() * getBaseInteractionRadius();
    }

    // Called every tick
    public void update(Game g) {
        pos.add(vel);
        clampPosition(0, g.getWidth(), 0, g.getHeight());
    }

    protected void clampPosition(double minX, double maxX, double minY, double maxY) {
        pos.x = Utils.clamp(pos.x, minX, maxX);
        pos.y = Utils.clamp(pos.y, minY, maxY);
    }

    public double distanceTo(GameObject other) {
        return Vector.distance(this.pos, other.pos);
    }

    public void resolveCollisions(Game g) {
        double separationDistance = 20; // minimum allowed distance
        for (GameObject other : g.objects) {
            if (other == this) continue;
            Vector diff = other.pos.copy().subtract(this.pos);
            double dist = diff.magnitude();
            if (dist < separationDistance && dist > 0) {
                Vector push = diff.normalize().multiply(separationDistance - dist);
                pos.add(push.multiply(-0.5));  // push self back
                other.pos.add(push.multiply(0.5)); // push other forward
            }
        }
    }

    public void takeDamage(double amount) {
        health -= amount;
        if (health <= 0) die();
    }
    
	 private void die() {
			// TODO Auto-generated method stub
			
		}

    
	 
	 public String getInfo() {
		    StringBuilder sb = new StringBuilder();
		    sb.append("Class: ").append(getClass().getSimpleName()).append("\n");
		    sb.append("Position: ").append(String.format("%.1f, %.1f", pos.x, pos.y)).append("\n");
		    sb.append("Size: ").append(size).append("\n");

		    // Health (if present)
		    if (this instanceof Animal) {
		        Animal a = (Animal)this;
		        sb.append("Health: ").append(a.health).append("/").append(a.maxHealth).append("\n");
		        sb.append("Hunger: ").append(String.format("%.1f", a.hunger)).append("/").append(a.hungerThreshold).append("\n");
		    }

		    // Growth (for plants)
		    if (this instanceof Plant) {
		        Plant p = (Plant)this;
		        sb.append("Growth: ").append(String.format("%.1f", p.getGrowth())).append("\n");
		        if (p.foodComponent != null) {
		            sb.append("Food available: ").append(!p.foodComponent.isConsumed()).append("\n");
		        }
		    }

		    // Any other info (food components, etc.)
		    if (foodComponent != null) {
		        sb.append("Food value: ").append(foodComponent.getFoodValue()).append("\n");
		    }

		    return sb.toString();
		}

	 
	 
    public abstract void render(Graphics2D g);
}
