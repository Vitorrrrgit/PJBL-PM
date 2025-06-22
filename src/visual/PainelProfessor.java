package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.time.LocalDate;
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
import models.Frequencia;
import models.Professor;
import models.Sistema;
import models.Turma;

public class PainelProfessor extends JPanel {

    private final Sistema sistema;
    private final Professor professor;
    private final boolean mostrarBotaoLogout;
    private final JComboBox<Turma> comboTurmas;
    private final JTable tabelaAlunos;
    private final DefaultTableModel tableModel;

    // Construtor com controle de logout (usado pela MainWindow)
    public PainelProfessor(Sistema sistema, Professor professor, boolean mostrarBotaoLogout) {
        this.sistema = sistema;
        this.professor = professor;
        this.mostrarBotaoLogout = mostrarBotaoLogout;
        
        this.setLayout(new BorderLayout(10, 10));

        String[] colunas = { "Matrícula", "Nome do Aluno", "Presente" };
        this.tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public Class<?> getColumnClass(int col) {
                return col == 2 ? Boolean.class : String.class;
            }
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 2;
            }
        };
        
        this.tabelaAlunos = new JTable(tableModel);

        List<Turma> turmasDoProfessor = sistema.buscarTurmasPorProfessor(professor);
        this.comboTurmas = new JComboBox<>(turmasDoProfessor.toArray(Turma[]::new));
        
        criarInterface();
        atualizarTabelaAlunos();
    }

    // Construtor de compatibilidade (modo standalone - com logout)
    public PainelProfessor(Sistema sistema, Professor professor) {
        this(sistema, professor, true);
    }

    private void criarInterface() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(" Registrar Frequência", criarAbaRegistro());
        tabbedPane.addTab(" Meu Perfil", criarAbaPerfil());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Só adiciona painel de logout se solicitado (modo standalone)
        if (mostrarBotaoLogout) {
            add(criarPainelSair(), BorderLayout.SOUTH);
        }
    }

    private JPanel criarAbaRegistro() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnSalvar = new JButton(" Salvar Frequências da Aula de Hoje");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        painelTopo.add(new JLabel(" Selecione a Turma:"));
        painelTopo.add(comboTurmas);
        painelTopo.add(btnSalvar);

        comboTurmas.addActionListener(e -> atualizarTabelaAlunos());
        btnSalvar.addActionListener(e -> salvarFrequencias());

        painel.add(painelTopo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaAlunos), BorderLayout.CENTER);
        
        return painel;
    }

    private JPanel criarAbaPerfil() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        
        // Informações do professor
        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new javax.swing.BoxLayout(painelInfo, javax.swing.BoxLayout.Y_AXIS));
        
        JLabel lblNome = new JLabel(" Professor: " + professor.getNome());
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JLabel lblEmail = new JLabel(" Email: " + professor.getEmail());
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel lblArea = new JLabel(" Área: " + professor.getArea());
        lblArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Estatísticas
        List<Turma> turmas = sistema.buscarTurmasPorProfessor(professor);
        JLabel lblTurmas = new JLabel("📚 Turmas sob responsabilidade: " + turmas.size());
        lblTurmas.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        int totalAlunos = turmas.stream().mapToInt(t -> t.getAlunosMatriculados().size()).sum();
        JLabel lblAlunos = new JLabel("👥 Total de alunos: " + totalAlunos);
        lblAlunos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        painelInfo.add(lblNome);
        painelInfo.add(new JLabel(" ")); // Espaço
        painelInfo.add(lblEmail);
        painelInfo.add(lblArea);
        painelInfo.add(new JLabel(" ")); // Espaço
        painelInfo.add(lblTurmas);
        painelInfo.add(lblAlunos);
        
        painel.add(painelInfo);
        
        // Botão alterar senha (só no modo standalone)
        if (mostrarBotaoLogout) {
            JButton btnAlterarSenha = new JButton(" Alterar Senha");
            btnAlterarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnAlterarSenha.setPreferredSize(new java.awt.Dimension(150, 35));
            btnAlterarSenha.addActionListener(e -> {
                Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
                new AlterarSenhaDialog(parent, sistema, professor).setVisible(true);
            });
            painel.add(btnAlterarSenha);
        }
        
        return painel;
    }

    private JPanel criarPainelSair() {
        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton(" Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setPreferredSize(new java.awt.Dimension(100, 35));
        btnLogout.setBackground(new java.awt.Color(231, 76, 60));
        btnLogout.setForeground(java.awt.Color.WHITE);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);

        btnLogout.addActionListener(e -> {
            int confirmacao = JOptionPane.showConfirmDialog(this, "Deseja realmente fazer logout?", "Confirmar Saída", JOptionPane.YES_NO_OPTION);
            if (confirmacao == JOptionPane.YES_OPTION) {
                Window janelaPrincipal = SwingUtilities.getWindowAncestor(this);
                if (janelaPrincipal != null) {
                    janelaPrincipal.dispose();
                }
                new LoginWindow(sistema).setVisible(true);
            }
        });

        painelSair.add(btnLogout);
        return painelSair;
    }

    private void atualizarTabelaAlunos() {
        tableModel.setRowCount(0);
        Turma turmaSelecionada = (Turma) comboTurmas.getSelectedItem();
        if (turmaSelecionada != null && turmaSelecionada.getAlunosMatriculados() != null) {
            for (Aluno aluno : turmaSelecionada.getAlunosMatriculados()) {
                tableModel.addRow(new Object[] { aluno.getMatricula(), aluno.getNome(), true });
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            tableModel.addRow(new Object[] { "Nenhum aluno matriculado", "", false });
        }
    }

    private void salvarFrequencias() {
        Turma turmaSelecionada = (Turma) comboTurmas.getSelectedItem();
        if (turmaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma turma selecionada!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (tableModel.getRowCount() == 0 || "Nenhum aluno matriculado".equals(tableModel.getValueAt(0, 0))) {
            JOptionPane.showMessageDialog(this, "Não há alunos para registrar frequência!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int registrosSalvos = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String matricula = (String) tableModel.getValueAt(i, 0);
            boolean presente = (boolean) tableModel.getValueAt(i, 2);
            int novoId = sistema.obterProximoIdFrequencia();
            
            Frequencia freq = new Frequencia(novoId, matricula, professor.getCpf(),
                    turmaSelecionada.getNomeDisciplina(), LocalDate.now(), presente,
                    presente ? "" : "Falta registrada pelo professor");
            
            sistema.adicionarFrequencia(freq);
            registrosSalvos++;
        }
        
        JOptionPane.showMessageDialog(this, 
            String.format(" %d registros de frequência salvos com sucesso!\n Data: %s", 
                registrosSalvos, LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))), 
            "Sucesso", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}