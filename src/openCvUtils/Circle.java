package openCvUtils;

import org.opencv.core.Point;

public class Circle {

    private Point point;
    private double radius;
    private String color;

    public Circle(Point point, double radius, String color) {
        this.point = point;
        this.radius = radius;
        this.color = color;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
