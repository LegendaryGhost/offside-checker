package openCvUtils;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class ImageProcessingUtils {

    public static List<Line> findLines(Mat image) {
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
