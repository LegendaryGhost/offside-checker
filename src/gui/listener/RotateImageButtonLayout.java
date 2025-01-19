package gui.listener;

import gui.Window;
import gui.utils.GuiUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RotateImageButtonLayout implements ActionListener {
    private final JPanel content;
    private final int imageIndex;

    public RotateImageButtonLayout(JPanel content, int imageIndex) {
	this.content = content;
	this.imageIndex = imageIndex;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	Window window = (Window) SwingUtilities.getWindowAncestor(content);
	// L'image en cours de traitement
	BufferedImage currentImage = window.getFileAsBufferedImage(0);

	if (currentImage != null) {
	    // Appliquer la rotation
	    double radians = Math.toRadians(90);
	    double sin = Math.abs(Math.sin(radians));
	    double cos = Math.abs(Math.cos(radians));
	    int width = currentImage.getWidth();
	    int height = currentImage.getHeight();
	    int newWidth = (int) Math.floor(width * cos + height * sin);
	    int newHeight = (int) Math.floor(height * cos + width * sin);

	    BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, currentImage.getType());
	    Graphics2D g2d = rotatedImage.createGraphics();
	    AffineTransform at = new AffineTransform();
	    at.translate((newWidth - width) / 2.0, (newHeight - height) / 2.0);
	    at.rotate(radians, width / 2.0, height / 2.0);
	    g2d.drawRenderedImage(currentImage, at);
	    g2d.dispose();

	    // Mettre à jour l'image affichée
	    displayImage(rotatedImage);

	    // Sauvegarder l'image pivotée et remplacer le fichier dans Fenetre
	    String formatName = "jpg"; // ou "jpg" selon le format souhaité
	    String outputPath = "rotated_image.jpg"; // ou un autre chemin temporaire
	    File newFile = saveBufferedImageToFile(rotatedImage, formatName, outputPath);

	    if (newFile.exists()) {
		window.setImage(newFile, imageIndex); // Remplacer l'ancien fichier
	    }
	}
    }

    // Afficher l'image dans le panneau
    private void displayImage(BufferedImage image) {
	ImageIcon imageIcon = new ImageIcon(image);
	JScrollPane scrollPane = GuiUtils.getScrollPane(imageIcon);

	content.removeAll();
	content.add(scrollPane, BorderLayout.CENTER);
	content.revalidate();
	content.repaint();
    }

    public static File saveBufferedImageToFile(BufferedImage image, String formatName, String outputPath) {
	File outputFile = new File(outputPath);
	try {
	    ImageIO.write(image, formatName, outputFile);
	} catch (IOException e) {
	    System.out.println("Erreur lors de la sauvegarde de l'image : " + e.getMessage());
	}
	return outputFile;
    }
}