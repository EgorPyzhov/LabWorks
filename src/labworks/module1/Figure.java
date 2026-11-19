package labworks.module1;

/**
 * Абстрактна геометрична фігура (ЛР6-ЛР7).
 */
public abstract class Figure {
    /** Назва фігури. */
    private final String name;

    /** Створює фігуру з назвою за замовчуванням. */
    protected Figure() {
        this("Геометрична фігура");
    }

    /**
     * Створює фігуру з назвою.
     *
     * @param name назва
     */
    protected Figure(String name) {
        this.name = name;
    }

    /** @return назва фігури */
    public String getName() {
        return name;
    }

    /** @return площа фігури */
    public abstract double area();

    /** @return текст з інформацією про фігуру */
    public String showInfo() {
        return "Фігура: " + name;
    }

    /**
     * Інформація про фігуру з повідомленням (перевантаження методу).
     *
     * @param message заголовок
     * @return текст
     */
    public String showInfo(String message) {
        return message + ": " + name;
    }
}
