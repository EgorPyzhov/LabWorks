package labworks.module1;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Допоміжне форматування чисел (аналог форматів {@code G} та простого виводу в C#).
 */
public final class Fmt {
    private Fmt() {
    }

    /**
     * Число у загальному форматі з обмеженням значущих цифр (як {@code G12} у C#).
     *
     * @param v      число
     * @param digits кількість значущих цифр
     * @return рядок без зайвих нулів
     */
    public static String g(double v, int digits) {
        if (v == 0 || Double.isNaN(v) || Double.isInfinite(v)) {
            return v == 0 ? "0" : String.valueOf(v);
        }
        BigDecimal b = new BigDecimal(v).round(new MathContext(digits)).stripTrailingZeros();
        int exp = b.precision() - b.scale() - 1;
        if (exp < -5 || exp >= digits) {
            String m = b.movePointLeft(exp).toPlainString();
            return m + "E" + (exp < 0 ? "-" : "+") + String.format("%02d", Math.abs(exp));
        }
        return b.toPlainString();
    }

    /**
     * Число без зайвого «.0» для цілих значень (як звичайний вивід числа в C#).
     *
     * @param v число
     * @return рядок
     */
    public static String num(double v) {
        if (v == Math.rint(v) && Math.abs(v) < 1e15) {
            return String.valueOf((long) v);
        }
        return String.valueOf(v);
    }
}
