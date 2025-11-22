package sheepfarm;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import sheepfarm.ResourceLoader;

public class ResourceLoader {

    private final Map<String, BufferedImage[]> sprites = new HashMap<>();

    /**
     * Load all PNGs in a folder with optional JSON metadata.
     * For animals, flipping is applied (right/left), for buildings/items no flipping is needed.
     * You can pass isAnimal = true for animals/NPCs, false for static sprites.
     */
    public void loadFolder(String folderPath, boolean isAnimal) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Assets folder not found: " + folderPath);
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".png"));
        if (files == null) return;

        for (File sheetFile : files) {
            String baseName = sheetFile.getName().replace(".png", "");
            File jsonFile = new File(folderPath, baseName + ".json");
            loadSheet(sheetFile, jsonFile.exists() ? jsonFile : null, baseName, isAnimal);
        }
    }

    private void loadSheet(File sheetFile, File metaFile, String key, boolean flipForAnimal) {
        try {
            BufferedImage sheet = ImageIO.read(sheetFile);
            int frameWidth = sheet.getHeight(); // default: square frames
            int frameHeight = sheet.getHeight();
            int frames = sheet.getWidth() / frameWidth;

            // JSON override
            if (metaFile != null) {
                try {
                    JSONObject obj = (JSONObject) new JSONParser().parse(new FileReader(metaFile));
                    frameWidth = ((Long) obj.get("frameWidth")).intValue();
                    frameHeight = ((Long) obj.getOrDefault("frameHeight", (long) frameWidth)).intValue();
                    JSONArray anims = (JSONArray) obj.get("animations");
                    if (anims != null && anims.size() > 0) {
                        JSONObject firstAnim = (JSONObject) anims.get(0);
                        if (firstAnim.containsKey("frames")) {
                            frames = ((Long) firstAnim.get("frames")).intValue();
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Failed to read JSON: " + metaFile.getName() + " — using defaults");
                }
            }

            BufferedImage[] rightFrames = new BufferedImage[frames];
            for (int i = 0; i < frames; i++) {
                rightFrames[i] = sheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }

            if (flipForAnimal) {
                sprites.put(key + "_right", rightFrames);
                sprites.put(key + "_left", flipFrames(rightFrames));
            } else {
                sprites.put(key, rightFrames); // static sprite, no flip
            }

        } catch (Exception e) {
            System.out.println("Failed to load sheet: " + sheetFile.getName());
            e.printStackTrace();
        }
    }

    private BufferedImage[] flipFrames(BufferedImage[] frames) {
        BufferedImage[] flipped = new BufferedImage[frames.length];
        for (int i = 0; i < frames.length; i++) {
            BufferedImage orig = frames[i];
            BufferedImage copy = new BufferedImage(orig.getWidth(), orig.getHeight(), BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < orig.getHeight(); y++) {
                for (int x = 0; x < orig.getWidth(); x++) {
                    copy.setRGB(orig.getWidth() - 1 - x, y, orig.getRGB(x, y));
                }
            }
            flipped[i] = copy;
        }
        return flipped;
    }

    public BufferedImage[] get(String key) {
        return sprites.get(key);
    }

    public Set<String> getLoadedKeys() {
        return sprites.keySet();
    }

    public Map<String, BufferedImage[]> getSprites() {
        return sprites;
    }
}
