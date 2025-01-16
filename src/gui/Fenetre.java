package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import gui.listener.AnalyseButtonLayout;
import gui.listener.ChargerButtonLayout;
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

	setResizable(false);

	// Initialiser les composants
	initComponents();
    }

    private void initComponents() {
	// Définir un BorderLayout pour organiser la fenêtre
	setLayout(new BorderLayout());

	// Création de la barre latérale
	JPanel sidebarWest = new JPanel();
	sidebarWest.setBackground(Color.LIGHT_GRAY);
	sidebarWest.setPreferredSize(new Dimension(100, 0)); // Largeur fixe pour la barre latérale
	sidebarWest.setLayout(new BoxLayout(sidebarWest, BoxLayout.Y_AXIS));

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
	sidebarWest.add(loadButton);

	JButton SuppImage = createButton("Supprimer Image");
	SuppImage.addActionListener(new SupprimerButtonLayout(content));
	sidebarWest.add(SuppImage);

	JButton beginAnalyse = createButton("Analyser");
	beginAnalyse.addActionListener(new AnalyseButtonLayout(content));
	sidebarWest.add(beginAnalyse);

	// Ajouter la barre latérale à droite et le contenu au centre
	add(sidebarWest, BorderLayout.WEST);
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

    public void setImage(File file) {
	image = file;
    }
}
