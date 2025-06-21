package visual;

import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.*;

public class PainelProfessor extends JPanel {

    private final Sistema sistema;
    private final Professor professor;
    private final JComboBox<Turma> comboTurmas;
    private final JTable tabelaAlunos;
    private final DefaultTableModel tableModel;

    public PainelProfessor(Sistema sistema, Professor professor) {
        this.sistema = sistema;
        this.professor = professor;
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
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Registrar Frequência", criarAbaRegistro());
        tabbedPane.addTab("Meu Perfil", criarAbaPerfil());
        add(tabbedPane, BorderLayout.CENTER);
        add(criarPainelSair(), BorderLayout.SOUTH);

        atualizarTabelaAlunos();
    }

    private JPanel criarAbaRegistro() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSalvar = new JButton("Salvar Frequências da Aula de Hoje");

        painelTopo.add(new JLabel("Selecione a Turma:"));
        painelTopo.add(comboTurmas);
        painelTopo.add(btnSalvar);

        comboTurmas.addActionListener(_ -> atualizarTabelaAlunos());
        btnSalvar.addActionListener(_ -> salvarFrequencias());

        painel.add(painelTopo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaAlunos), BorderLayout.CENTER);
        return painel;
    }

    private JPanel criarAbaPerfil() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAlterarSenha = new JButton("Alterar Senha");
        btnAlterarSenha.addActionListener(
                _ -> new AlterarSenhaDialog((Frame) SwingUtilities.getWindowAncestor(this), sistema, professor)
                        .setVisible(true));
        painel.add(new JLabel("Usuário: " + professor.getNome()));
        painel.add(btnAlterarSenha);
        return painel;
    }

    private JPanel criarPainelSair() {
        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelSair.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JButton btnLogout = new JButton("Logout");

        btnLogout.addActionListener(_ -> {
            int confirmacao = JOptionPane.showConfirmDialog(
                    this,
                    "Deseja realmente fazer logout e voltar para a tela de login?",
                    "Confirmar Saída",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacao == JOptionPane.YES_OPTION) {
                Window janelaPrincipal = SwingUtilities.getWindowAncestor(this);

                if (janelaPrincipal != null) {
                    janelaPrincipal.dispose();
                }

                new LoginWindow().setVisible(true);
            }
        });

        painelSair.add(btnLogout);
        return painelSair;
    }

    private void atualizarTabelaAlunos() {
        tableModel.setRowCount(0);
        Turma turmaSelecionada = (Turma) comboTurmas.getSelectedItem();
        if (turmaSelecionada != null) {
            for (Aluno aluno : turmaSelecionada.getAlunosMatriculados()) {
                tableModel.addRow(new Object[] { aluno.getMatricula(), aluno.getNome(), true });
            }
        }
    }

    private void salvarFrequencias() {
        Turma turmaSelecionada = (Turma) comboTurmas.getSelectedItem();
        if (turmaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma turma selecionada!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int registrosSalvos = 0;
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String matricula = (String) tableModel.getValueAt(i, 0);
            boolean presente = (boolean) tableModel.getValueAt(i, 2);
            
            // CORREÇÃO: Usar o método correto de geração de ID
            int novoId = sistema.obterProximoIdFrequencia();
            
            Frequencia freq = new Frequencia(
                novoId, 
                matricula, 
                professor.getCpf(),
                turmaSelecionada.getNomeDisciplina(), 
                LocalDate.now(), 
                presente, 
                presente ? "" : "Falta registrada pelo professor"
            );
            
            try {
                sistema.adicionarFrequencia(freq);
                registrosSalvos++;
            } catch (persistencia.SistemaException ex) {
                System.err.println("Erro ao salvar frequência para matrícula " + matricula + ": " + ex.getMessage());
            }
        }
        
        if (registrosSalvos > 0) {
            JOptionPane.showMessageDialog(this, 
                "Frequências salvas com sucesso!\n" + 
                "Total de registros: " + registrosSalvos + " alunos\n" +
                "Data: " + LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
                "Sucesso", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma frequência foi salva.", "Atenção", JOptionPane.WARNING_MESSAGE);
        }
    }
}