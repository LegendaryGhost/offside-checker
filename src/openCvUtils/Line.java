package openCvUtils;

import org.opencv.core.Point;

public class Line {
    
    private final Point point1;
    private final Point point2;
    
    public Line(double x1, double y1, double x2, double y2) {
	this.point1 = new Point(x1, y1);
	this.point2 = new Point(x2, y2);
    }
    
    public Point getPoint1() {
	return point1;
    }
    
    public Point getPoint2() {
	return point2;
    }

    @Override
    public String toString() {
	return "Line{" + "x1=" + point1.x + ", y1=" + point1.y + ", x2=" + point2.x + ", y2=" + point2.y + '}';
    }
}
