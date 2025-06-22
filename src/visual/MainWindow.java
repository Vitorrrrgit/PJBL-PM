package visual;

import javax.swing.*;
import models.*;

public class MainWindow extends JFrame {

    /**
     * Construtor da janela principal do sistema
     * @param sistema A instância única do sistema.
     * @param usuarioLogado O usuário que fez o login.
     */
    public MainWindow(Sistema sistema, Usuario usuarioLogado) {
        super("Sistema de Frequência");
        setSize(950, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel painelPrincipal;

        // Lógica central que decide qual painel carregar baseado no tipo de usuário
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
                setTitle("Painel Administrativo - " + admin.getNome());
            }
            default -> {
                painelPrincipal = new JPanel();
                painelPrincipal.add(new JLabel("Erro: Tipo de usuário não reconhecido."));
            }
        }

        add(painelPrincipal);
    }
}