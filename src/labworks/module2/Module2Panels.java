package labworks.module2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class Module2Panels {
    private Component parent;
    private final FigureManager manager = new FigureManager();
    private final FigureDatabase db;
    private final List<Runnable> refreshers = new ArrayList<>();

   
    public Module2Panels(FigureDatabase db) {
        this.db = db;
    }

    
    public void setParent(Component parent) {
        this.parent = parent;
    }


    private JTable newFigureTable() {
        DefaultTableModel m = new DefaultTableModel(
                new String[] { "№", "Назва", "Сторона A", "Сторона B", "Площа" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable t = new JTable(m);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return t;
    }

    private void showFigures(JTable table, List<GeometricFigure> list) {
        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.setRowCount(0);
        for (GeometricFigure f : list) {
            m.addRow(new Object[] { manager.indexOf(f) + 1, f.getName(),
                    String.format("%.2f", f.getSideA()), String.format("%.2f", f.getSideB()),
                    String.format("%.2f", f.getArea()) });
        }
    }

    private void registerFullView(JTable table) {
        Runnable r = () -> showFigures(table, manager.getAll());
        refreshers.add(r);
        r.run();
    }

    private void refreshAll() {
        for (Runnable r : refreshers) {
            r.run();
        }
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Помилка", JOptionPane.ERROR_MESSAGE);
    }

    private double num(String s) {
        return Double.parseDouble(s.trim().replace(',', '.'));
    }

    private JPanel panel(JComponent top, JTable table, JComponent bottom) {
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        if (bottom != null) {
            p.add(bottom, BorderLayout.SOUTH);
        }
        return p;
    }

    private JPanel flow(Component... items) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        for (Component c : items) {
            p.add(c);
        }
        return p;
    }


    public JPanel buildLab8() {
        JTable table = newFigureTable();
        registerFullView(table);

        JButton all = new JButton("Показати всі");
        JButton modify = new JButton("Змінити вибрану");
        JTextField minArea = new JTextField("20", 5);
        JButton filter = new JButton("Площа більша за:");
        JButton asc = new JButton("Сортувати за площею ↑");
        JButton desc = new JButton("Сортувати за площею ↓");

        all.addActionListener(e -> refreshAll());
        asc.addActionListener(e -> {
            manager.sortByArea(true);
            refreshAll();
        });
        desc.addActionListener(e -> {
            manager.sortByArea(false);
            refreshAll();
        });
        filter.addActionListener(e -> {
            try {
                showFigures(table, manager.findByArea(num(minArea.getText())));
            } catch (NumberFormatException ex) {
                error("Введіть число.");
            }
        });
        modify.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                error("Спочатку виділіть фігуру в таблиці.");
                return;
            }
            int index = (Integer) table.getValueAt(row, 0) - 1;
            JTextField name = new JTextField(String.valueOf(table.getValueAt(row, 1)));
            JTextField a = new JTextField(String.valueOf(table.getValueAt(row, 2)));
            JTextField b = new JTextField(String.valueOf(table.getValueAt(row, 3)));
            Object[] form = { "Назва:", name, "Сторона A:", a, "Сторона B:", b };
            if (JOptionPane.showConfirmDialog(parent, form, "Зміна фігури №" + (index + 1),
                    JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
                return;
            }
            try {
                manager.modifyFigure(index, name.getText(), num(a.getText()), num(b.getText()));
                refreshAll();
            } catch (NumberFormatException ex) {
                error("Сторони мають бути числами.");
            } catch (IllegalArgumentException ex) {
                error(ex.getMessage());
            }
        });

        return panel(flow(all, modify, filter, minArea, asc, desc), table, null);
    }


    public JPanel buildLab9() {
        JTable table = newFigureTable();
        registerFullView(table);

        JLabel result = new JLabel(" ");
        JButton asc = new JButton("Сортувати за назвою А-Я");
        JButton desc = new JButton("Сортувати за назвою Я-А");
        JTextField regex = new JTextField("Squ.*", 10);
        JButton filter = new JButton("Фільтр за виразом:");
        JButton minMax = new JButton("Мін / макс площа");
        JButton all = new JButton("Показати всі");

        asc.addActionListener(e -> {
            manager.sortByName(true);
            refreshAll();
        });
        desc.addActionListener(e -> {
            manager.sortByName(false);
            refreshAll();
        });
        filter.addActionListener(e -> {
            try {
                showFigures(table, manager.findByNameRegex(regex.getText()));
            } catch (PatternSyntaxException ex) {
                error("Некоректний регулярний вираз.");
            }
        });
        minMax.addActionListener(e -> result.setText("<html>" + manager.getMinMaxAreaText() + "</html>"));
        all.addActionListener(e -> refreshAll());

        return panel(flow(asc, desc, regex, filter, minMax, all), table, result);
    }


    public JPanel buildLab10() {
        JTable table = newFigureTable();
        registerFullView(table);

        JLabel status = new JLabel("Файли обираються у вікні вибору.");
        JButton readTxt = new JButton("Зчитати з текстового файлу");
        JButton writeBin = new JButton("Записати відсортований масив (бінарний)");
        JButton readBin = new JButton("Зчитати бінарний файл");

        readTxt.addActionListener(e -> {
            File f = chooseFile(false, "figures.txt");
            if (f == null) {
                return;
            }
            try {
                int skipped = manager.loadFromTextFile(f.getPath());
                refreshAll();
                status.setText("Дані зчитано з " + f.getName() + ". Пропущено рядків: " + skipped);
            } catch (Exception ex) {
                error("Помилка читання: " + ex.getMessage());
            }
        });
        writeBin.addActionListener(e -> {
            File f = chooseFile(true, "figures.dat");
            if (f == null) {
                return;
            }
            try {
                manager.saveToBinaryFile(f.getPath());
                refreshAll();
                status.setText("Відсортований масив записано у " + f.getName());
            } catch (Exception ex) {
                error("Помилка запису: " + ex.getMessage());
            }
        });
        readBin.addActionListener(e -> {
            File f = chooseFile(false, "figures.dat");
            if (f == null) {
                return;
            }
            try {
                manager.readBinaryFile(f.getPath());
                refreshAll();
                status.setText("Дані зчитано з бінарного файлу " + f.getName());
            } catch (Exception ex) {
                error("Помилка читання: " + ex.getMessage());
            }
        });

        return panel(flow(readTxt, writeBin, readBin), table, status);
    }

    /** Вікно вибору файлу; повертає null, якщо користувач скасував. */
    private File chooseFile(boolean save, String defaultName) {
        JFileChooser fc = new JFileChooser(new File("."));
        fc.setSelectedFile(new File(defaultName));
        int r = save ? fc.showSaveDialog(parent) : fc.showOpenDialog(parent);
        return r == JFileChooser.APPROVE_OPTION ? fc.getSelectedFile() : null;
    }


    public JPanel buildLab11() {
        DefaultTableModel model = new DefaultTableModel(
                new String[] { "ID", "Тип", "Ширина", "Висота", "Площа", "Периметр" }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JLabel status = new JLabel(" ");
        JButton all = new JButton("Переглянути всі");
        JButton add = new JButton("Додати");
        JButton edit = new JButton("Редагувати вибраний");
        JButton del = new JButton("Видалити вибраний");
        JTextField maxPer = new JTextField("20", 4);
        JButton byPer = new JButton("Периметр менше:");
        JComboBox<String> typeBox = new JComboBox<>(new String[] { "Rectangle", "Square", "Figure" });
        JButton byType = new JButton("Вибірка за типом");
        JButton imp = new JButton("Імпорт з figures.txt");
        JButton recreate = new JButton("Створити БД заново");

        Runnable showAll = () -> {
            try {
                fillDb(model, db.selectAll());
                status.setText("Записів: " + model.getRowCount());
            } catch (SQLException ex) {
                error(ex.getMessage());
            }
        };
        showAll.run();

        all.addActionListener(e -> showAll.run());
        add.addActionListener(e -> {
            if (dbForm("Новий запис", null, null, null, null)) {
                showAll.run();
            }
        });
        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                error("Спочатку виділіть запис у таблиці.");
                return;
            }
            if (dbForm("Редагування запису", (Integer) table.getValueAt(row, 0),
                    (String) table.getValueAt(row, 1), table.getValueAt(row, 2), table.getValueAt(row, 3))) {
                showAll.run();
            }
        });
        del.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                error("Спочатку виділіть запис у таблиці.");
                return;
            }
            try {
                db.delete((Integer) table.getValueAt(row, 0));
                showAll.run();
            } catch (SQLException ex) {
                error(ex.getMessage());
            }
        });
        byPer.addActionListener(e -> {
            try {
                fillDb(model, db.selectByPerimeter(num(maxPer.getText())));
                status.setText("Знайдено: " + model.getRowCount());
            } catch (NumberFormatException ex) {
                error("Введіть число.");
            } catch (SQLException ex) {
                error(ex.getMessage());
            }
        });
        byType.addActionListener(e -> {
            try {
                fillDb(model, db.selectByType((String) typeBox.getSelectedItem()));
                status.setText("Знайдено: " + model.getRowCount());
            } catch (SQLException ex) {
                error(ex.getMessage());
            }
        });
        imp.addActionListener(e -> {
            try {
                int[] r = db.importFromFile("figures.txt");
                showAll.run();
                status.setText("Додано: " + r[0] + ", пропущено: " + r[1]);
            } catch (Exception ex) {
                error("Не вдалося імпортувати: " + ex.getMessage());
            }
        });
        recreate.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(parent, "Усі дані буде видалено. Продовжити?", "Підтвердження",
                    JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                db.recreate();
                showAll.run();
            } catch (SQLException ex) {
                error(ex.getMessage());
            }
        });

        JPanel top = new JPanel(new GridLayout(2, 1));
        top.add(flow(all, add, edit, del, imp, recreate));
        top.add(flow(byPer, maxPer, typeBox, byType));
        return panel(top, table, status);
    }

    private void fillDb(DefaultTableModel model, List<Object[]> rows) {
        model.setRowCount(0);
        for (Object[] r : rows) {
            model.addRow(new Object[] { r[0], r[1], r[2], r[3],
                    String.format("%.2f", (Double) r[4]), String.format("%.2f", (Double) r[5]) });
        }
    }

    
    private boolean dbForm(String title, Integer id, String type, Object w, Object h) {
        JComboBox<String> typeBox = new JComboBox<>(new String[] { "Rectangle", "Square", "Figure" });
        if (type != null) {
            typeBox.setSelectedItem(type);
        }
        JTextField wf = new JTextField(w == null ? "" : String.valueOf(w));
        JTextField hf = new JTextField(h == null ? "" : String.valueOf(h));
        Object[] form = { "Тип:", typeBox, "Ширина:", wf, "Висота:", hf };
        if (JOptionPane.showConfirmDialog(parent, form, title, JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) {
            return false;
        }
        try {
            String t = (String) typeBox.getSelectedItem();
            if (id == null) {
                db.insert(t, num(wf.getText()), num(hf.getText()));
            } else {
                db.update(id, t, num(wf.getText()), num(hf.getText()));
            }
            return true;
        } catch (NumberFormatException ex) {
            error("Ширина і висота мають бути числами.");
        } catch (SQLException ex) {
            error("БД відхилила запис (порушено обмеження):\n" + ex.getMessage()
                    + "\nПідказка: сторони > 0, а у Square ширина = висоті.");
        }
        return false;
    }
}
