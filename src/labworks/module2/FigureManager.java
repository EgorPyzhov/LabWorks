package labworks.module2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;


public class FigureManager {
    private GeometricFigure[] figures;

    public FigureManager() {
        figures = new GeometricFigure[] {
                new GeometricFigure("Rectangle", 5, 6),
                new GeometricFigure("Square", 4, 4),
                new GeometricFigure("Figure", 3, 7),
                new GeometricFigure("Rectangle", 8, 5),
                new GeometricFigure("Square", 6, 6),
                new GeometricFigure("Figure", 2, 9),
                new GeometricFigure("Rectangle", 7, 4),
                new GeometricFigure("Square", 3, 3),
                new GeometricFigure("Figure", 5, 8),
                new GeometricFigure("Rectangle", 9, 6)
        };
    }


    public List<GeometricFigure> getAll() {
        return new ArrayList<>(Arrays.asList(figures));
    }

  
    public int indexOf(GeometricFigure f) {
        for (int i = 0; i < figures.length; i++) {
            if (figures[i] == f) {
                return i;
            }
        }
        return -1;
    }

    public void modifyFigure(int index, String name, double a, double b) {
        if (index < 0 || index >= figures.length) {
            throw new IllegalArgumentException("Неправильний номер фігури.");
        }
        if (name == null || name.trim().isEmpty() || !(a > 0) || !(b > 0)) {
            throw new IllegalArgumentException("Назва не повинна бути порожньою, а сторони мають бути додатними.");
        }
        figures[index].setName(name.trim());
        figures[index].setSideA(a);
        figures[index].setSideB(b);
    }

    
    public List<GeometricFigure> findByArea(double minArea) {
        List<GeometricFigure> result = new ArrayList<>();
        for (GeometricFigure f : figures) {
            if (f.getArea() > minArea) {
                result.add(f);
            }
        }
        return result;
    }

   
    public void sortByArea(boolean ascending) {
        Comparator<GeometricFigure> c = Comparator.comparingDouble(GeometricFigure::getArea);
        Arrays.sort(figures, ascending ? c : c.reversed());
    }


   
    public void sortByName(boolean ascending) {
        Comparator<GeometricFigure> c = (x, y) -> x.getName().compareToIgnoreCase(y.getName());
        Arrays.sort(figures, ascending ? c : c.reversed());
    }

   
    public List<GeometricFigure> findByNameRegex(String regex) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        List<GeometricFigure> result = new ArrayList<>();
        for (GeometricFigure f : figures) {
            if (p.matcher(f.getName()).matches()) {
                result.add(f);
            }
        }
        return result;
    }

   
    public String getMinMaxAreaText() {
        if (figures.length == 0) {
            return "Масив порожній.";
        }
        GeometricFigure min = figures[0];
        GeometricFigure max = figures[0];
        for (GeometricFigure f : figures) {
            if (f.getArea() < min.getArea()) {
                min = f;
            }
            if (f.getArea() > max.getArea()) {
                max = f;
            }
        }
        return String.format(
                "Найменшу площу %.2f має фігура %s (сторони %.2f і %.2f), "
                        + "найбільшу - %.2f - фігура %s (сторони %.2f і %.2f).",
                min.getArea(), min.getName(), min.getSideA(), min.getSideB(),
                max.getArea(), max.getName(), max.getSideA(), max.getSideB());
    }

  
    public int loadFromTextFile(String fileName) throws IOException {
        List<GeometricFigure> list = new ArrayList<>();
        int skipped = 0;
        for (String line : Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8)) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] d = line.split(";");
            try {
                if (d.length != 3) {
                    throw new NumberFormatException();
                }
                String name = d[0].trim();
                double a = Double.parseDouble(d[1].trim().replace(',', '.'));
                double b = Double.parseDouble(d[2].trim().replace(',', '.'));
                if (name.isEmpty() || !(a > 0) || !(b > 0) || Double.isInfinite(a) || Double.isInfinite(b)) {
                    throw new NumberFormatException();
                }
                list.add(new GeometricFigure(name, a, b));
            } catch (NumberFormatException e) {
                skipped++;
            }
        }
        if (list.isEmpty()) {
            throw new IOException("У файлі немає коректних даних. Масив не змінено.");
        }
        figures = list.toArray(new GeometricFigure[0]);
        return skipped;
    }

  
    public void saveToBinaryFile(String fileName) throws IOException {
        sortByName(true);
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(figures);
        }
    }

 
    public void readBinaryFile(String fileName) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            figures = (GeometricFigure[]) in.readObject();
        } catch (ClassNotFoundException | ClassCastException e) {
            throw new IOException("Неправильний формат файлу.", e);
        }
    }
}
