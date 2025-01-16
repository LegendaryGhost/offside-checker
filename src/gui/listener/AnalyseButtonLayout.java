package gui.listener;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import gui.Window;
import gui.utils.GuiUtils;
import openCvUtils.Analyse;
import openCvUtils.Utils;
import openCvUtils.PointRadius;

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

	File imageFile = window.getFile();

	if (imageFile == null || !imageFile.exists()) {
	    JOptionPane.showMessageDialog(null, "Aucune image n'a été chargée !");
	    return;
	}

	// Charger l'image avec OpenCV
	System.out.println("Chemin de l'image : " + imageFile.getAbsolutePath());
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

	Mat blueMask = Utils.getBlueMask(hsvImage);
	Mat redMask = Utils.getRedMask(hsvImage);
	Mat black = Utils.getNiggaMask(hsvImage);

	List<PointRadius> bluePoints = Utils.findPointsWithRadius(blueMask, "blue");
	List<PointRadius> redPoints = Utils.findPointsWithRadius(redMask, "red");
	List<PointRadius> blackPoints = Utils.findPointsWithRadius(black, "black");

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
	List<PointRadius> allPlayer = Utils.gatherAll(bluePoints, redPoints);
	PointRadius ball = blackPoints.getFirst();
	PointRadius ballCarrier = Analyse.getPlayerClosestToBall(allPlayer, ball);

	// 2
	Utils.orderedAsc(allPlayer);

	// Afficher les coordonnées Y du point bleu sur l'image en blanc
	Imgproc.putText(image, String.format("Porteur de balle", ballCarrier.getPoint().y), ballCarrier.getPoint(),
		Imgproc.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 0), 1);

	// 3
	int direction = Analyse.findDirection(ballCarrier, allPlayer);

	// 4
	List<PointRadius> opponents = Analyse.findOpponent(allPlayer, ballCarrier);

	// 5
	Utils.orderedAsc(opponents);
	GuiUtils.message(content, "Les defenseur est l'equipe " + opponents.getFirst().getColor());
	PointRadius lastdefense = null;
	if (direction == -1) {
	    GuiUtils.message(content, "Miakatra");
	    lastdefense = opponents.get(1);

	}
	if (direction == 1) {
	    GuiUtils.message(content, "Midina");
	    lastdefense = opponents.get(opponents.size() - 2);
	}

	// Afficher les coordonnées Y du point bleu sur l'image en blanc
	assert lastdefense != null;
	Imgproc.putText(image, "Dernier defenseur", lastdefense.getPoint(), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
		new Scalar(0, 0, 0), 1);

	// 6
	PointRadius hasOffsideLine = Analyse.findOffsideLine(lastdefense, ballCarrier, direction);

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
	List<PointRadius> teamMates = Analyse.findOpponent(allPlayer, lastdefense);

	// 9 Vérifier les joueurs hors-jeu
	for (PointRadius pointRadius : teamMates) {
	    if ((direction == 1 && pointRadius.getPoint().y + pointRadius.getRadius() > yCoord) || (direction == -1
		    && pointRadius.getPoint().y - pointRadius.getRadius() < yCoord)) {
		// Le joueur est hors-jeu
		Imgproc.putText(image, "Hors jeu", pointRadius.getPoint(), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
			new Scalar(0, 0, 50), 1); // Red color for offside players
	    } else if ((direction == 1 && (pointRadius.getPoint().y > ballCarrier.getPoint().y)) || (direction == -1 && (
		    pointRadius.getPoint().y < ballCarrier.getPoint().y))) {
		// Le joueur est en position normale
		Imgproc.putText(image, "Normal", pointRadius.getPoint(), Imgproc.FONT_HERSHEY_SIMPLEX, 0.5,
			new Scalar(0, 50, 0), 1); // Green color for normal players

		// Dessiner une flèche de pWithBall vers pointRadius
		Point startPoint = ballCarrier.getPoint(); // Point de départ de la flèche
		Point endPoint = pointRadius.getPoint(); // Point d'arrivée de la flèche
		Imgproc.arrowedLine(image, startPoint, endPoint, new Scalar(0, 0, 0),
			2); // Flèche bleue avec une épaisseur de 2
	    }
	}

	// Convertir l'image Mat en BufferedImage et l'afficher dans le JPanel
	BufferedImage bufferedImage = matToBufferedImage(image);
	displayImage(bufferedImage);
    }

    private static void displayDetectedPoints(Mat image, List<PointRadius> bluePoints, List<PointRadius> redPoints,
	    List<PointRadius> blackPoints) {
	// Dessiner un cercle bleu pour chaque point bleu et afficher les coordonnées Y en blanc
	for (PointRadius pr : bluePoints) {
	    // Dessiner le point principal en bleu
	    Imgproc.circle(image, pr.getPoint(), (int) pr.getRadius(), new Scalar(255, 0, 0), -1); // Blue

	}

	// Dessiner un cercle rouge pour chaque point rouge et afficher les coordonnées Y en blanc
	for (PointRadius pr : redPoints) {
	    // Dessiner le point principal en rouge
	    Imgproc.circle(image, pr.getPoint(), (int) pr.getRadius(), new Scalar(0, 0, 255), -1); // Red
	}

	// Dessiner un cercle noir pour chaque point noir et afficher les coordonnées Y en blanc
	for (PointRadius pr : blackPoints) {
	    // Dessiner le point principal en noir
	    Imgproc.circle(image, pr.getPoint(), (int) pr.getRadius(), new Scalar(0, 0, 0), -1); // Black
	}
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
