package openCvUtils;

import org.opencv.core.Point;

public class Circle {

    Point point;
    double radius;
    String color;

    public Circle(Point point, double radius) {
        this.point = point;
        this.radius = radius;
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

    public Circle(Point point, double radius, String color) {
        this.point = point;
        this.radius = radius;
        this.color = color;
    }

}
