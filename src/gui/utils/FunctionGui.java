package gui.utils;

import java.awt.Component;

import javax.swing.SwingUtilities;



import gui.Fenetre;

public class FunctionGui {
    
    public static Fenetre getParent(Component component){
        return (Fenetre) SwingUtilities.getWindowAncestor(component);
    }

    public static void message(Component component,String message){
        Fenetre fenetre = (Fenetre) SwingUtilities.getWindowAncestor(component);
        fenetre.addNotification(message);
    }
}
