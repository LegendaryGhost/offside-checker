package gui.listener;

import javax.swing.*;

import gui.Fenetre;
import gui.utils.GuiUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SupprimerButtonLayout implements ActionListener {

    private final JPanel contentPanel;

    public SupprimerButtonLayout(JPanel contentPanel) {
        this.contentPanel = contentPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Supprimer le contenu actuel
        contentPanel.removeAll();

        // Ajouter un message par défaut
        JLabel defaultLabel = new JLabel("Aucune image sélectionnée", SwingConstants.CENTER);
        defaultLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        contentPanel.add(defaultLabel, BorderLayout.CENTER);

        // Mettre à jour l'affichage
        contentPanel.revalidate();
        contentPanel.repaint();

        Fenetre fenetre = GuiUtils.getParent(contentPanel);
        fenetre.setImage(null);

    }
}
