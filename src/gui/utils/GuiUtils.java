package gui.utils;

import java.awt.Component;

import javax.swing.*;

import gui.Window;

public class GuiUtils {
    
    public static Window getParent(Component component){
        return (Window) SwingUtilities.getWindowAncestor(component);
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
