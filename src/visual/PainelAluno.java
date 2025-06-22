package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
import models.Sistema;

public class PainelAluno extends JPanel {

    private final Sistema sistema;
    private final Aluno aluno;

    public PainelAluno(Sistema sistema, Aluno aluno) {
        this.sistema = sistema;
        this.aluno = aluno;
        setLayout(new BorderLayout(10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Minhas Frequências", criarAbaFrequencias());
        tabbedPane.addTab("Meu Perfil", criarAbaPerfil());
        
        add(tabbedPane, BorderLayout.CENTER);
        add(criarPainelSair(), BorderLayout.SOUTH);
    }

    private JScrollPane criarAbaFrequencias() {
        String[] colunas = { "Disciplina", "Data", "Status", "Observações" };
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);
        JTable tabela = new JTable(tableModel);

        List<Frequencia> frequencias = sistema.buscarFrequenciasPorAluno(aluno.getMatricula());
        for (Frequencia f : frequencias) {
            tableModel.addRow(new Object[] {
                    f.getDisciplina(), f.getDataFormatada(), f.getStatus(), f.getObservacoes()
            });
        }
        return new JScrollPane(tabela);
    }

    private JPanel criarAbaPerfil() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder("Meu Resumo Acadêmico"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // --- CÁLCULO E FORMATAÇÃO DA PORCENTAGEM DE PRESENÇA ---
        double percentualPresenca = sistema.calcularPercentualDePresenca(aluno); // Esta linha agora funciona
        String textoPresenca = String.format("%.1f%%", percentualPresenca);

        // --- EXIBIÇÃO DAS INFORMAÇÕES ---
        gbc.gridy = 0; gbc.gridx = 0; painel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; painel.add(new JLabel(aluno.getNome()), gbc);

        gbc.gridy = 1; gbc.gridx = 0; painel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; painel.add(new JLabel(aluno.getEmail()), gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        JLabel labelPresenca = new JLabel("Percentual de Presença:");
        labelPresenca.setFont(new Font("Segoe UI", Font.BOLD, 12));
        painel.add(labelPresenca, gbc);
        
        gbc.gridx = 1;
        JLabel valorPresenca = new JLabel(textoPresenca);
        valorPresenca.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        if (percentualPresenca < 75.0) {
            valorPresenca.setForeground(Color.RED);
        } else {
            valorPresenca.setForeground(new Color(0, 128, 0));
        }
        painel.add(valorPresenca, gbc);

        // --- BOTÕES DE AÇÃO ---
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(20, 5, 5, 5);
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAlterarSenha = new JButton("Alterar Senha");
        btnAlterarSenha.addActionListener(_ -> new AlterarSenhaDialog((Frame) SwingUtilities.getWindowAncestor(this), sistema, aluno).setVisible(true));
        
        painelBotoes.add(btnAlterarSenha);
        painel.add(painelBotoes, gbc);
        return painel;
    }

    private JPanel criarPainelSair() {
        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(_ -> {
            int confirmacao = JOptionPane.showConfirmDialog(
                this, "Deseja realmente sair?", "Confirmar Saída", JOptionPane.YES_NO_OPTION);
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