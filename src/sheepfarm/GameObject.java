package sheepfarm;

import java.awt.Graphics2D;

public abstract class GameObject {
    public Vector pos;
    public Vector vel = new Vector();
    public int size;
	public int health;

    public GameObject(double x, double y, int size) {
        this.pos = new Vector(x, y);
        this.size = size;
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

    
    public abstract void render(Graphics2D g);
}
