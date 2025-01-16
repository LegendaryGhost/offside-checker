package gui.utils;

import java.awt.Component;

import javax.swing.*;

import gui.Fenetre;

public class GuiUtils {
    
    public static Fenetre getParent(Component component){
        return (Fenetre) SwingUtilities.getWindowAncestor(component);
    }

    public static void message(Component component,String message){
        Fenetre fenetre = (Fenetre) SwingUtilities.getWindowAncestor(component);
        fenetre.addNotification(message);
    }

    public static JScrollPane getScrollPane(ImageIcon imageIcon) {
        // Mettre l'image dans un JLabel
        JLabel imageLabel = new JLabel(imageIcon);

        // Créer un JScrollPane pour gérer le défilement si l'image est grande
        JScrollPane scrollPane = new JScrollPane(imageLabel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return scrollPane;
    }
}
