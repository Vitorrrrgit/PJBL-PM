import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import models.Sistema; // Importar Sistema
import visual.LoginWindow;

public class Main {

    public static void main(String[] args) {
    
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Não foi possível aplicar o Look and Feel do sistema.");
        }

        Sistema sistema = new Sistema();

        SwingUtilities.invokeLater(() -> {
            System.out.println("Iniciando Sistema de Frequencia...");
            
            LoginWindow login = new LoginWindow(sistema);
            login.setVisible(true);
        });
    }
}