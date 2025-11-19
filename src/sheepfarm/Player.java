package sheepfarm;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends Animal {

    // Position is inherited from Animal/GameObject
    private int speed = 4;

    // Input flags
    private boolean up, down, left, right;

    // Spritesheet & frames
    private BufferedImage sheet;
    private BufferedImage[][] frames; // [direction][frameIndex]
    private int currentFrame = 0;
    private int frameTick = 0;
    private int frameDelay = 6;
    private int direction = 0; // 0=S,1=SE,...7=E
    private final int numDirections = 8;
    private final int numFrames = 1; // 1 frame per direction

    // Directions
    public static final int SOUTH = 0, SOUTH_EAST = 1, NORTH_EAST = 2, NORTH = 3,
                            NORTH_WEST = 4, SOUTH_WEST = 5, WEST = 6, EAST = 7;

    public Player(double x, double y, int size, BufferedImage sprite) {
        super(x, y, size, sprite);
        this.sheet = sprite;
        loadFrames();
    }

    private void loadFrames() {
        if (sheet == null) return;

        int frameWidth = sheet.getWidth() / numDirections;
        int frameHeight = sheet.getHeight();

        frames = new BufferedImage[numDirections][numFrames];

        for (int dir = 0; dir < numDirections; dir++) {
            frames[dir][0] = sheet.getSubimage(dir * frameWidth, 0, frameWidth, frameHeight);
        }
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

        // Update direction for animation
        if (up && right) direction = NORTH_EAST;
        else if (up && left) direction = NORTH_WEST;
        else if (down && right) direction = SOUTH_EAST;
        else if (down && left) direction = SOUTH_WEST;
        else if (up) direction = NORTH;
        else if (down) direction = SOUTH;
        else if (left) direction = WEST;
        else if (right) direction = EAST;

        // Animate if moving
        if (dx != 0 || dy != 0) {
            frameTick++;
            if (frameTick >= frameDelay) {
                frameTick = 0;
                currentFrame = (currentFrame + 1) % numFrames;
            }
        } else {
            currentFrame = 0;
            frameTick = 0;
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (frames != null) {
            g.drawImage(frames[direction][currentFrame],
                    (int)pos.x - size, (int)pos.y - size,
                    size * 2, size * 2, null);
        } else {
            g.setColor(Color.BLUE);
            g.fillRect((int)pos.x - size, (int)pos.y - size, size * 2, size * 2);
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: case KeyEvent.VK_UP: up = true; break;
            case KeyEvent.VK_S: case KeyEvent.VK_DOWN: down = true; break;
            case KeyEvent.VK_A: case KeyEvent.VK_LEFT: left = true; break;
            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: right = true; break;
        }
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: case KeyEvent.VK_UP: up = false; break;
            case KeyEvent.VK_S: case KeyEvent.VK_DOWN: down = false; break;
            case KeyEvent.VK_A: case KeyEvent.VK_LEFT: left = false; break;
            case KeyEvent.VK_D: case KeyEvent.VK_RIGHT: right = false; break;
        }
    }
}
