package visual;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.*;
import persistencia.SistemaException;

public class PainelAdmin extends JPanel {

    private final Sistema sistema;
    private final Administrador admin;

    private final JTable tabelaUsuarios;
    private final DefaultTableModel tableModelUsuarios;
    private final JTable tabelaDisciplinas;
    private final DefaultTableModel tableModelDisciplinas;

    public PainelAdmin(Sistema sistema, Administrador admin) {
        this.sistema = sistema;
        this.admin = admin;
        setLayout(new BorderLayout());

        // Modelos e Tabelas
        String[] colunasUsuarios = { "ID", "Nome", "Email", "CPF", "Tipo" };
        this.tableModelUsuarios = new DefaultTableModel(colunasUsuarios, 0);
        this.tabelaUsuarios = new JTable(tableModelUsuarios);

        String[] colunasDisciplinas = { "Código", "Nome da Disciplina", "Professor CPF" };
        this.tableModelDisciplinas = new DefaultTableModel(colunasDisciplinas, 0);
        this.tabelaDisciplinas = new JTable(tableModelDisciplinas);

        // Abas
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Gerenciar Usuários", criarAbaGerenciarUsuarios());
        tabbedPane.addTab("Gerenciar Disciplinas", criarAbaGerenciarDisciplinas());
        tabbedPane.addTab("Relatórios Detalhados", criarAbaRelatorios());
        tabbedPane.addTab("Meu Perfil", criarAbaPerfil());

        add(tabbedPane, BorderLayout.CENTER);
        add(criarPainelSair(), BorderLayout.SOUTH);

        atualizarTabelas();
    }

    // Aba de Gerenciar Usuários    
    private JPanel criarAbaGerenciarUsuarios() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton("Adicionar Usuário");
        JButton btnRemover = new JButton("Remover Usuário");

        btnAdicionar.addActionListener(_ -> mostrarDialogoAdicionarUsuario());
        btnRemover.addActionListener(_ -> removerUsuarioSelecionado());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painel.add(new JScrollPane(tabelaUsuarios), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    // Aba de Gerenciar Disciplinas
    private JPanel criarAbaGerenciarDisciplinas() {
        JPanel painel = new JPanel(new BorderLayout(10, 10));
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdicionar = new JButton("Adicionar Disciplina");
        JButton btnRemover = new JButton("Remover Disciplina");

        btnAdicionar.addActionListener(_ -> adicionarNovaDisciplina());
        btnRemover.addActionListener(_ -> removerDisciplinaSelecionada());

        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnRemover);
        painel.add(new JScrollPane(tabelaDisciplinas), BorderLayout.CENTER);
        painel.add(painelBotoes, BorderLayout.SOUTH);
        return painel;
    }

    // Aba de Relatórios
    private JPanel criarAbaRelatorios() {
    JPanel painel = new JPanel(new BorderLayout(10, 10));
    painel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JPanel painelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JComboBox<String> comboRelatorios = new JComboBox<>(new String[] {
        "Selecione um relatório...", "Relatório de Alunos", "Relatório de Professores", "Relatório de Turmas"
    });
    painelFiltro.add(new JLabel("Tipo de Relatório:"));
    painelFiltro.add(comboRelatorios);
    
    JTextArea areaResultado = new JTextArea("Selecione um tipo de relatório para visualizar os dados.");
    areaResultado.setEditable(false);
    areaResultado.setFont(new Font("Consolas", Font.PLAIN, 14));

    comboRelatorios.addActionListener(_ -> {
        String selecionado = (String) comboRelatorios.getSelectedItem();
        if (selecionado == null) return;
        switch (selecionado) {
            case "Relatório de Alunos" -> areaResultado.setText(sistema.gerarRelatorioDeAlunos());
            case "Relatório de Professores" -> areaResultado.setText(sistema.gerarRelatorioDeProfessores());
            case "Relatório de Turmas" -> areaResultado.setText(sistema.gerarRelatorioDeTurmas());
            default -> areaResultado.setText("Selecione um tipo de relatório para visualizar os dados.");
        }
    });

    painel.add(painelFiltro, BorderLayout.NORTH);
    painel.add(new JScrollPane(areaResultado), BorderLayout.CENTER);
    return painel;
}

    // Aba de Perfil
    private JPanel criarAbaPerfil() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAlterarSenha = new JButton("Alterar Senha");
        btnAlterarSenha.addActionListener(_ -> alterarSenha());
        painel.add(new JLabel("Usuário: " + admin.getNome()));
        painel.add(btnAlterarSenha);
        return painel;
    }

    private JPanel criarPainelSair() {
        JPanel painelSair = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelSair.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Mudei o nome do botão para refletir a ação de logout
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

    // --- MÉTODOS DE LÓGICA ---

    private void atualizarTabelas() {
        atualizarTabelaUsuarios();
        atualizarTabelaDisciplinas();
    }

    private void atualizarTabelaUsuarios() {
        tableModelUsuarios.setRowCount(0);
        sistema.listarUsuarios().forEach(u -> tableModelUsuarios
                .addRow(new Object[] { u.getId(), u.getNome(), u.getEmail(), u.getCpf(), u.getTipoUsuario() }));
    }

    private void atualizarTabelaDisciplinas() {
        tableModelDisciplinas.setRowCount(0);
        sistema.listarDisciplinas().forEach(d -> tableModelDisciplinas
                .addRow(new Object[] { d.getCodigoDisciplina(), d.getNomeDisciplina(), d.getProfessorResponsavel() }));
    }

    private void removerUsuarioSelecionado() {
        int linha = tabelaUsuarios.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para remover.");
            return;
        }

        int id = (int) tableModelUsuarios.getValueAt(linha, 0);
        Usuario u = sistema.listarUsuarios().stream().filter(user -> user.getId() == id).findFirst().orElse(null);

        if (u != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Remover " + u.getNome() + "?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    sistema.removerUsuario(u);
                    atualizarTabelaUsuarios();
                } catch (SistemaException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void removerDisciplinaSelecionada() {
        int linha = tabelaDisciplinas.getSelectedRow();
        if (linha == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma disciplina para remover.");
            return;
        }

        String codigo = (String) tableModelDisciplinas.getValueAt(linha, 0);
        Disciplina d = sistema.listarDisciplinas().stream().filter(disc -> disc.getCodigoDisciplina().equals(codigo))
                .findFirst().orElse(null);

        if (d != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Remover a disciplina " + d.getNomeDisciplina() + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    sistema.removerDisciplina(d);
                    atualizarTabelaDisciplinas();
                } catch (SistemaException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // --- MÉTODOS DE ADICIONAR ---
    private void adicionarNovaDisciplina() {
        // --- Cria os campos para o formulário ---
        JTextField txtCodigo = new JTextField();
        JTextField txtNome = new JTextField();
        JTextField txtCpfProfessor = new JTextField();

        // --- Cria o painel do formulário ---
        JPanel painelFormulario = new JPanel(new GridLayout(0, 2, 5, 5));
        painelFormulario.add(new JLabel("Código da Disciplina:"));
        painelFormulario.add(txtCodigo);
        painelFormulario.add(new JLabel("Nome da Disciplina:"));
        painelFormulario.add(txtNome);
        painelFormulario.add(new JLabel("CPF do Professor Responsável:"));
        painelFormulario.add(txtCpfProfessor);

        // --- Mostra o diálogo com o formulário ---
        int resultado = JOptionPane.showConfirmDialog(this, painelFormulario,
                "Adicionar Nova Disciplina", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            String codigo = txtCodigo.getText().trim();
            String nome = txtNome.getText().trim();
            String cpf = txtCpfProfessor.getText().trim();

            // Verifica se os campos não estão vazios
            if (codigo.isEmpty() || nome.isEmpty() || cpf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            boolean professorExiste = sistema.listarUsuarios().stream()
                    .anyMatch(u -> u instanceof Professor && u.getCpf().equals(cpf));
            if (!professorExiste) {
                JOptionPane.showMessageDialog(this, "Nenhum professor encontrado com o CPF informado.", "Erro",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                Disciplina novaDisciplina = new Disciplina(nome, codigo, cpf);
                sistema.adicionarDisciplina(novaDisciplina);
                atualizarTabelaDisciplinas(); // Atualiza a tabela na tela
                JOptionPane.showMessageDialog(this, "Disciplina adicionada com sucesso!");
            } catch (SistemaException e) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar disciplina: " + e.getMessage(), "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void alterarSenha() {
        new AlterarSenhaDialog((Frame) SwingUtilities.getWindowAncestor(this), sistema, admin).setVisible(true);
    }

    private void mostrarDialogoAdicionarUsuario() {
        // --- Componentes do Formulário ---
        JTextField txtNome = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtCpf = new JTextField();
        JComboBox<String> comboTipo = new JComboBox<>(
                new String[] { "Aluno", "Professor", "Coordenador", "Administrador" });

        // --- Painel do Formulário ---
        JPanel painelFormulario = new JPanel();
        painelFormulario.setLayout(new BoxLayout(painelFormulario, BoxLayout.Y_AXIS));
        painelFormulario.add(new JLabel("Tipo de Usuário:"));
        painelFormulario.add(comboTipo);
        painelFormulario.add(new JLabel("Nome:"));
        painelFormulario.add(txtNome);
        painelFormulario.add(new JLabel("Email:"));
        painelFormulario.add(txtEmail);
        painelFormulario.add(new JLabel("CPF:"));
        painelFormulario.add(txtCpf);

        int resultado = JOptionPane.showConfirmDialog(this, painelFormulario, "Adicionar Novo Usuário",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (resultado == JOptionPane.OK_OPTION) {
            try {
                int novoId = sistema.listarUsuarios().size() + 1;
                String tipo = (String) comboTipo.getSelectedItem();
                Usuario novoUsuario;

                novoUsuario = switch (tipo) {
                    case "Aluno" -> new Aluno(novoId, txtNome.getText(), txtEmail.getText(), txtCpf.getText(),
                            "MAT-" + novoId, "Indefinido", 1);
                    case "Professor" -> new Professor(novoId, txtNome.getText(), txtEmail.getText(), txtCpf.getText(),
                            "Indefinida", "Indefinida");
                    case "Coordenador" ->
                        new Coordenador(novoId, txtNome.getText(), txtEmail.getText(), txtCpf.getText(), "Indefinido");
                    case "Administrador" ->
                        new Administrador(novoId, txtNome.getText(), txtEmail.getText(), txtCpf.getText(), "PADRAO");
                    default -> throw new SistemaException("Tipo", tipo, "Tipo de usuário inválido.");
                };

                sistema.adicionarUsuario(novoUsuario);
                atualizarTabelaUsuarios();
                JOptionPane.showMessageDialog(this, tipo + " adicionado com sucesso!");

            } catch (SistemaException | NullPointerException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao criar usuário: " + ex.getMessage(), "Erro de Validação",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}