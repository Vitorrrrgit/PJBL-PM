package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.FileWriter;
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
    private final boolean mostrarBotaoLogout;
    private final DefaultTableModel tableModelUsuarios;
    private final DefaultTableModel tableModelDisciplinas;
    private final DefaultTableModel tableModelTurmas;
    private final DefaultTableModel tableModelCursos;

    // Construtor com controle de logout (usado pela MainWindow)
    public PainelAdmin(Sistema sistema, Administrador admin, boolean mostrarBotaoLogout) {
        this.sistema = sistema;
        this.admin = admin;
        this.mostrarBotaoLogout = mostrarBotaoLogout;
        
        setLayout(new BorderLayout());

        this.tableModelUsuarios = new DefaultTableModel(new String[]{"ID", "Nome", "Email", "CPF", "Tipo", "Curso/Área"}, 0) { 
            @Override public boolean isCellEditable(int r, int c){ return false; } 
        };
        this.tableModelDisciplinas = new DefaultTableModel(new String[]{"Código", "Nome", "Professor", "Curso"}, 0) { 
            @Override public boolean isCellEditable(int r, int c){ return false; } 
        };
        this.tableModelTurmas = new DefaultTableModel(new String[]{"Disciplina", "Professor", "Alunos", "Curso"}, 0) { 
            @Override public boolean isCellEditable(int r, int c){ return false; } 
        };
        this.tableModelCursos = new DefaultTableModel(new String[]{"ID", "Nome do Curso"}, 0) { 
            @Override public boolean isCellEditable(int r, int c){ return false; } 
        };

        criarInterface();
        atualizarTodasTabelas();
    }

    // Construtor de compatibilidade 
    public PainelAdmin(Sistema sistema, Administrador admin) {
        this(sistema, admin, true);
    }

    private void criarInterface() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(" Gerenciar Usuários", criarAbaGerenciarUsuarios());
        tabbedPane.addTab(" Gerenciar Cursos", criarAbaGerenciarCursos());
        tabbedPane.addTab(" Gerenciar Disciplinas", criarAbaGerenciarDisciplinas());
        tabbedPane.addTab(" Gerenciar Turmas", criarAbaGerenciarTurmas());
        tabbedPane.addTab(" Relatórios", criarAbaRelatorios());
        tabbedPane.addTab(" Importar/Exportar", criarAbaImportarExportar());
        
        // Só adiciona aba de perfil no modo standalone
        if (mostrarBotaoLogout) {
            tabbedPane.addTab(" Meu Perfil", criarAbaPerfil());
        }

        add(tabbedPane, BorderLayout.CENTER);
        
        // Só adiciona painel de logout se solicitado (modo standalone)
        if (mostrarBotaoLogout) {
            add(criarPainelSair(), BorderLayout.SOUTH);
        }
    }

    private void atualizarTodasTabelas() {
        // Usuários
        tableModelUsuarios.setRowCount(0);
        sistema.listarUsuarios().forEach(u -> {
            String cursoOuArea = "N/A";
            if (u instanceof Aluno a) cursoOuArea = a.getCurso().getNome();
            else if (u instanceof Professor p) cursoOuArea = p.getArea();
            else if (u instanceof Coordenador c) cursoOuArea = c.getCurso().getNome();
            tableModelUsuarios.addRow(new Object[]{u.getId(), u.getNome(), u.getEmail(), u.getCpf(), u.getTipoUsuario(), cursoOuArea});
        });

        // Disciplinas
        tableModelDisciplinas.setRowCount(0);
        sistema.listarDisciplinas().forEach(d -> {
            String nomeProfessor = sistema.listarUsuarios().stream()
                .filter(u -> u instanceof Professor && u.getCpf().equals(d.getProfessorResponsavel()))
                .map(Usuario::getNome)
                .findFirst().orElse("Professor não encontrado");
            tableModelDisciplinas.addRow(new Object[]{d.getCodigoDisciplina(), d.getNomeDisciplina(), nomeProfessor, d.getCurso().getNome()});
        });
        
        // Turmas
        tableModelTurmas.setRowCount(0);
        sistema.listarTurmas().forEach(t -> tableModelTurmas.addRow(new Object[]{
            t.getNomeDisciplina(), 
            t.getProfessorResponsavel().getNome(), 
            t.getAlunosMatriculados().size(),
            t.getCurso() != null ? t.getCurso().getNome() : "N/A"
        }));
        
        // Cursos
        tableModelCursos.setRowCount(0);
        sistema.listarCursos().forEach(c -> tableModelCursos.addRow(new Object[]{c.getId(), c.getNome()}));
    }

    // ===== ABAS DE GERENCIAMENTO =====

    private JPanel criarAbaGerenciarUsuarios() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        JTable tabela = new JTable(tableModelUsuarios);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton(" Adicionar");
        JButton btnRemover = new JButton(" Remover");
        btnAdicionar.addActionListener(e -> adicionarUsuario());
        btnRemover.addActionListener(e -> removerUsuario(tabela));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaGerenciarCursos() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        JTable tabela = new JTable(tableModelCursos);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton(" Adicionar");
        JButton btnRemover = new JButton(" Remover");
        btnAdicionar.addActionListener(e -> adicionarCurso());
        btnRemover.addActionListener(e -> removerCurso(tabela));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaGerenciarDisciplinas() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        JTable tabela = new JTable(tableModelDisciplinas);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton(" Adicionar");
        JButton btnRemover = new JButton(" Remover");
        btnAdicionar.addActionListener(e -> adicionarDisciplina());
        btnRemover.addActionListener(e -> removerDisciplina(tabela));
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaGerenciarTurmas() {
        JPanel painel = new JPanel(new BorderLayout(5, 5));
        JTable tabela = new JTable(tableModelTurmas);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton(" Adicionar Turma");
        JButton btnRemover = new JButton(" Remover Turma");
        JButton btnAdicionarAluno = new JButton(" Adicionar Aluno à Turma");
        btnAdicionar.addActionListener(e -> adicionarTurma());
        btnRemover.addActionListener(e -> removerTurma(tabela));
        btnAdicionarAluno.addActionListener(e -> adicionarAlunoATurma(tabela));
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
        JComboBox<String> combo = new JComboBox<>(new String[]{"Selecione...", "Relatório Geral", "Alunos", "Professores", "Turmas"});
        painelFiltro.add(combo);
        
        combo.addActionListener(e -> {
            String sel = (String) combo.getSelectedItem();
            if (sel != null) switch (sel) {
                case "Relatório Geral" -> area.setText(sistema.gerarRelatorioGeral());
                case "Alunos" -> area.setText(sistema.gerarRelatorioDeAlunos());
                case "Professores" -> area.setText(sistema.gerarRelatorioDeProfessores());
                case "Turmas" -> area.setText(sistema.gerarRelatorioDeTurmas());
            }
        });
        
        JButton btnExportar = new JButton(" Exportar Relatório (TXT)");
        btnExportar.addActionListener(e -> exportarRelatorio(area.getText()));
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBotoes.add(btnExportar);
        
        painel.add(painelFiltro, BorderLayout.NORTH);
        painel.add(new JScrollPane(area), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    private JPanel criarAbaImportarExportar() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));
        
        JButton btnImportar = new JButton(" Importar Frequências (CSV)");
        btnImportar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnImportar.setPreferredSize(new java.awt.Dimension(250, 50));
        
        JButton btnExportar = new JButton(" Exportar Frequências (CSV)");
        btnExportar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExportar.setPreferredSize(new java.awt.Dimension(250, 50));
        
        btnImportar.addActionListener(e -> importarFrequencias());
        btnExportar.addActionListener(e -> exportarFrequencias());
        
        painel.add(btnImportar);
        painel.add(btnExportar);
        return painel;
    }

    private JPanel criarAbaPerfil() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        
        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new javax.swing.BoxLayout(painelInfo, javax.swing.BoxLayout.Y_AXIS));
        
        JLabel lblNome = new JLabel(" Administrador: " + admin.getNome());
        lblNome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        JLabel lblEmail = new JLabel(" Email: " + admin.getEmail());
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel lblNivel = new JLabel(" Nível de Acesso: TOTAL");
        lblNivel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        painelInfo.add(lblNome);
        painelInfo.add(new JLabel(" "));
        painelInfo.add(lblEmail);
        painelInfo.add(lblNivel);
        
        painel.add(painelInfo);
        
        JButton btnAlterarSenha = new JButton(" Alterar Senha");
        btnAlterarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnAlterarSenha.setPreferredSize(new java.awt.Dimension(150, 35));
        btnAlterarSenha.addActionListener(e -> {
            Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);
            new AlterarSenhaDialog(parent, sistema, admin).setVisible(true);
        });
        painel.add(btnAlterarSenha);
        
        return painel;
    }

    // ===== LÓGICA DE MANIPULAÇÃO =====
    
    private void adicionarCurso() {
        String nomeCurso = JOptionPane.showInputDialog(this, "Digite o nome do novo curso:", "Adicionar Curso", JOptionPane.PLAIN_MESSAGE);
        if (nomeCurso != null && !nomeCurso.trim().isEmpty()) {
            try {
                sistema.adicionarCurso(nomeCurso);
                atualizarTodasTabelas();
                JOptionPane.showMessageDialog(this, "Curso adicionado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (SistemaException e) { 
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
            }
        }
    }

    private void removerCurso(JTable tabela) {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um curso para remover.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (JOptionPane.showConfirmDialog(this, "Remover curso?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int id = (int) tabela.getValueAt(linha, 0);
            Curso curso = sistema.listarCursos().stream().filter(c -> c.getId() == id).findFirst().orElse(null);
            if (curso != null) {
                try { 
                    sistema.removerCurso(curso); 
                    atualizarTodasTabelas();
                    JOptionPane.showMessageDialog(this, "Curso removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SistemaException e) { 
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
                }
            }
        }
    }

    private void adicionarUsuario() {
        JTextField nomeField = new JTextField(15);
        JTextField emailField = new JTextField(15);
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
            } catch (SistemaException e) { 
                JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
            }
        }
    }
    
    private void removerUsuario(JTable tabela) {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para remover.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (JOptionPane.showConfirmDialog(this, "Remover usuário?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            int id = (int) tabela.getValueAt(linha, 0);
            sistema.listarUsuarios().stream().filter(u -> u.getId() == id).findFirst().ifPresent(usuario -> {
                try { 
                    sistema.removerUsuario(usuario); 
                    atualizarTodasTabelas();
                    JOptionPane.showMessageDialog(this, "Usuário removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SistemaException e) { 
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
                }
            });
        }
    }

    private void adicionarDisciplina() {
        JTextField codigoField = new JTextField(10);
        JTextField nomeField = new JTextField(15);
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
                try { 
                    sistema.adicionarDisciplina(new Disciplina(nomeField.getText(), codigoField.getText(), prof.getCpf(), curso)); 
                    atualizarTodasTabelas();
                    JOptionPane.showMessageDialog(this, "Disciplina adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SistemaException e) { 
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
                }
            }
        }
    }
    
    private void removerDisciplina(JTable tabela) {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma disciplina para remover.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (JOptionPane.showConfirmDialog(this, "Remover disciplina?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String codigo = (String) tabela.getValueAt(linha, 0);
            sistema.listarDisciplinas().stream().filter(d -> d.getCodigoDisciplina().equals(codigo)).findFirst().ifPresent(disciplina -> {
                try { 
                    sistema.removerDisciplina(disciplina); 
                    atualizarTodasTabelas();
                    JOptionPane.showMessageDialog(this, "Disciplina removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SistemaException e) { 
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
                }
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
                try { 
                    sistema.adicionarTurma(new Turma(disc.getNomeDisciplina(), 2025, 1, prof, disc.getCurso())); 
                    atualizarTodasTabelas();
                    JOptionPane.showMessageDialog(this, "Turma adicionada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SistemaException e) { 
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
                }
            }
        }
    }
    
    private void removerTurma(JTable tabela) {
        int linha = tabela.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma turma para remover.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (JOptionPane.showConfirmDialog(this, "Remover turma?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            String nomeDisciplina = (String) tabela.getValueAt(linha, 0);
            Turma turma = sistema.listarTurmas().stream().filter(t -> t.getNomeDisciplina().equals(nomeDisciplina)).findFirst().orElse(null);
            if (turma != null) {
                try { 
                    sistema.removerTurma(turma); 
                    atualizarTodasTabelas();
                    JOptionPane.showMessageDialog(this, "Turma removida com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (SistemaException e) { 
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
                }
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
                JOptionPane.showMessageDialog(this, "Aluno adicionado à turma com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void exportarRelatorio(String conteudo) {
        if (conteudo.startsWith("Selecione")) { 
            JOptionPane.showMessageDialog(this, "Selecione um relatório para exportar.", "Aviso", JOptionPane.WARNING_MESSAGE); 
            return; 
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("TXT Files", "txt"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (PrintWriter out = new PrintWriter(new FileWriter(chooser.getSelectedFile()))) {
                out.println(conteudo);
                JOptionPane.showMessageDialog(this, "Relatório exportado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) { 
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
            }
        }
    }

    private void importarFrequencias() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            sistema.importarFrequenciasCSV(chooser.getSelectedFile().getAbsolutePath());
            JOptionPane.showMessageDialog(this, "Importação concluída! Verifique o console para detalhes.", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exportarFrequencias() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV Files", "csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try { 
                sistema.exportarFrequenciasCSV(chooser.getSelectedFile().getAbsolutePath()); 
                JOptionPane.showMessageDialog(this, "Frequências exportadas com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) { 
                JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
            }
        }
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
            Window janela = SwingUtilities.getWindowAncestor(this);
            if (janela != null) janela.dispose();
            new LoginWindow(sistema).setVisible(true);
        });
        painelSair.add(btnLogout);
        return painelSair;
    }
}