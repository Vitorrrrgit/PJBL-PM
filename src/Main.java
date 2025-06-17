import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException; // Garante que a classe LoginWindow seja encontrada
import visual.LoginWindow;

/**
 * Classe principal que inicia todo o sistema.
 */
public class Main {

    public static void main(String[] args) {
        // Configura a aparência da interface para se parecer com o sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        // CORREÇÃO: Capturando as exceções específicas para remover o alerta.
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Não foi possível aplicar o Look and Feel do sistema.");
        }

        // Inicia a aplicação na thread de eventos da interface gráfica
        SwingUtilities.invokeLater(() -> {
            System.out.println("Iniciando Sistema de Frequencia...");
            
            LoginWindow login = new LoginWindow();
            login.setVisible(true);
        });
    }
}
