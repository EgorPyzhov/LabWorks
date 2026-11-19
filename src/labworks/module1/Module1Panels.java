package labworks.module1;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * Графічні панелі для ЛР1-ЛР7. Кожен метод {@code build...} повертає панель для вкладки.
 */
public class Module1Panels {
    /** Вікно-власник діалогів. */
    private Component parent;

    /**
     * Задає вікно-власник діалогів.
     *
     * @param parent компонент-власник
     */
    public void setParent(Component parent) {
        this.parent = parent;
    }

    /** Читає число з тексту (допускає кому). */
    private static double readNumber(String text) {
        try {
            double v = Double.parseDouble(text.trim().replace(',', '.'));
            if (!Double.isFinite(v)) {
                throw new NumberFormatException();
            }
            return v;
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Введіть коректне число.");
        }
    }

    private static JTextArea newOutput() {
        JTextArea t = new JTextArea();
        t.setEditable(false);
        t.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        return t;
    }

    private static JPanel layout(JPanel top, JTextArea output) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(output), BorderLayout.CENTER);
        return p;
    }

    private static JPanel flow(Component... items) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        for (Component c : items) {
            p.add(c);
        }
        return p;
    }

    /**
     * Панель ЛР1-ЛР3: обчислення G для введеного x (y = 2, a = 1).
     *
     * @param checkDomain true - перевіряти область допустимих значень (ЛР2, ЛР3)
     * @return панель
     */
    public JPanel buildCalculation(boolean checkDomain) {
        JTextField input = new JTextField("2", 10);
        JButton button = new JButton("Обчислити G");
        JTextArea output = newOutput();

        button.addActionListener(e -> {
            try {
                double x = readNumber(input.getText());
                if (checkDomain && !Expression.isValid(x, 1)) {
                    output.setText("x не належить області допустимих значень.");
                    return;
                }
                output.setText("x = " + Fmt.num(x) + "\ny = 2\na = 1\n"
                        + "G = " + Fmt.g(Expression.calculate(x, 2, 1), 12));
            } catch (Exception ex) {
                output.setText("Помилка: " + ex.getMessage());
            }
        });
        return layout(flow(new JLabel("Введіть x:"), input, button), output);
    }

    /**
     * Панель ЛР4: табулювання функції циклами for і while.
     *
     * @return панель
     */
    public JPanel buildLab4() {
        JTextField input = new JTextField("12", 6);
        JButton forBtn = new JButton("Цикл for");
        JButton whileBtn = new JButton("Цикл while");
        JTextArea output = newOutput();

        forBtn.addActionListener(e -> runLab4(input.getText(), false, output));
        whileBtn.addActionListener(e -> runLab4(input.getText(), true, output));
        return layout(flow(new JLabel("Номер варіанта N:"), input, forBtn, whileBtn), output);
    }

    /** Виконує табулювання для ЛР4. */
    private void runLab4(String text, boolean useWhile, JTextArea output) {
        try {
            int n;
            try {
                n = Integer.parseInt(text.trim());
            } catch (NumberFormatException e) {
                n = -1;
            }
            if (n < 0) {
                throw new IllegalArgumentException("N має бути цілим невід'ємним числом.");
            }
            double start = -10 - 2.5 * n;
            double end = 5 + 1.2 * n;
            double step = 0.5 + n / 20.0;
            int count = (int) Math.floor((end - start) / step + 1e-10);
            List<String> lines = new ArrayList<>();
            lines.add("Інтервал: [" + Fmt.num(start) + "; " + Fmt.num(end) + "], крок: " + Fmt.num(step));
            lines.add("x\tG");
            lines.add("---------------------------------------");

            if (!useWhile) {
                for (int i = 0; i <= count; i++) {
                    lines.add(tabulate(start + i * step));
                }
            } else {
                int i = 0;
                while (i <= count) {
                    lines.add(tabulate(start + i * step));
                    i++;
                }
            }
            output.setText(String.join("\n", lines));
            output.setCaretPosition(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, ex.getMessage(), "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Один рядок таблиці значень для ЛР4. */
    private static String tabulate(double x) {
        if (!Expression.isValid(x, 1)) {
            return String.format("%.2f\tНедопустиме значення — пропущено", x);
        }
        return String.format("%.2f\t%s", x, Fmt.g(Expression.calculate(x, 2, 1), 8));
    }

    /**
     * Панель ЛР5: класи, конструктори, властивості.
     *
     * @return панель
     */
    public JPanel buildLab5() {
        JButton button = new JButton("Демонструвати класи");
        JTextArea output = newOutput();
        button.addActionListener(e -> {
            try {
                Expression first = new Expression();
                Expression second = new Expression(-40, 2, 1);
                StringBuilder sb = new StringBuilder();
                sb.append("КОНСТРУКТОР ЗА ЗАМОВЧУВАННЯМ\n")
                        .append("x = ").append(Fmt.num(first.getX())).append("; y = ").append(Fmt.num(first.getY()))
                        .append("; a = ").append(Fmt.num(first.getA())).append('\n')
                        .append("calculate() = ").append(Fmt.g(first.calculate(), 10)).append("\n\n")
                        .append("КОНСТРУКТОР З ПАРАМЕТРАМИ\n")
                        .append("x = ").append(Fmt.num(second.getX())).append("; y = ").append(Fmt.num(second.getY()))
                        .append("; a = ").append(Fmt.num(second.getA())).append('\n')
                        .append("calculate() = ").append(Fmt.g(second.calculate(), 10)).append("\n\n")
                        .append("Статичний метод: ").append(Fmt.g(Expression.calculate(-40, 2, 1), 10)).append("\n\n")
                        .append("Зміна X через сеттер:\n");
                second.setX(-38.9);
                sb.append("X = ").append(Fmt.num(second.getX())).append("; G = ").append(Fmt.g(second.calculate(), 10));
                output.setText(sb.toString());
            } catch (Exception ex) {
                output.setText(ex.getMessage());
            }
        });
        return layout(flow(button), output);
    }

    /**
     * Панель ЛР6: успадкування, перевантаження, агрегація.
     *
     * @return панель
     */
    public JPanel buildLab6() {
        JButton button = new JButton("Демонструвати фігури");
        JTextArea output = newOutput();
        button.addActionListener(e -> {
            try {
                Figure circle = new Circle(5);
                Figure rectangle = new Rectangle(4, 6);
                Drawing drawing = new Drawing(circle);
                output.setText(circle.showInfo() + "\n"
                        + String.format("Площа кола: %.2f%n%n", circle.area())
                        + rectangle.showInfo() + "\n"
                        + String.format("Площа прямокутника: %.2f%n%n", rectangle.area())
                        + circle.showInfo("Назва фігури") + "\n\n"
                        + drawing.showInfo() + "\n\n"
                        + drawing.showInfo("Інформація про малюнок"));
            } catch (Exception ex) {
                output.setText(ex.getMessage());
            }
        });
        return layout(flow(button), output);
    }

    /**
     * Панель ЛР7: абстрактний клас і поліморфізм.
     *
     * @return панель
     */
    public JPanel buildLab7() {
        JButton button = new JButton("Демонструвати поліморфізм");
        JTextArea output = newOutput();
        button.addActionListener(e -> {
            try {
                Figure[] figures = { new Circle(), new Circle(5), new Rectangle(), new Rectangle(4, 6) };
                List<String> lines = new ArrayList<>();
                lines.add("АБСТРАКТНИЙ КЛАС FIGURE");
                lines.add("Результати роботи похідних класів:");
                lines.add("");
                for (Figure f : figures) {
                    lines.add(f.showInfo());
                    lines.add(String.format("Площа: %.2f", f.area()));
                    lines.add("");
                }
                Drawing drawing = new Drawing(new Rectangle(3, 7));
                lines.add(drawing.showInfo("Агрегація"));
                lines.add(String.format("Площа фігури в малюнку: %.2f", drawing.getFigure().area()));
                output.setText(String.join("\n", lines));
            } catch (Exception ex) {
                output.setText(ex.getMessage());
            }
        });
        return layout(flow(button), output);
    }
}
