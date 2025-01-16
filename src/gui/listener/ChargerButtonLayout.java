package gui.listener;

import javax.swing.*;

import gui.Window;
import gui.utils.GuiUtils;

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
            ImageIcon imageIcon = new ImageIcon(lastLoadedFile.getAbsolutePath());
            JScrollPane scrollPane = GuiUtils.getScrollPane(imageIcon);

            // Mettre à jour le panneau de contenu
            content.removeAll();
            content.setLayout(new BorderLayout());
            content.add(scrollPane, BorderLayout.CENTER);
            content.revalidate();
            content.repaint();

            Window window = (Window) SwingUtilities.getWindowAncestor(content);
            window.setImage(lastLoadedFile);
            window.addNotification("-> Image chargée avec succès ! Chemin de l'image : " + lastLoadedFile.getAbsolutePath());
        }
    }
}
