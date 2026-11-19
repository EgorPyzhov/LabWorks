package labworks.module2;

import java.io.Serializable;


public class GeometricFigure implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private double sideA;
    private double sideB;

   
    public GeometricFigure(String name, double sideA, double sideB) {
        this.name = name;
        this.sideA = sideA;
        this.sideB = sideB;
    }

    public String getName() {
        return name;
    }

    public double getSideA() {
        return sideA;
    }

    public double getSideB() {
        return sideB;
    }

    public double getArea() {
        return sideA * sideB;
    }

    public double getPerimeter() {
        return 2 * (sideA + sideB);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSideA(double sideA) {
        this.sideA = sideA;
    }

    public void setSideB(double sideB) {
        this.sideB = sideB;
    }
}
