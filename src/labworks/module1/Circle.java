package labworks.module1;

/**
 * Коло.
 */
public class Circle extends Figure {
    /** Радіус. */
    private double radius;

    /** Створює коло з радіусом 1. */
    public Circle() {
        super("Коло");
        radius = 1;
    }

    /**
     * Створює коло.
     *
     * @param radius радіус (додатний)
     * @throws IllegalArgumentException якщо радіус некоректний
     */
    public Circle(double radius) {
        super("Коло");
        if (!Double.isFinite(radius) || radius <= 0) {
            throw new IllegalArgumentException("Радіус має бути додатним числом.");
        }
        this.radius = radius;
    }

    /** @return радіус */
    public double getRadius() {
        return radius;
    }

    /** @param radius новий радіус */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    @Override
    public String showInfo() {
        return String.format("Коло | Радіус: %.2f | Площа: %.2f", radius, area());
    }
}
