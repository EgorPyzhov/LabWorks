package labworks;

import labworks.module2.FigureDatabase;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Locale;


public class Main {
   
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        SwingUtilities.invokeLater(() -> {
            try {
                FigureDatabase db = new FigureDatabase("jdbc:sqlite:figures.db");
                new MainWindow(db).setVisible(true);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Не вдалося підключитися до БД: " + e.getMessage(),
                        "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
