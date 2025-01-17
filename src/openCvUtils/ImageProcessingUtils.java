package openCvUtils;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class ImageProcessingUtils {

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

    public static List<Point> findPoints(Mat mask) {
	List<Point> points = new ArrayList<>();
	// Trouver les contours
	List<MatOfPoint> contours = new ArrayList<>();
	Mat hierarchy = new Mat();
	Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

	// Trouver le centre de chaque contour
	for (MatOfPoint contour : contours) {
	    Moments moments = Imgproc.moments(contour);
	    if (moments.m00 != 0) {
		int x = (int) (moments.m10 / moments.m00);
		int y = (int) (moments.m01 / moments.m00);
		points.add(new Point(x, y));
	    }
	}
	return points;
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
