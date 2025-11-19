package sheepfarm;

import java.util.Random;

public class Utils {

    private static final Random rnd = new Random();

    // Clamp a value between min and max
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    // Generate random double between min (inclusive) and max (exclusive)
    public static double randomRange(double min, double max) {
        return min + rnd.nextDouble() * (max - min);
    }

    // Generate random int between min (inclusive) and max (inclusive)
    public static int randomInt(int min, int max) {
        return min + rnd.nextInt(max - min + 1);
    }

    // Linear interpolation
    public static double lerp(double a, double b, double t) {
        return a + (b - a) * clamp(t, 0, 1);
    }

    // Distance between two points
    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.hypot(x2 - x1, y2 - y1);
    }

    // Distance between two vectors
    public static double distance(Vector v1, Vector v2) {
        return distance(v1.x, v1.y, v2.x, v2.y);
    }

    // Random boolean with probability
    public static boolean chance(double probability) {
        return rnd.nextDouble() < probability;
    }

    // Return a random element from an array
    public static <T> T randomElement(T[] array) {
        if (array.length == 0) return null;
        return array[randomInt(0, array.length - 1)];
    }

    // Return a random element from a list
    public static <T> T randomElement(java.util.List<T> list) {
        if (list.isEmpty()) return null;
        return list.get(randomInt(0, list.size() - 1));
    }
}
