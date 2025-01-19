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

public class Window extends JFrame {

    private static final File[] image = new File[2];

    public Window() {
	// Définir le titre de la fenêtre
	super("Offside checker");

	// Définir la taille de la fenêtre
	setSize(1200, 800);

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

	JPanel contentLeft = new JPanel();
	contentLeft.setBackground(Color.WHITE);
	contentLeft.setLayout(new BorderLayout());

	JPanel contentRight = new JPanel();
	contentRight.setBackground(Color.WHITE);
	contentRight.setLayout(new BorderLayout());

	// Ajouter un message par défaut
	JLabel defaultLabel = new JLabel("Aucune image sélectionnée", SwingConstants.CENTER);
	defaultLabel.setFont(new Font("Arial", Font.ITALIC, 16));
	contentLeft.add(defaultLabel, BorderLayout.CENTER);

	JLabel defaultLabel2 = new JLabel("Aucune image sélectionnée", SwingConstants.CENTER);
	defaultLabel2.setFont(new Font("Arial", Font.ITALIC, 16));
	contentRight.add(defaultLabel2, BorderLayout.CENTER);

	content.add(contentLeft, BorderLayout.WEST);
	content.add(contentRight, BorderLayout.EAST);

	// Ajouter un bouton "Charger" et l'associer au listener
	JButton loadButton = createButton("Charger image 1");
	loadButton.addActionListener(new ChargerButtonLayout(contentLeft, 0));
	menuBarBottom.add(loadButton);

	// Bouton pour faire pivoter l'image
	JButton rotateButton = createButton("Pivoter image 1");
	rotateButton.addActionListener(new RotateImageButtonLayout(contentLeft, 0));
	menuBarBottom.add(rotateButton);

	JButton deleteButton = createButton("Supprimer image 1");
	deleteButton.addActionListener(new SupprimerButtonLayout(contentLeft, 0));
	menuBarBottom.add(deleteButton);

	// Ajouter un bouton "Charger" et l'associer au listener
	JButton loadButton2 = createButton("Charger image 2");
	loadButton2.addActionListener(new ChargerButtonLayout(contentRight, 1));
	menuBarBottom.add(loadButton2);

	// Bouton pour faire pivoter l'image
	JButton rotateButton2 = createButton("Pivoter image 2");
	rotateButton2.addActionListener(new RotateImageButtonLayout(contentRight, 1));
	menuBarBottom.add(rotateButton2);

	JButton deleteButton2 = createButton("Supprimer image 2");
	deleteButton2.addActionListener(new SupprimerButtonLayout(contentRight, 1));
	menuBarBottom.add(deleteButton2);

	JButton analyseButton = createButton("Analyser");
	analyseButton.addActionListener(new AnalyseButtonLayout(contentLeft, contentRight));
	menuBarBottom.add(analyseButton);

	// Ajouter la barre latérale à droite et le contenu au centre
	add(menuBarBottom, BorderLayout.SOUTH);
	add(content, BorderLayout.CENTER);
    }

    private JButton createButton(String text) {
	JButton button = new JButton(text);
	button.setMaximumSize(new Dimension(150, 40)); // Taille maximale pour chaque bouton
	return button;
    }

    public File getFile(int index) {
	return image[index];
    }

    public BufferedImage getFileAsBufferedImage(int index) {
	if (image[index] != null) {
	    try {
		return ImageIO.read(image[index]);
	    } catch (IOException e) {
		System.out.println("Erreur lors de la lecture de l'image : " + e.getMessage());
	    }
	}
	return null;
    }

    public void setImage(File file, int index) {
	image[index] = file;
    }
}
