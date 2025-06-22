package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import models.Aluno;
import models.Coordenador;
import models.Frequencia;
import models.Sistema;
import models.Turma;
import models.Usuario;

public class PainelCoordenador extends JPanel {

    private final Sistema sistema;
    private final Coordenador coordenador;
    private final JComboBox<Turma> comboTurmas;
    private final DefaultTableModel tableModelFrequencias;

    public PainelCoordenador(Sistema sistema, Coordenador coordenador) {
        this.sistema = sistema;
        this.coordenador = coordenador;
        setLayout(new BorderLayout(10, 10));

        // Filtra as turmas para mostrar apenas as do curso do coordenador
        List<Turma> turmasDoCurso = sistema.buscarTurmasPorCurso(coordenador.getCurso().getNome());
        this.comboTurmas = new JComboBox<>(turmasDoCurso.toArray(new Turma[0]));

        // Adiciona a coluna "Professor" na tabela
        String[] colunasFreq = { "Data", "Professor", "Nome do Aluno", "Matrícula", "Status", "Observações" };
        this.tableModelFrequencias = new DefaultTableModel(colunasFreq, 0);
        JTable tabelaFrequencias = new JTable(tableModelFrequencias);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Relatórios do Curso", criarAbaRelatorios(tabelaFrequencias));
        tabbedPane.addTab("Meu Perfil", criarAbaPerfil());

        add(tabbedPane, BorderLayout.CENTER);
        add(criarPainelSair(), BorderLayout.SOUTH);

        // Atualiza a tabela com a primeira turma da lista, se houver
        if (comboTurmas.getItemCount() > 0) {
            atualizarTabelaFrequencias();
        }
    }

    private JPanel criarAbaRelatorios(JTable tabela) {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltro.add(new JLabel("Selecione a Turma para ver o relatório:"));
        painelFiltro.add(comboTurmas);
        comboTurmas.addActionListener(_ -> atualizarTabelaFrequencias());
        painel.add(painelFiltro, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarAbaPerfil() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        // PADRONIZAR COM OS OUTROS PAINÉIS
        JButton btnAlterarSenha = new JButton("Alterar Senha");
        btnAlterarSenha.addActionListener(_ -> 
            new AlterarSenhaDialog((Frame) SwingUtilities.getWindowAncestor(this), sistema, coordenador).setVisible(true)
        );
        
        painel.add(new JLabel("Usuário: " + coordenador.getNome()));
        painel.add(new JLabel(" | Coordenador do curso: " + coordenador.getCurso().getNome()));
        painel.add(btnAlterarSenha);
        
        return painel;
    }

    private void atualizarTabelaFrequencias() {
        tableModelFrequencias.setRowCount(0);
        Turma turmaSelecionada = (Turma) comboTurmas.getSelectedItem();

        if (turmaSelecionada != null) {
            String nomeProfessor = turmaSelecionada.getProfessorResponsavel().getNome();
            List<Frequencia> frequencias = sistema.buscarFrequenciasPorDisciplina(turmaSelecionada.getNomeDisciplina());

            for (Frequencia f : frequencias) {
                // Garante que a frequência pertence a um aluno da turma selecionada
                boolean alunoNaTurma = turmaSelecionada.getAlunosMatriculados().stream()
                        .anyMatch(a -> a.getMatricula().equals(f.getAlunoMatricula()));

                if (alunoNaTurma) {
                    String nomeAluno = sistema.listarUsuarios().stream()
                            .filter(u -> u instanceof Aluno && ((Aluno) u).getMatricula().equals(f.getAlunoMatricula()))
                            .map(Usuario::getNome)
                            .findFirst().orElse("Matrícula não encontrada");

                    // Adiciona a linha na tabela com o nome do professor
                    tableModelFrequencias.addRow(new Object[] {
                            f.getDataFormatada(),
                            nomeProfessor,
                            nomeAluno,
                            f.getAlunoMatricula(),
                            f.getStatus(),
                            f.getObservacoes()
                    });
                }
            }
        }
    }

    private JPanel criarPainelSair() {
        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Logout");
        
        btnLogout.addActionListener(_ -> {
            int confirmacao = JOptionPane.showConfirmDialog(
                this, 
                "Deseja realmente fazer logout?", 
                "Confirmar Saída", 
                JOptionPane.YES_NO_OPTION
            );
            if (confirmacao == JOptionPane.YES_OPTION) {
                Window janela = SwingUtilities.getWindowAncestor(this);
                if (janela != null) janela.dispose();
                new LoginWindow(sistema).setVisible(true);
            }
        });
        
        painelSair.add(btnLogout);
        return painelSair;
    }
}