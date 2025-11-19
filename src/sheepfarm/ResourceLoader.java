package sheepfarm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ResourceLoader {

    public BufferedImage sheepRightSprite;
    public BufferedImage sheepLeftSprite;
    public BufferedImage sheepDeadSprite;
    public BufferedImage grassSprite;
    public BufferedImage fenceSprite;
    public BufferedImage barnSprite;
    public BufferedImage playerSheet;

    public ResourceLoader() {
        sheepRightSprite = load("sheep_right.png");
        sheepLeftSprite  = load("sheep_left.png");
        sheepDeadSprite  = load("sheep_dead.png");
        grassSprite      = load("grass.png");
        fenceSprite      = load("fence.png");
        barnSprite       = load("barn.png");
        playerSheet      = load("player_spritesheet.png");
    }

    private BufferedImage load(String filename) {
        // First try classpath
        try {
            BufferedImage img = ImageIO.read(getClass().getResource("/assets/" + filename));
            if (img != null) return img;
        } catch (Exception ignored) {}

        // Fallback to file system (relative to project root)
        try {
            return ImageIO.read(new File("assets/" + filename));
        } catch (IOException e) {
            System.out.println("Could not load image: " + filename);
            e.printStackTrace();
            return null;
        }
    }
}
