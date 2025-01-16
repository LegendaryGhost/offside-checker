package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import gui.listener.AnalyseButtonLayout;
import gui.listener.ChargerButtonLayout;
import gui.listener.RotateImageButtonLayout;
import gui.listener.SupprimerButtonLayout;

public class Fenetre extends JFrame {

    private static File image;

    public Fenetre() {
	// Définir le titre de la fenêtre
	super("Offside checker");

	// Définir la taille de la fenêtre
	setSize(1000, 800);

	// Spécifier l'action par défaut lors de la fermeture de la fenêtre
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Centrer la fenêtre sur l'écran
	setLocationRelativeTo(null);

	setResizable(true);

	// Initialiser les composants
	initComponents();
    }

    private void initComponents() {
	// Définir un BorderLayout pour organiser la fenêtre
	setLayout(new BorderLayout());

	// Création de la barre latérale
	JPanel menuBarBottom = new JPanel();
	menuBarBottom.setBackground(Color.LIGHT_GRAY);
	menuBarBottom.setPreferredSize(new Dimension(0, 30)); // Largeur fixe pour la barre latérale
	menuBarBottom.setLayout(new BoxLayout(menuBarBottom, BoxLayout.X_AXIS));

	// Création de la zone de contenu
	JPanel content = new JPanel();
	content.setBackground(Color.WHITE);
	content.setLayout(new BorderLayout());

	// Ajouter un message par défaut
	JLabel defaultLabel = new JLabel("Aucune image sélectionnée", SwingConstants.CENTER);
	defaultLabel.setFont(new Font("Arial", Font.ITALIC, 16));
	content.add(defaultLabel, BorderLayout.CENTER);

	// Ajouter un bouton "Charger" et l'associer au listener
	JButton loadButton = createButton("Charger");
	loadButton.addActionListener(new ChargerButtonLayout(content));
	menuBarBottom.add(loadButton);

	// Bouton pour faire pivoter l'image
	JButton rotateButton = createButton("Pivoter");
	rotateButton.addActionListener(new RotateImageButtonLayout(content));
	menuBarBottom.add(rotateButton);

	JButton SuppImage = createButton("Supprimer Image");
	SuppImage.addActionListener(new SupprimerButtonLayout(content));
	menuBarBottom.add(SuppImage);

	JButton beginAnalyse = createButton("Analyser");
	beginAnalyse.addActionListener(new AnalyseButtonLayout(content));
	menuBarBottom.add(beginAnalyse);

	// Ajouter la barre latérale à droite et le contenu au centre
	add(menuBarBottom, BorderLayout.SOUTH);
	add(content, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
	JButton button = new JButton(text);
	button.setMaximumSize(new Dimension(100, 40)); // Taille maximale pour chaque bouton
	return button;
    }

    // Méthode pour ajouter des notifications dans le JTextArea
    public void addNotification(String message) {
	System.out.println(message);
    }

    public File getFile() {
	return image;
    }

    public BufferedImage getFileAsBufferedImage() {
	if (image != null) {
	    try {
		return ImageIO.read(image);
	    } catch (IOException e) {
		e.printStackTrace();
		System.out.println("Erreur lors de la lecture de l'image : " + e.getMessage());
	    }
	}
	return null;
    }

    public void setImage(File file) {
	image = file;
    }
}
