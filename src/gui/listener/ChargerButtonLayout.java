package gui.listener;

import javax.swing.*;

import gui.Fenetre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;


public class ChargerButtonLayout implements ActionListener {
    private JPanel content;
  
    public ChargerButtonLayout(JPanel content) {
        this.content = content;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File lastLoadedFile = fileChooser.getSelectedFile();

            // Afficher l'image chargée dans le panneau de contenu
            ImageIcon imageIcon = new ImageIcon(lastLoadedFile.getAbsolutePath());
            JLabel imageLabel = new JLabel(imageIcon);
            content.removeAll();
            content.add(imageLabel);
            content.revalidate();
            content.repaint();

            Fenetre fenetre = (Fenetre) SwingUtilities.getWindowAncestor(content);
            fenetre.setImage(lastLoadedFile);
            fenetre.addNotification("-> Image chargée avec succès ! Chemin de l'image : "+lastLoadedFile.getAbsolutePath());
            
        }
    }
}
