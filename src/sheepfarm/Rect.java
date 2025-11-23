package sheepfarm;

public class Rect {
    public int x, y, w, h;
    public Rect(int x, int y, int w, int h) { this.x = x; this.y = y; this.w = w; this.h = h; }

    public boolean intersects(Rect other) {
        return x < other.x + other.w && x + w > other.x &&
               y < other.y + other.h && y + h > other.y;
    }

    public int getLeft() { return x; }
    public int getRight() { return x + w; }
    public int getTop() { return y; }
    public int getBottom() { return y + h; }
    public double getCenterX() { return x + w / 2.0; }
    public double getCenterY() { return y + h / 2.0; }
}
