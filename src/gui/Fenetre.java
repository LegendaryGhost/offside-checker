package gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import gui.listener.AnalyseButtonLayout;
import gui.listener.ChargerButtonLayout;
import gui.listener.SupprimerButtonLayout;

public class Fenetre extends JFrame {

    private JTextArea notificationArea;
    
    private static File image;

    public Fenetre() {
        // Définir le titre de la fenêtre
        super("Ma Fenêtre Swing");

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

        JPanel sidebarEast = new JPanel();
        sidebarEast.setBackground(Color.LIGHT_GRAY);
        sidebarEast.setPreferredSize(new Dimension(150, 0)); // Largeur fixe pour la barre latérale
        sidebarEast.setLayout(new BoxLayout(sidebarEast, BoxLayout.Y_AXIS));

        // Créer une zone de notifications (JTextArea) à droite
        notificationArea = new JTextArea(10, 20);  // 10 lignes, 20 caractères de large
        notificationArea.setEditable(false);  // Empêcher la modification manuelle
        notificationArea.setLineWrap(true);  // Permet de faire un retour à la ligne
        notificationArea.setWrapStyleWord(true); // Ajouter des retours à la ligne au mot entier
        JScrollPane scrollPane = new JScrollPane(notificationArea);  // Ajouter un scroll si nécessaire
        sidebarEast.add(scrollPane);

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
        add(sidebarEast, BorderLayout.EAST);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(100, 40)); // Taille maximale pour chaque bouton
        return button;
    }

    

    // Méthode pour ajouter des notifications dans le JTextArea
    public void addNotification(String message) {
        notificationArea.append(message + "\n");  // Ajouter le message à la fin avec un retour à la ligne
        notificationArea.setCaretPosition(notificationArea.getDocument().getLength());  // Faire défiler jusqu'en bas
    }

    public File getFile(){
        return image;
    }
    public void setImage(File file){
        image = file;
    }
}
