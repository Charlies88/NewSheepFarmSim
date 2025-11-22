package sheepfarm;

import java.awt.image.BufferedImage;

public class PlayerSprites {
    public BufferedImage down, right, upRight, up, upLeft, left, downLeft, downRight;

    public PlayerSprites(BufferedImage down, BufferedImage right, BufferedImage upRight,
                         BufferedImage up, BufferedImage upLeft, BufferedImage left,
                         BufferedImage downLeft, BufferedImage downRight) {
        this.down = down;
        this.right = right;
        this.upRight = upRight;
        this.up = up;
        this.upLeft = upLeft;
        this.left = left;
        this.downLeft = downLeft;
        this.downRight = downRight;
    }
}

