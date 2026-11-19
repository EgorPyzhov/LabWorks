package labworks.module1;

/**
 * Прямокутник.
 */
public class Rectangle extends Figure {
    /** Ширина. */
    private double width;
    /** Висота. */
    private double height;

    /** Створює прямокутник 1 x 1. */
    public Rectangle() {
        super("Прямокутник");
        width = 1;
        height = 1;
    }

    /**
     * Створює прямокутник.
     *
     * @param width  ширина (додатна)
     * @param height висота (додатна)
     * @throws IllegalArgumentException якщо сторони некоректні
     */
    public Rectangle(double width, double height) {
        super("Прямокутник");
        if (!Double.isFinite(width) || width <= 0) {
            throw new IllegalArgumentException("Ширина має бути додатним числом.");
        }
        if (!Double.isFinite(height) || height <= 0) {
            throw new IllegalArgumentException("Висота має бути додатним числом.");
        }
        this.width = width;
        this.height = height;
    }

    /** @return ширина */
    public double getWidth() {
        return width;
    }

    /** @param width нова ширина */
    public void setWidth(double width) {
        this.width = width;
    }

    /** @return висота */
    public double getHeight() {
        return height;
    }

    /** @param height нова висота */
    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public String showInfo() {
        return String.format("Прямокутник | Сторони: %.2f x %.2f | Площа: %.2f", width, height, area());
    }
}
