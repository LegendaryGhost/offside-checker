import javax.swing.SwingUtilities;

import gui.Fenetre;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Fenetre fenetre = new Fenetre();
            fenetre.setVisible(true);
        });
    }
}
