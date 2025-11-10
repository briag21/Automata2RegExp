
import javax.swing.SwingUtilities;

import com.automata2regexp.ui_graphique.FenetrePrincipale;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetrePrincipale fenetre = new FenetrePrincipale();
            fenetre.setVisible(true);
        });
    }
}