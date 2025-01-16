package gui.listener;

import gui.Fenetre;
import gui.utils.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class RotateImageButtonLayout implements ActionListener {
    private final JPanel content;
    private BufferedImage currentImage; // L'image en cours de traitement
    private int rotationAngle = 0; // Angle de rotation en degrés

    public RotateImageButtonLayout(JPanel content) {
	this.content = content;
    }

    public void setCurrentImage(BufferedImage image) {
	this.currentImage = image;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	Fenetre fenetre = (Fenetre) SwingUtilities.getWindowAncestor(content);
	currentImage = fenetre.getFileAsBufferedImage();

	if (currentImage != null) {
	    // Augmenter l'angle de 90° à chaque clic
	    rotationAngle = (rotationAngle + 90) % 360;

	    // Appliquer la rotation
	    double radians = Math.toRadians(rotationAngle);
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
}