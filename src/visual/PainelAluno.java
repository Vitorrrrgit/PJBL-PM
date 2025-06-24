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
    private final boolean mostrarBotaoLogout;

    // Construtor com controle de logout (usado pela MainWindow)
    public PainelAluno(Sistema sistema, Aluno aluno, boolean mostrarBotaoLogout) {
        this.sistema = sistema;
        this.aluno = aluno;
        this.mostrarBotaoLogout = mostrarBotaoLogout;
        
        setLayout(new BorderLayout(10, 10));
        criarInterface();
    }

    // Construtor de compatibilidade 
    public PainelAluno(Sistema sistema, Aluno aluno) {
        this(sistema, aluno, true);
    }

    private void criarInterface() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(" Minhas Frequências", criarAbaFrequencias());
        tabbedPane.addTab(" Meu Perfil", criarAbaPerfil());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Só adiciona painel de logout se solicitado 
        if (mostrarBotaoLogout) {
            add(criarPainelSair(), BorderLayout.SOUTH);
        }
    }

    private JScrollPane criarAbaFrequencias() {
        String[] colunas = { "Disciplina", "Data", "Status", "Observações" };
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        
        JTable tabela = new JTable(tableModel);
        tabela.setRowHeight(25);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        List<Frequencia> frequencias = sistema.buscarFrequenciasPorAluno(aluno.getMatricula());
        
        if (frequencias.isEmpty()) {
            tableModel.addRow(new Object[] { "Nenhuma frequência encontrada", "", "", "" });
        } else {
            for (Frequencia f : frequencias) {
                tableModel.addRow(new Object[] {
                    f.getDisciplina(), 
                    f.getDataFormatada(), 
                    f.getStatus(), 
                    f.getObservacoes()
                });
            }
        }
        
        return new JScrollPane(tabela);
    }

    private JPanel criarAbaPerfil() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            " Resumo Acadêmico",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(52, 152, 219)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Cálculo da presença
        double percentualPresenca = sistema.calcularPercentualDePresenca(aluno);
        String textoPresenca = String.format("%.1f%%", percentualPresenca);

        // Informações pessoais
        gbc.gridy = 0; gbc.gridx = 0; 
        painel.add(criarLabel("Nome:", true), gbc);
        gbc.gridx = 1; 
        painel.add(criarLabel(aluno.getNome(), false), gbc);

        gbc.gridy = 1; gbc.gridx = 0; 
        painel.add(criarLabel("Email:", true), gbc);
        gbc.gridx = 1; 
        painel.add(criarLabel(aluno.getEmail(), false), gbc);

        gbc.gridy = 2; gbc.gridx = 0; 
        painel.add(criarLabel("Matrícula:", true), gbc);
        gbc.gridx = 1; 
        painel.add(criarLabel(aluno.getMatricula(), false), gbc);

        gbc.gridy = 3; gbc.gridx = 0; 
        painel.add(criarLabel("Curso:", true), gbc);
        gbc.gridx = 1; 
        painel.add(criarLabel(aluno.getCurso().getNome(), false), gbc);

        gbc.gridy = 4; gbc.gridx = 0; 
        painel.add(criarLabel("Semestre:", true), gbc);
        gbc.gridx = 1; 
        painel.add(criarLabel(String.valueOf(aluno.getSemestre()) + "º", false), gbc);

        // Linha separadora
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 8, 10, 8);
        JPanel separator = new JPanel();
        separator.setBackground(new Color(200, 200, 200));
        separator.setPreferredSize(new java.awt.Dimension(0, 1));
        painel.add(separator, gbc);

        // Reset das configurações
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Estatísticas de presença
        gbc.gridy = 6; gbc.gridx = 0;
        JLabel labelPresenca = criarLabel(" Percentual de Presença:", true);
        labelPresenca.setFont(new Font("Segoe UI", Font.BOLD, 14));
        painel.add(labelPresenca, gbc);
        
        gbc.gridx = 1;
        JLabel valorPresenca = criarLabel(textoPresenca, false);
        valorPresenca.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        if (percentualPresenca < 75.0) {
            valorPresenca.setForeground(new Color(231, 76, 60)); // Vermelho
            valorPresenca.setText(textoPresenca + "  Abaixo do mínimo!");
        } else {
            valorPresenca.setForeground(new Color(46, 204, 113)); // Verde
            valorPresenca.setText(textoPresenca + " OK!");
        }
        painel.add(valorPresenca, gbc);

        // Estatísticas adicionais
        List<Frequencia> frequencias = sistema.buscarFrequenciasPorAluno(aluno.getMatricula());
        long totalPresencas = frequencias.stream().mapToLong(f -> f.isPresente() ? 1 : 0).sum();
        long totalFaltas = frequencias.size() - totalPresencas;

        gbc.gridy = 7; gbc.gridx = 0;
        painel.add(criarLabel("Total de Aulas:", true), gbc);
        gbc.gridx = 1;
        painel.add(criarLabel(String.valueOf(frequencias.size()), false), gbc);

        gbc.gridy = 8; gbc.gridx = 0;
        painel.add(criarLabel("Presenças:", true), gbc);
        gbc.gridx = 1;
        JLabel lblPresencas = criarLabel(String.valueOf(totalPresencas), false);
        lblPresencas.setForeground(new Color(46, 204, 113));
        painel.add(lblPresencas, gbc);

        gbc.gridy = 9; gbc.gridx = 0;
        painel.add(criarLabel("Faltas:", true), gbc);
        gbc.gridx = 1;
        JLabel lblFaltas = criarLabel(String.valueOf(totalFaltas), false);
        lblFaltas.setForeground(new Color(231, 76, 60));
        painel.add(lblFaltas, gbc);

        // Botão alterar senha 
        if (mostrarBotaoLogout) {
            gbc.gridy = 10; gbc.gridx = 0; gbc.gridwidth = 2; 
            gbc.insets = new Insets(20, 8, 8, 8);
            gbc.anchor = GridBagConstraints.CENTER;
            
            JButton btnAlterarSenha = new JButton(" Alterar Minha Senha");
            btnAlterarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btnAlterarSenha.setPreferredSize(new java.awt.Dimension(180, 35));
            btnAlterarSenha.addActionListener(e -> {
                Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
                new AlterarSenhaDialog(parent, sistema, aluno).setVisible(true);
            });
            
            painel.add(btnAlterarSenha, gbc);
        }
        
        return painel;
    }

    private JLabel criarLabel(String texto, boolean isBold) {
        JLabel label = new JLabel(texto);
        if (isBold) {
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setForeground(new Color(52, 73, 94));
        } else {
            label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            label.setForeground(new Color(85, 85, 85));
        }
        return label;
    }

    private JPanel criarPainelSair() {
        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelSair.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JButton btnLogout = new JButton(" Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLogout.setPreferredSize(new java.awt.Dimension(100, 35));
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.addActionListener(e -> {
            int confirmacao = JOptionPane.showConfirmDialog(
                this, 
                "Deseja realmente sair?", 
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