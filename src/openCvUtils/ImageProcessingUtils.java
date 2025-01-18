package openCvUtils;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class ImageProcessingUtils {

    public static List<Rectangle> findGoals(Mat image) {
	List<Line> lines = findLines(image);
	List<List<Line>> lineGroups = groupCloseLines(lines, 20);
	List<Rectangle> goals = new ArrayList<>();
	for (List<Line> lineGroup : lineGroups) {
	    if (lineGroup.size() <= 1)
		continue;

	    Point topLeftPoint = new Point(image.width(), image.height());
	    Point bottomRightPoint = new Point(0, 0);
	    for (Line line : lineGroup) {
		Point point1 = line.getPoint1(), point2 = line.getPoint2();

		if (point1.x < topLeftPoint.x || point1.y < topLeftPoint.y)
		    topLeftPoint = point1;
		if (point1.x > bottomRightPoint.x || point1.y > bottomRightPoint.y)
		    bottomRightPoint = point1;

		if (point2.x < topLeftPoint.x || point2.y < topLeftPoint.y)
		    topLeftPoint = point2;
		if (point2.x > bottomRightPoint.x || point2.y > bottomRightPoint.y)
		    bottomRightPoint = point2;
	    }
	    Rect rect = new Rect(
		    (int) topLeftPoint.x,
		    (int) topLeftPoint.y,
		    (int) (bottomRightPoint.x - topLeftPoint.x),
		    (int) (bottomRightPoint.y - topLeftPoint.y)
	    );
	    goals.add(new Rectangle(rect, "No colour"));
	}
	return goals;
    }

    public static List<List<Line>> groupCloseLines(List<Line> lines, double maxDistance) {
	List<List<Line>> groupedLines = new ArrayList<>();
	boolean[] visited = new boolean[lines.size()];

	for (int i = 0; i < lines.size(); i++) {
	    if (visited[i]) {
		continue;
	    }

	    List<Line> group = new ArrayList<>();
	    group.add(lines.get(i));
	    visited[i] = true;

	    for (int j = i + 1; j < lines.size(); j++) {
		if (!visited[j] && areLinesClose(lines.get(i), lines.get(j), maxDistance)) {
		    group.add(lines.get(j));
		    visited[j] = true;
		}
	    }

	    groupedLines.add(group);
	}

	return groupedLines;
    }

    private static boolean areLinesClose(Line line1, Line line2, double maxDistance) {
	return (distance(line1.getPoint1(), line2.getPoint1()) < maxDistance
		|| distance(line1.getPoint1(), line2.getPoint2()) < maxDistance
		|| distance(line1.getPoint2(), line2.getPoint1()) < maxDistance
		|| distance(line1.getPoint2(), line2.getPoint2()) < maxDistance);
    }

    private static double distance(Point p1, Point p2) {
	return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    private static List<Line> findLines(Mat image) {
	// Convert the image to grayscale
	Mat grayImage = new Mat();
	Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

	// Apply edge detection
	Mat edges = new Mat();
	Imgproc.Canny(grayImage, edges, 50, 150);

	// Detect lines using Hough Line Transform
	Mat linesMat = new Mat();
	Imgproc.HoughLinesP(edges, linesMat, 1, Math.PI / 180, 40, 20, 5);

	// Store detected lines in a list
	List<Line> lines = new ArrayList<>();
	for (int i = 0; i < linesMat.rows(); i++) {
	    double[] lineParams = linesMat.get(i, 0);
	    double x1 = lineParams[0], y1 = lineParams[1], x2 = lineParams[2], y2 = lineParams[3];
	    lines.add(new Line(x1, y1, x2, y2));
	}

	return lines;
    }

    public static List<Circle> findCircles(Mat mask, String color) {
	List<Circle> circles = new ArrayList<>();

	// Trouver les contours dans le masque
	List<MatOfPoint> contours = new ArrayList<>();
	Mat hierarchy = new Mat();
	Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

	// Parcourir chaque contour et calculer le cercle englobant
	for (MatOfPoint contour : contours) {
	    // Filtrer les petits contours
	    double area = Imgproc.contourArea(contour);
	    if (area < 20) { // Seuil d'aire (ajustez selon votre image)
		continue;
	    }
	    Point center = new Point();
	    float[] radius = new float[1];
	    Imgproc.minEnclosingCircle(new MatOfPoint2f(contour.toArray()), center, radius);

	    // Ajouter le centre et le rayon à la liste
	    circles.add(new Circle(center, radius[0], color));
	}

	return circles;
    }

    public static Mat getBlueMask(Mat hsvImage) {
	// Définir les plages pour chaque couleur en HSV
	// Plage de bleu (teinte entre 100 et 140)
	Scalar lowerBlue = new Scalar(100, 150, 50);
	Scalar upperBlue = new Scalar(140, 255, 255);

	// Appliquer les masques
	Mat blueMask = new Mat();
	Core.inRange(hsvImage, lowerBlue, upperBlue, blueMask);
	return blueMask;

    }

    public static Mat getRedMask(Mat hsvImage) {
	// Plage de rouge (teinte entre 0 et 10 et 170 et 180)
	Scalar lowerRed1 = new Scalar(0, 150, 50);
	Scalar upperRed1 = new Scalar(10, 255, 255);
	Scalar lowerRed2 = new Scalar(170, 150, 50);
	Scalar upperRed2 = new Scalar(180, 255, 255);

	// Appliquer les masques
	Mat redMask1 = new Mat();
	Core.inRange(hsvImage, lowerRed1, upperRed1, redMask1);

	Mat redMask2 = new Mat();
	Core.inRange(hsvImage, lowerRed2, upperRed2, redMask2);

	// Fusionner les deux masques rouges
	Mat redMask = new Mat();
	Core.addWeighted(redMask1, 1.0, redMask2, 1.0, 0.0, redMask);

	return redMask;

    }

    public static Mat getBlackMask(Mat hsvImage) {
	// Plage de noir (faible saturation et faible luminosité)
	Scalar lowerBlack = new Scalar(0, 0, 0);
	Scalar upperBlack = new Scalar(180, 255, 50);

	Mat blackMask = new Mat();
	Core.inRange(hsvImage, lowerBlack, upperBlack, blackMask);

	return blackMask;
    }

}
