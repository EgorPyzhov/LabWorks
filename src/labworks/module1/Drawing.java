package labworks.module1;

/**
 * Малюнок, що містить фігуру (агрегація, ЛР6-ЛР7).
 */
public class Drawing {
    /** Фігура всередині малюнка. */
    private Figure figure;

    /** Створює малюнок з колом за замовчуванням. */
    public Drawing() {
        this(new Circle());
    }

    /**
     * Створює малюнок із фігурою.
     *
     * @param figure фігура (не null)
     */
    public Drawing(Figure figure) {
        setFigure(figure);
    }

    /** @return фігура малюнка */
    public Figure getFigure() {
        return figure;
    }

    /**
     * @param figure нова фігура (не null)
     * @throws NullPointerException якщо фігура null
     */
    public void setFigure(Figure figure) {
        if (figure == null) {
            throw new NullPointerException("figure");
        }
        this.figure = figure;
    }

    /** @return інформація про малюнок */
    public String showInfo() {
        return "Малюнок містить фігуру:\n" + figure.showInfo();
    }

    /**
     * @param title заголовок
     * @return інформація про малюнок із заголовком
     */
    public String showInfo(String title) {
        return title + "\n" + figure.showInfo();
    }
}
