package sheepfarm;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Animal {
    private int speed = 4;

    // Input flags
    private boolean up, down, left, right;

    // Spritesheet animations
    private PlayerSprites sprites;
    private BufferedImage currentFrame;

    public Player(double x, double y, int size, PlayerSprites sprites) {
        super(x, y, size, sprites.down); // default idle frame
        this.sprites = sprites;
        playAnimation("idle");
    }

    // Determine which frame to show based on movement direction
    BufferedImage getFrameFromDirection(double dx, double dy) {
        if (dy > 0 && Math.abs(dx) < 0.3)      return sprites.down;
        if (dx > 0 && Math.abs(dy) < 0.3)      return sprites.right;
        if (dx > 0 && dy < 0)                   return sprites.upRight;
        if (dy < 0 && Math.abs(dx) < 0.3)      return sprites.up;
        if (dx < 0 && dy < 0)                   return sprites.upLeft;
        if (dx < 0 && Math.abs(dy) < 0.3)      return sprites.left;
        if (dx < 0 && dy > 0)                   return sprites.downLeft;
        if (dx > 0 && dy > 0)                   return sprites.downRight;
        return sprites.down; // idle default
    }

    @Override
    protected void think(Game g) {
        // Move based on input
        double dx = (right ? 1 : 0) - (left ? 1 : 0);
        double dy = (down ? 1 : 0) - (up ? 1 : 0);

        // Normalize diagonal movement
        if (dx != 0 && dy != 0) {
            double inv = 1.0 / Math.sqrt(2);
            dx *= inv;
            dy *= inv;
        }

        vel.x = dx * speed;
        vel.y = dy * speed;

        // Select animation frame based on direction
        currentFrame = getFrameFromDirection(dx, dy);
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(currentFrame, (int)(pos.x - size/2), (int)(pos.y - size/2), size, size, null);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: case KeyEvent.VK_UP:    up = true; break;
            case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  down = true; break;
            case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  left = true; break;
            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: right = true; break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: case KeyEvent.VK_UP:    up = false; break;
            case KeyEvent.VK_S: case KeyEvent.VK_DOWN:  down = false; break;
            case KeyEvent.VK_A: case KeyEvent.VK_LEFT:  left = false; break;
            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: right = false; break;
        }
    }
}
