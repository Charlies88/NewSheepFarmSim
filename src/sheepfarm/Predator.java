package sheepfarm;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Predator extends Animal {

	public Predator(double x, double y, int size, BufferedImage defaultSprite, int maxHealth) {
        super(x, y, size, defaultSprite, maxHealth);
    }
	@Override
	protected void think(Game g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}

}
