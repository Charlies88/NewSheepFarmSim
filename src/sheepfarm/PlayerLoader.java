package sheepfarm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PlayerLoader {

    public PlayerSprites load(String pngPath, String jsonPath) {
        try {
            // --- Read JSON ---
            JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(jsonPath));
            int frameW = ((Long) obj.get("frameWidth")).intValue();
            int frameH = ((Long) obj.get("frameHeight")).intValue();

            // --- Load PNG ---
            BufferedImage sheet = ImageIO.read(new File(pngPath));

            if (sheet.getWidth() < frameW * 8) {
                System.err.println("PLAYER ERROR: Sheet too small or wrong dimensions!");
                return null;
            }

            // Slice 8 frames
            BufferedImage[] f = new BufferedImage[8];
            for (int i = 0; i < 8; i++) {
                f[i] = sheet.getSubimage(i * frameW, 0, frameW, frameH);
            }

            // Return the standalone PlayerSprites
            return new PlayerSprites(f[0], f[1], f[2], f[3], f[4], f[5], f[6], f[7]);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
