package openCvUtils;

import org.opencv.core.Rect;

public class Rectangle {

    private final Rect rect;
    private String color;

    public Rectangle(Rect rect, String color) {
	this.rect = rect;
	this.color = color;
    }

    public Rect getRect() {
	return rect;
    }

    public String getColor() {
	return color;
    }

    public void setColor(String color) {
	this.color = color;
    }

    @Override
    public String toString() {
	return "Rectangle{" + "color='" + color + '\'' + ", x=" + rect.x + ", y=" + rect.y + ", width=" + rect.width + ", height="
		+ rect.height + '}';
    }
}
