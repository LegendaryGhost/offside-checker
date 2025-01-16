package gui.listener;

import javax.swing.*;

import gui.Fenetre;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class ChargerButtonLayout implements ActionListener {
    private final JPanel content;
  
    public ChargerButtonLayout(JPanel content) {
        this.content = content;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File lastLoadedFile = fileChooser.getSelectedFile();

            // Charger l'image
            JScrollPane scrollPane = getScrollPane(lastLoadedFile);

            // Mettre à jour le panneau de contenu
            content.removeAll();
            content.setLayout(new BorderLayout());
            content.add(scrollPane, BorderLayout.CENTER);
            content.revalidate();
            content.repaint();

            Fenetre fenetre = (Fenetre) SwingUtilities.getWindowAncestor(content);
            fenetre.setImage(lastLoadedFile);
            fenetre.addNotification("-> Image chargée avec succès ! Chemin de l'image : " + lastLoadedFile.getAbsolutePath());
        }
    }

    private static JScrollPane getScrollPane(File lastLoadedFile) {
        ImageIcon imageIcon = new ImageIcon(lastLoadedFile.getAbsolutePath());

        // Mettre l'image dans un JLabel
        JLabel imageLabel = new JLabel(imageIcon);

        // Créer un JScrollPane pour gérer le défilement si l'image est grande
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }
}
