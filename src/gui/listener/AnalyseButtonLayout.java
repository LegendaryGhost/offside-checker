package gui.listener;

import openCvUtils.*;
import openCvUtils.Rectangle;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import gui.Window;
import gui.utils.GuiUtils;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.List;

public class AnalyseButtonLayout implements ActionListener {

    private static JPanel content;

    public AnalyseButtonLayout(JPanel contente) {
	content = contente;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

	/*
	 * Load image
	 */
	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	Window window = GuiUtils.getParent(content);

	File imageFile = window.getFile(0);

	if (imageFile == null || !imageFile.exists()) {
	    JOptionPane.showMessageDialog(null, "Aucune image n'a été chargée !");
	    return;
	}

	// Charger l'image avec OpenCV
	Mat image = Imgcodecs.imread(imageFile.getAbsolutePath());

	if (image.empty()) {
	    JOptionPane.showMessageDialog(null, "Impossible de charger l'image !");
	    return;
	}

	// Convertir l'image en HSV
	/*
	 * Recupérer les points informations utiles pour l'analyse
	 */
	Mat hsvImage = new Mat();
	Imgproc.cvtColor(image, hsvImage, Imgproc.COLOR_BGR2HSV);

	Mat blueMask = ImageProcessingUtils.getBlueMask(hsvImage);
	Mat redMask = ImageProcessingUtils.getRedMask(hsvImage);
	Mat blackMask = ImageProcessingUtils.getBlackMask(hsvImage);

	List<Circle> bluePoints = ImageProcessingUtils.findCircles(blueMask, "blue");
	List<Circle> redPoints = ImageProcessingUtils.findCircles(redMask, "red");
	List<Circle> blackPoints = ImageProcessingUtils.findCircles(blackMask, "black");

	List<Rectangle> goals = ImageProcessingUtils.findGoals(image);
	for (Rectangle goal : goals) {
	    Imgproc.rectangle(image, goal.getRect(), new Scalar(0, 0, 255));
	}

	if (bluePoints.isEmpty() || redPoints.isEmpty() || blackPoints.isEmpty()) {
	    JOptionPane.showMessageDialog(null, "Impossible de faire l'analyse");
	    return;
	}

	/*
	 * Analyse des points
	 * 1 Rassembler tous les joueur, et trouver le joueur qui possède la balle
	 *
	 * 2 ordonner les joueur par rapport à Y croisant
	 * 3 trouver le sens de l'attaque
	 * 4 trouver les adva de l'equipe qui à le ballon
	 * 5 trouver le dernier defenseur

	 * 6 determiner si la ligne d'hors jeu est le ballon ou le dernier defenseur
	 * 7 tracer la ligne d'hors jeu
	 * 8 trouver les allier hors jeu
	 * 9 Afficher le resultat
	 * 10
	 */
	// 1
	List<Circle> allPlayer = ListUtils.gatherAll(bluePoints, redPoints);
	Circle ball = blackPoints.getFirst();
	Circle ballCarrier = Analyse.getPlayerClosestToBall(allPlayer, ball);

	// 2
	ListUtils.orderedAsc(allPlayer);

	// Afficher les coordonnées Y du point bleu sur l'image en blanc
	Imgproc.putText(image, "Porteur de balle", ballCarrier.getPoint(),
		Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 0), 1);

	// 3
	int direction = Analyse.findDirection(ballCarrier, allPlayer);

	// 4
	List<Circle> opponents = Analyse.findOpponent(allPlayer, ballCarrier);

	// 5
	ListUtils.orderedAsc(opponents);
	Circle lastdefense = null;
	if (direction == -1) {
	    lastdefense = opponents.get(1);
	}
	if (direction == 1) {
	    lastdefense = opponents.get(opponents.size() - 2);
	}

	// Afficher les coordonnées Y du point bleu sur l'image en blanc
	assert lastdefense != null;
	Imgproc.putText(image, "Dernier defenseur", lastdefense.getPoint(), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
		new Scalar(0, 0, 0), 1);

	// 6
	Circle hasOffsideLine = Analyse.findOffsideLine(lastdefense, ballCarrier, direction);

	// 7 Calculer la coordonnée Y de la ligne en fonction de la direction
	double yCoord = hasOffsideLine.getPoint().y;
	if (direction == 1) {
	    // Bord du bas
	    yCoord += hasOffsideLine.getRadius();
	} else {
	    // Bord du haut
	    yCoord -= hasOffsideLine.getRadius();
	}

	// Tracer une ligne horizontale sur la ligne d'hors-jeu en jaune
	Point start = new Point(0, yCoord);
	Point end = new Point(image.width(), yCoord);
	Imgproc.line(image, start, end, new Scalar(0, 255, 255), 2); // Yellow color with thickness 2

	// 8
	List<Circle> teamMates = Analyse.findOpponent(allPlayer, lastdefense);

	// 9 Vérifier les joueurs hors-jeu
	for (Circle circle : teamMates) {
	    if ((direction == 1 && circle.getPoint().y + circle.getRadius() > yCoord) || (direction == -1
		    && circle.getPoint().y - circle.getRadius() < yCoord)) {
		// Le joueur est hors-jeu
		Imgproc.putText(image, "Hors jeu", circle.getPoint(), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
			new Scalar(0, 0, 50), 1); // Red color for offside players
	    } else if ((direction == 1 && (circle.getPoint().y > ballCarrier.getPoint().y)) || (direction == -1 && (
		    circle.getPoint().y < ballCarrier.getPoint().y))) {
		// Le joueur est en position normale
		Imgproc.putText(image, "Normal", circle.getPoint(), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
			new Scalar(0, 50, 0), 1); // Green color for normal players

		// Dessiner une flèche de pWithBall vers pointRadius
		Point startPoint = ballCarrier.getPoint(); // Point de départ de la flèche
		Point endPoint = circle.getPoint(); // Point d'arrivée de la flèche
		Imgproc.arrowedLine(image, startPoint, endPoint, new Scalar(0, 0, 0),
			2); // Flèche bleue avec une épaisseur de 2
	    }
	}

	// Convertir l'image Mat en BufferedImage et l'afficher dans le JPanel
	BufferedImage bufferedImage = matToBufferedImage(image);
	displayImage(bufferedImage);
    }

    // Méthode pour convertir Mat (OpenCV) en BufferedImage
    private static BufferedImage matToBufferedImage(Mat mat) {
	// Créer un BufferedImage à partir de la Mat
	int width = mat.width();
	int height = mat.height();
	int channels = mat.channels();
	byte[] sourcePixels = new byte[width * height * channels];
	mat.get(0, 0, sourcePixels);

	// Créer un BufferedImage et y copier les pixels
	BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);
	return image;
    }

    // Méthode pour afficher l'image sur le JPanel
    private void displayImage(BufferedImage image) {
	// Créer un ImageIcon à partir du BufferedImage
	ImageIcon imageIcon = new ImageIcon(image);

	// Utiliser GuiUtils.getScrollPane pour obtenir un JScrollPane
	JScrollPane scrollPane = GuiUtils.getScrollPane(imageIcon);

	// Effacer le contenu actuel du JPanel
	content.removeAll();

	// Ajouter le JScrollPane à la fenêtre
	content.add(scrollPane, BorderLayout.CENTER);

	// Repeindre le JPanel pour afficher la nouvelle image
	content.revalidate();
	content.repaint();
    }
}
