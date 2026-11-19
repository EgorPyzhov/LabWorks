package labworks;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class AuthorPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public AuthorPanel() {
        setLayout(null);

        JLabel photo = new JLabel("", SwingConstants.CENTER);
        photo.setBounds(25, 30, 210, 260);
        File f = new File("author.jpg");
        if (f.exists()) {
            Image img = new ImageIcon(f.getPath()).getImage();
            int w = img.getWidth(null);
            int h = img.getHeight(null);
            if (w > 0 && h > 0) {
                double k = Math.min(208.0 / w, 258.0 / h);
                photo.setIcon(new ImageIcon(img.getScaledInstance((int) (w * k), (int) (h * k), Image.SCALE_SMOOTH)));
            }

        }
        add(photo);

        JLabel info = new JLabel("<html>Пижов Єгор<br>"
                + "Група: КН-24002б (2 підгр.)<br>"
                + "Навчальний заклад: НУБіП України<br>"
                + "Варіант №12<br>"
                + "Мова програмування: Java<br>"
                + "Бібліотека GUI: Swing</html>");
        info.setFont(new Font("SansSerif", Font.PLAIN, 18));
        info.setVerticalAlignment(SwingConstants.TOP);
        info.setBounds(270, 30, 560, 260);
        add(info);
    }
}
