package visual;

import javax.swing.*;
import models.*;

public class MainWindow extends JFrame {

    /**
     * --- CORREÇÃO: Construtor modificado ---
     * @param sistema A instância única do sistema.
     * @param usuarioLogado O usuário que fez o login.
     */
    public MainWindow(Sistema sistema, Usuario usuarioLogado) {
        super("Sistema de Frequência");
        setSize(950, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // REMOVE a criação de uma nova instância. Usa a que foi recebida.
        // Sistema sistema = new Sistema(); 
        JPanel painelPrincipal;

        // Lógica central que decide qual painel carregar
        switch (usuarioLogado) {
            case Aluno aluno -> {
                painelPrincipal = new PainelAluno(sistema, aluno);
                setTitle("Portal do Aluno - " + aluno.getNome());
            }
            case Professor professor -> {
                painelPrincipal = new PainelProfessor(sistema, professor);
                setTitle("Portal do Professor - " + professor.getNome());
            }
            case Coordenador coordenador -> {
                painelPrincipal = new PainelCoordenador(sistema, coordenador);
                setTitle("Portal do Coordenador - " + coordenador.getNome());
            }
            case Administrador admin -> {
                painelPrincipal = new PainelAdmin(sistema, admin);
                setTitle("Painel Administrativo");
            }
            default -> {
                painelPrincipal = new JPanel();
                painelPrincipal.add(new JLabel("Erro: Tipo de usuário não reconhecido."));
            }
        }

        add(painelPrincipal);
    }
}