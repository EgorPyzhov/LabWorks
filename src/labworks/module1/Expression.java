package labworks.module1;

/**
 * Математичний вираз G(x, y, a) з ЛР1-ЛР5 (варіант 12).
 */
public class Expression {
    /** Змінна x. */
    private double x;
    /** Змінна y. */
    private double y;
    /** Параметр a. */
    private double a;

    /** Створює вираз зі значеннями за замовчуванням: x=1, y=2, a=1. */
    public Expression() {
        this(1, 2, 1);
    }

    /**
     * Створює вираз із заданими значеннями.
     *
     * @param x змінна x
     * @param y змінна y
     * @param a параметр a
     */
    public Expression(double x, double y, double a) {
        this.x = x;
        this.y = y;
        this.a = a;
    }

    /** @return значення x */
    public double getX() {
        return x;
    }

    /** @param x нове значення x */
    public void setX(double x) {
        this.x = x;
    }

    /** @return значення y */
    public double getY() {
        return y;
    }

    /** @param y нове значення y */
    public void setY(double y) {
        this.y = y;
    }

    /** @return значення a */
    public double getA() {
        return a;
    }

    /** @param a нове значення a */
    public void setA(double a) {
        this.a = a;
    }

    /**
     * Перевіряє, чи належить x області допустимих значень.
     *
     * @param x змінна x
     * @param a параметр a
     * @return true, якщо x не дорівнює 0 і x + a не дорівнює 0
     */
    public static boolean isValid(double x, double a) {
        return Double.isFinite(x) && Double.isFinite(a) && x != 0 && x + a != 0;
    }

    /**
     * Обчислює G для поточних значень полів.
     *
     * @return результат
     */
    public double calculate() {
        return calculate(x, y, a);
    }

    /**
     * Обчислює G(x, y, a) = (cos^3|x+y| - (x+y)) / (atan^4(x+a) * x^5).
     *
     * @param x змінна x
     * @param y змінна y
     * @param a параметр a
     * @return результат
     * @throws IllegalArgumentException якщо x не належить області допустимих значень
     * @throws ArithmeticException      якщо результат не є скінченним числом
     */
    public static double calculate(double x, double y, double a) {
        if (!isValid(x, a)) {
            throw new IllegalArgumentException("x не належить області допустимих значень.");
        }
        double numerator = Math.pow(Math.cos(Math.abs(x + y)), 3) - (x + y);
        double denominator = Math.pow(Math.atan(x + a), 4) * Math.pow(x, 5);
        double result = numerator / denominator;
        if (!Double.isFinite(result)) {
            throw new ArithmeticException("Результат не є скінченним числом.");
        }
        return result;
    }
}
