package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import models.Administrador;
import models.Aluno;
import models.Coordenador;
import models.Curso;
import models.Disciplina;
import models.Professor;
import models.Sistema;
import models.Turma;
import models.Usuario;
import persistencia.SistemaException;

public class PainelAdmin extends JPanel {

    private final Sistema sistema;
    private final Administrador admin;
    private final DefaultTableModel tableModelUsuarios;
    private final DefaultTableModel tableModelDisciplinas;
    private final DefaultTableModel tableModelTurmas;
    private final DefaultTableModel tableModelCursos;

    public PainelAdmin(Sistema sistema, Administrador admin) {
        this.sistema = sistema;
        this.admin = admin;
        setLayout(new BorderLayout());

        this.tableModelUsuarios = new DefaultTableModel(new String[]{"ID", "Nome", "Email", "CPF", "Tipo", "Curso/Área"}, 0) { @Override public boolean isCellEditable(int r, int c){ return false; } };
        this.tableModelDisciplinas = new DefaultTableModel(new String[]{"Código", "Nome", "Professor", "Curso"}, 0) { @Override public boolean isCellEditable(int r, int c){ return false; } };
        this.tableModelTurmas = new DefaultTableModel(new String[]{"Disciplina", "Professor", "Alunos"}, 0) { @Override public boolean isCellEditable(int r, int c){ return false; } };
        this.tableModelCursos = new DefaultTableModel(new String[]{"ID", "Nome do Curso"}, 0) { @Override public boolean isCellEditable(int r, int c){ return false; } };

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Gerenciar Usuários", criarAbaGerenciarUsuarios());
        tabbedPane.addTab("Gerenciar Cursos", criarAbaGerenciarCursos());
        tabbedPane.addTab("Gerenciar Disciplinas", criarAbaGerenciarDisciplinas());
        tabbedPane.addTab("Gerenciar Turmas", criarAbaGerenciarTurmas());
        tabbedPane.addTab("Relatórios", criarAbaRelatorios());
        tabbedPane.addTab("Importar/Exportar", criarAbaImportarExportar());
        tabbedPane.addTab("Meu Perfil", criarAbaPerfil());

        add(tabbedPane, BorderLayout.CENTER);
        add(criarPainelSair(), BorderLayout.SOUTH);
        atualizarTodasTabelas();
    }

    private void atualizarTodasTabelas() {
        tableModelUsuarios.setRowCount(0);
        sistema.listarUsuarios().forEach(u -> {
            String cursoOuArea = "N/A";
            if (u instanceof Aluno a) cursoOuArea = a.getCurso().getNome();
            else if (u instanceof Professor p) cursoOuArea = p.getArea();
            else if (u instanceof Coordenador c) cursoOuArea = c.getCurso().getNome();
            tableModelUsuarios.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail(), u.getCpf(), u.getTipoUsuario(), cursoOuArea});
        });

        tableModelDisciplinas.setRowCount(0);
        sistema.listarDisciplinas().forEach(d -> tableModelDisciplinas.addRow(new Object[]{d.getCodigoDisciplina(), d.getNomeDisciplina(), d.getProfessorResponsavel(), d.getCurso().getNome()}));
        
        tableModelTurmas.setRowCount(0);
        sistema.listarTurmas().forEach(t -> tableModelTurmas.addRow(new Object[]{t.getNomeDisciplina(), t.getProfessorResponsavel().getNome(), t.getAlunosMatriculados().size()}));
        
        tableModelCursos.setRowCount(0);
        sistema.listarCursos().forEach(c -> tableModelCursos.addRow(new Object[]{c.getId(), c.getNome()}));
    }

    // --- ABAS DE GERENCIAMENTO ---

    private JPanel criarAbaGerenciarUsuarios() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        JTable tabela = new JTable(tableModelUsuarios);
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnRemover = new JButton("Remover");
        btnAdicionar.addActionListener(_ -> adicionarUsuario());
        btnRemover.addActionListener(_ -> removerUsuario(tabela));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaGerenciarCursos() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        JTable tabela = new JTable(tableModelCursos);
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnRemover = new JButton("Remover");
        btnAdicionar.addActionListener(_ -> adicionarCurso());
        btnRemover.addActionListener(_ -> removerCurso(tabela));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaGerenciarDisciplinas() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        JTable tabela = new JTable(tableModelDisciplinas);
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnRemover = new JButton("Remover");
        btnAdicionar.addActionListener(_ -> adicionarDisciplina());
        btnRemover.addActionListener(_ -> removerDisciplina(tabela));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaGerenciarTurmas() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        JTable tabela = new JTable(tableModelTurmas);
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton("Adicionar Turma");
        JButton btnRemover = new JButton("Remover Turma");
        JButton btnAdicionarAluno = new JButton("Adicionar Aluno à Turma");
        btnAdicionar.addActionListener(_ -> adicionarTurma());
        btnRemover.addActionListener(_ -> removerTurma(tabela));
        btnAdicionarAluno.addActionListener(_ -> adicionarAlunoATurma(tabela));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnAdicionarAluno);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaRelatorios() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JTextArea area = new JTextArea("Selecione um relatório para visualizar.");
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelFiltro.add(new JLabel("Tipo de Relatório:"));
        JComboBox<String> combo = new JComboBox<>(new String[]{"Selecione...", "Alunos", "Professores", "Turmas"});
        painelFiltro.add(combo);
        combo.addActionListener(e -> {
            String sel = (String) combo.getSelectedItem();
            if (sel != null) switch (sel) {
                case "Alunos" -> area.setText(sistema.gerarRelatorioDeAlunos());
                case "Professores" -> area.setText(sistema.gerarRelatorioDeProfessores());
                case "Turmas" -> area.setText(sistema.gerarRelatorioDeTurmas());
            }
        });
        JButton btnExportar = new JButton("Exportar Relatório (TXT)");
        btnExportar.addActionListener(_ -> exportarRelatorio(area.getText()));
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(btnExportar);
        painel.add(painelFiltro, BorderLayout.NORTH);
        painel.add(new JScrollPane(area), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaImportarExportar() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        JButton btnImportar = new JButton("Importar Frequências (CSV)");
        JButton btnExportar = new JButton("Exportar Frequências (CSV)");
        btnImportar.addActionListener(_ -> importarFrequencias());
        btnExportar.addActionListener(_ -> exportarFrequencias());
        painel.add(btnImportar);
        painel.add(btnExportar);
        return painel;
    }

    private JPanel criarAbaPerfil() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAlterarSenha = new JButton("Alterar Senha");
        btnAlterarSenha.addActionListener(_ -> new AlterarSenhaDialog((Frame) SwingUtilities.getWindowAncestor(this), sistema, admin).setVisible(true));
        painel.add(new JLabel("Logado como: " + admin.getNome()));
        painel.add(btnAlterarSenha);
        return painel;
    }

    // --- LÓGICA DE MANIPULAÇÃO ---
    
    private void adicionarCurso() {
        String nomeCurso = JOptionPane.showInputDialog(this, "Digite o nome do novo curso:", "Adicionar Curso", JOptionPane.PLAIN_MESSAGE);
        if (nomeCurso != null && !nomeCurso.trim().isEmpty()) {
            try {
                sistema.adicionarCurso(nomeCurso);
                atualizarTodasTabelas();
            } catch (SistemaException e) { JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void removerCurso(JTable tabela) {
        int linha = tabela.getSelectedRow();
        if (linha != -1 && JOptionPane.showConfirmDialog(this, "Remover curso?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int id = (int) tabela.getValueAt(linha, 0);
            Curso curso = sistema.listarCursos().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
            if (curso != null) {
                try { sistema.removerCurso(curso); atualizarTodasTabelas(); } catch (SistemaException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
            }
        }
    }

    private void adicionarUsuario() {
        JTextField nomeField = new JTextField();
        JTextField emailField = new JTextField();
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Aluno", "Professor", "Coordenador", "Administrador"});
        JLabel cursoLabel = new JLabel("Curso/Área:");
        JComboBox<Curso> cursoCombo = new JComboBox<>(sistema.listarCursos().toArray(new Curso[0]));
        JPanel painelDialogo = new JPanel(new GridLayout(0, 2, 5, 5));
        painelDialogo.add(new JLabel("Nome:")); painelDialogo.add(nomeField);
        painelDialogo.add(new JLabel("Email:")); painelDialogo.add(emailField);
        painelDialogo.add(new JLabel("Tipo:")); painelDialogo.add(tipoCombo);
        painelDialogo.add(cursoLabel);
        painelDialogo.add(cursoCombo);

        tipoCombo.addActionListener(e -> {
            boolean isAdm = "Administrador".equals(tipoCombo.getSelectedItem());
            cursoLabel.setVisible(!isAdm);
            cursoCombo.setVisible(!isAdm);
        });
        
        if (JOptionPane.showConfirmDialog(this, painelDialogo, "Adicionar Usuário", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                String tipo = (String) tipoCombo.getSelectedItem();
                Curso curso = (Curso) cursoCombo.getSelectedItem();
                if (!"Administrador".equals(tipo) && curso == null) {
                    JOptionPane.showMessageDialog(this, "Selecione um curso.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Usuario novoUsuario = switch (tipo) {
                    case "Aluno" -> new Aluno(sistema.obterProximoIdUsuario(), nomeField.getText(), emailField.getText(), sistema.gerarCPF(), "MAT" + sistema.obterProximoIdUsuario(), curso, 1);
                    case "Professor" -> new Professor(sistema.obterProximoIdUsuario(), nomeField.getText(), emailField.getText(), sistema.gerarCPF(), curso.getNome(), "Indefinida");
                    case "Coordenador" -> new Coordenador(sistema.obterProximoIdUsuario(), nomeField.getText(), emailField.getText(), sistema.gerarCPF(), curso);
                    default -> new Administrador(sistema.obterProximoIdUsuario(), nomeField.getText(), emailField.getText(), sistema.gerarCPF(), "TOTAL");
                };
                sistema.adicionarUsuario(novoUsuario);
                atualizarTodasTabelas();
                JOptionPane.showMessageDialog(this, "Usuário adicionado! Senha gerada: " + novoUsuario.getSenha(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SistemaException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
        }
    }
    
    private void removerUsuario(JTable tabela) {
        int linha = tabela.getSelectedRow();
        if (linha != -1 && JOptionPane.showConfirmDialog(this, "Remover usuário?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int id = (int) tabela.getValueAt(linha, 0);
            sistema.listarUsuarios().stream().filter(u -> u.getId() == id).findFirst().ifPresent(usuario -> {
                try { sistema.removerUsuario(usuario); atualizarTodasTabelas(); } catch (SistemaException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
            });
        }
    }

    private void adicionarDisciplina() {
        JTextField codigoField = new JTextField();
        JTextField nomeField = new JTextField();
        JComboBox<Professor> comboProfessores = new JComboBox<>(sistema.listarUsuarios().stream().filter(u -> u instanceof Professor).map(u -> (Professor) u).toArray(Professor[]::new));
        JComboBox<Curso> comboCursos = new JComboBox<>(sistema.listarCursos().toArray(new Curso[0]));
        JPanel painelDialogo = new JPanel(new GridLayout(0, 2, 5, 5));
        painelDialogo.add(new JLabel("Código:")); painelDialogo.add(codigoField);
        painelDialogo.add(new JLabel("Nome:")); painelDialogo.add(nomeField);
        painelDialogo.add(new JLabel("Professor:")); painelDialogo.add(comboProfessores);
        painelDialogo.add(new JLabel("Curso:")); painelDialogo.add(comboCursos);
        if (JOptionPane.showConfirmDialog(this, painelDialogo, "Adicionar Disciplina", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Professor prof = (Professor) comboProfessores.getSelectedItem();
            Curso curso = (Curso) comboCursos.getSelectedItem();
            if (prof != null && curso != null) {
                try { sistema.adicionarDisciplina(new Disciplina(nomeField.getText(), codigoField.getText(), prof.getCpf(), curso)); atualizarTodasTabelas(); } catch (SistemaException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
            }
        }
    }
    
    private void removerDisciplina(JTable tabela) {
        int linha = tabela.getSelectedRow();
        if (linha != -1 && JOptionPane.showConfirmDialog(this, "Remover disciplina?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String codigo = (String) tabela.getValueAt(linha, 0);
            sistema.listarDisciplinas().stream().filter(d -> d.getCodigoDisciplina().equals(codigo)).findFirst().ifPresent(disciplina -> {
                try { sistema.removerDisciplina(disciplina); atualizarTodasTabelas(); } catch (SistemaException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
            });
        }
    }
    
    private void adicionarTurma() {
        JComboBox<Disciplina> comboDisciplinas = new JComboBox<>(sistema.listarDisciplinas().toArray(Disciplina[]::new));
        JComboBox<Professor> comboProfessores = new JComboBox<>(sistema.listarUsuarios().stream().filter(u -> u instanceof Professor).map(u -> (Professor) u).toArray(Professor[]::new));
        JPanel painelDialogo = new JPanel(new GridLayout(0, 2, 5, 5));
        painelDialogo.add(new JLabel("Disciplina:")); painelDialogo.add(comboDisciplinas);
        painelDialogo.add(new JLabel("Professor:")); painelDialogo.add(comboProfessores);
        if (JOptionPane.showConfirmDialog(this, painelDialogo, "Adicionar Turma", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Disciplina disc = (Disciplina) comboDisciplinas.getSelectedItem();
            Professor prof = (Professor) comboProfessores.getSelectedItem();
            if (disc != null && prof != null) {
                try { sistema.adicionarTurma(new Turma(disc.getNomeDisciplina(), 2025, 1, prof, disc.getCurso())); atualizarTodasTabelas(); } catch (SistemaException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
            }
        }
    }
    
    private void removerTurma(JTable tabela) {
        int linha = tabela.getSelectedRow();
        if (linha != -1 && JOptionPane.showConfirmDialog(this, "Remover turma?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String nomeDisciplina = (String) tabela.getValueAt(linha, 0);
            Turma turma = sistema.listarTurmas().stream().filter(t -> t.getNomeDisciplina().equals(nomeDisciplina)).findFirst().orElse(null);
            if (turma != null) {
                try { sistema.removerTurma(turma); atualizarTodasTabelas(); } catch (SistemaException e) { JOptionPane.showMessageDialog(this, e.getMessage()); }
            }
        }
    }

    private void adicionarAlunoATurma(JTable tabelaTurmas) {
        int linhaSelecionada = tabelaTurmas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma turma.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String nomeDisciplina = (String) tableModelTurmas.getValueAt(linhaSelecionada, 0);
        Turma turma = sistema.listarTurmas().stream().filter(t -> t.getNomeDisciplina().equals(nomeDisciplina)).findFirst().orElse(null);
        if (turma == null) return;
        List<Aluno> alunosDisponiveis = sistema.listarUsuarios().stream()
            .filter(u -> u instanceof Aluno).map(u -> (Aluno) u)
            .filter(a -> !turma.getAlunosMatriculados().contains(a))
            .collect(Collectors.toList());
        if (alunosDisponiveis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há alunos disponíveis.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JComboBox<Aluno> comboAlunos = new JComboBox<>(alunosDisponiveis.toArray(new Aluno[0]));
        if (JOptionPane.showConfirmDialog(this, comboAlunos, "Selecione o Aluno", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Aluno aluno = (Aluno) comboAlunos.getSelectedItem();
            if (aluno != null) {
                turma.adicionarAluno(aluno);
                sistema.salvarDados();
                atualizarTodasTabelas();
            }
        }
    }
    
    private void exportarRelatorio(String conteudo) {
        if (conteudo.startsWith("Selecione")) { JOptionPane.showMessageDialog(this, "Selecione um relatório para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE); return; }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("TXT Files", "txt"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter out = new PrintWriter(new FileWriter(chooser.getSelectedFile()))) {
                out.println(conteudo);
                JOptionPane.showMessageDialog(this, "Relatório exportado!");
            } catch (IOException e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
        }
    }

    private void importarFrequencias() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            sistema.importarFrequenciasCSV(chooser.getSelectedFile().getAbsolutePath());
            atualizarTodasTabelas();
        }
    }

    private void exportarFrequencias() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try { sistema.exportarFrequenciasCSV(chooser.getSelectedFile().getAbsolutePath()); } catch (IOException e) { JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage()); }
        }
    }
    
    private JPanel criarPainelSair() {
        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(_ -> {
            Window janela = SwingUtilities.getWindowAncestor(this);
            if (janela != null) janela.dispose();
            new LoginWindow(sistema).setVisible(true);
        });
        painelSair.add(btnLogout);
        return painelSair;
    }
}