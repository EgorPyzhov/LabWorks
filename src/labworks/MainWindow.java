package labworks;

import labworks.module1.Module1Panels;
import labworks.module2.FigureDatabase;
import labworks.module2.Module2Panels;

import javax.swing.*;


public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;

   
    public MainWindow(FigureDatabase db) {
        super("Лабораторні роботи 1–11 | Пижов Єгор");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(null);

        Module1Panels m1 = new Module1Panels();
        m1.setParent(this);
        Module2Panels m2 = new Module2Panels(db);
        m2.setParent(this);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("ЛР1", m1.buildCalculation(false));
        tabs.addTab("ЛР2", m1.buildCalculation(true));
        tabs.addTab("ЛР3", m1.buildCalculation(true));
        tabs.addTab("ЛР4", m1.buildLab4());
        tabs.addTab("ЛР5", m1.buildLab5());
        tabs.addTab("ЛР6", m1.buildLab6());
        tabs.addTab("ЛР7", m1.buildLab7());
        tabs.addTab("ЛР8", m2.buildLab8());
        tabs.addTab("ЛР9", m2.buildLab9());
        tabs.addTab("ЛР10", m2.buildLab10());
        tabs.addTab("ЛР11", m2.buildLab11());
        tabs.addTab("Про автора", new AuthorPanel());
        add(tabs);
    }
}
