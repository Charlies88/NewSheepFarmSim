package sheepfarm;

public class Vector {
    public double x, y;

    public Vector() { this(0, 0); }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector add(Vector other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector subtract(Vector other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector divide(double scalar) {
        if (scalar != 0) {
            this.x /= scalar;
            this.y /= scalar;
        }
        return this;
    }

    public double magnitude() {
        return Math.hypot(x, y);
    }

    public Vector normalize() {
        double mag = magnitude();
        if (mag != 0) divide(mag);
        return this;
    }

    public Vector copy() {
        return new Vector(x, y);
    }

    public static double distance(Vector a, Vector b) {
        return Math.hypot(a.x - b.x, a.y - b.y);
    }
}
