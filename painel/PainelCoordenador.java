package visual;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.*;

public class PainelCoordenador extends JPanel {

    private final Sistema sistema;
    private final Coordenador coordenador;
    
    private final JComboBox<Turma> comboTurmas;
    private final JTable tabelaFrequencias;
    private final DefaultTableModel tableModelFrequencias;

    public PainelCoordenador(Sistema sistema, Coordenador coordenador) {
        this.sistema = sistema;
        this.coordenador = coordenador;
        setLayout(new BorderLayout(10, 10));

        // --- Componentes da Aba de Relatórios ---
        this.comboTurmas = new JComboBox<>(sistema.listarTurmas().toArray(Turma[]::new));
        
        // Adicionada a coluna "Matrícula"
        String[] colunasFreq = {"Data", "Nome do Aluno", "Matrícula", "Status", "Observações"};
        this.tableModelFrequencias = new DefaultTableModel(colunasFreq, 0);
        this.tabelaFrequencias = new JTable(tableModelFrequencias);

        // --- Montagem do Painel com Abas ---
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Relatórios do Curso", criarAbaRelatorios());
        tabbedPane.addTab("Alunos", criarAbaAlunos());
        tabbedPane.addTab("Meu Perfil", criarAbaPerfil());
        add(tabbedPane, BorderLayout.CENTER);
        add(criarPainelSair(), BorderLayout.SOUTH);
        
        atualizarTabelaFrequencias();
    }

    private JPanel criarAbaRelatorios() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltro.add(new JLabel("Selecione a Turma para ver o relatório:"));
        painelFiltro.add(comboTurmas);
        comboTurmas.addActionListener(_ -> atualizarTabelaFrequencias());
        painel.add(painelFiltro, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabelaFrequencias), BorderLayout.CENTER);
        return painel;
    }

private void atualizarTabelaFrequencias() {
    tableModelFrequencias.setRowCount(0); // Limpa a tabela
    Turma turmaSelecionada = (Turma) comboTurmas.getSelectedItem();

    if (turmaSelecionada != null) {
        List<Frequencia> frequencias = sistema.buscarFrequenciasPorDisciplina(turmaSelecionada.getNomeDisciplina());
        
        for (Frequencia f : frequencias) {
            String nomeAluno = sistema.listarUsuarios().stream()
                .filter(u -> u instanceof Aluno && ((Aluno) u).getMatricula().equals(f.getAlunoMatricula()))
                .map(Usuario::getNome)
                .findFirst() // Tenta encontrar o primeiro que corresponde
                .orElse("Matrícula não encontrada"); // Se não encontrar, usa este valor padrão

            // Adiciona a linha na tabela com o nome do aluno
            tableModelFrequencias.addRow(new Object[]{
                f.getDataFormatada(),
                nomeAluno,
                f.getAlunoMatricula(),
                f.getStatus(),
                f.getObservacoes()
            });
        }
    }
}

    private JPanel criarAbaAlunos() {
        JPanel painel = new JPanel(new BorderLayout());
        String[] colunas = {"ID", "Nome", "Matrícula", "Curso"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(model);
        
        sistema.listarUsuarios().stream()
                .filter(u -> u instanceof Aluno)
                .map(u -> (Aluno) u)
                .forEach(a -> model.addRow(new Object[]{a.getId(), a.getNome(), a.getMatricula(), a.getCurso()}));
        
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return painel; 
    }

    private JPanel criarAbaPerfil() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAlterarSenha = new JButton("Alterar Senha");
        btnAlterarSenha.addActionListener(_ -> new AlterarSenhaDialog((Frame) SwingUtilities.getWindowAncestor(this), sistema, coordenador).setVisible(true));
        
        painel.add(new JLabel("Coordenador: " + coordenador.getNome()));
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
            JOptionPane.YES_NO_OPTION
        );
        
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
}