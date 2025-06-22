package visual;

import java.awt.*;
import javax.swing.*;
import models.Sistema;
import models.Usuario;
import persistencia.SistemaException;

public class LoginWindow extends JFrame {
    private final Sistema sistema;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnLogin;
    private JButton btnCancelar;

    /**
     * --- CORREÇÃO 1: Construtor modificado ---
     * O construtor agora recebe a instância única do Sistema, em vez de criar uma nova.
     * @param sistema A instância única do sistema para ser usada pela janela.
     */
    public LoginWindow(Sistema sistema) {
        super("Sistema de Frequência - Login");
        // Usa a instância do sistema que foi passada como parâmetro.
        this.sistema = sistema;

        configurarJanela();
        criarComponentes();
        organizarLayout();
        adicionarListeners();
    }

    private void configurarJanela() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setBackground(Color.WHITE);
    }

    private void criarComponentes() {
        txtEmail = new JTextField(20);
        txtSenha = new JPasswordField(20);
        btnLogin = new JButton("Entrar");
        btnCancelar = new JButton("Cancelar");

        // Estilizando o botão de login para ter mais destaque
        btnLogin.setBackground(Color.BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // --- LINHAS PARA GARANTIR A COR DE FUNDO DOS BOTÕES ---
        btnLogin.setOpaque(true);
        btnCancelar.setOpaque(true);
        btnCancelar.setBorderPainted(false);
        btnLogin.setBorderPainted(false);
    }

    private void organizarLayout() {
        // Painel do Título (Norte)
        JPanel painelTitulo = new JPanel(new BorderLayout());
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel lblTitulo = new JLabel("Sistema de Frequência");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelTitulo.add(lblTitulo, BorderLayout.CENTER);

        try {
            ImageIcon icone = new ImageIcon(getClass().getResource("/image/imagempraTentarSistema.png"));
            JLabel lblImagem = new JLabel(icone);
            painelTitulo.add(lblImagem, BorderLayout.SOUTH);
        } catch (Exception e) {
            System.err.println("Aviso: Imagem do logo não encontrada.");
        }

        // Painel do Formulário e Botões (Centro)
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0: Email
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtEmail, gbc);

        // Linha 1: Senha
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelFormulario.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtSenha, gbc);

        // Linha 2: Painel com os botões
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnLogin);
        painelFormulario.add(painelBotoes, gbc);

        // Painel de Informações (Sul)
        JPanel painelInfo = new JPanel();
        painelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelInfo.setBackground(new Color(220, 220, 220));
        JLabel lblInfo = new JLabel(
                "<html>Projeto de Sistema de Frequência<br>Alunos:<b>Alana Vasconcelos, Guilherme Montoya e Vitor Henrique</b></html>");
        painelInfo.add(lblInfo);

        // Adicionando os painéis principais à janela
        setLayout(new BorderLayout());
        add(painelTitulo, BorderLayout.NORTH);
        add(painelFormulario, BorderLayout.CENTER);
        add(painelInfo, BorderLayout.SOUTH);
    }

    private void adicionarListeners() {
        btnLogin.addActionListener(_ -> fazerLogin());
        btnCancelar.addActionListener(_ -> System.exit(0));
        txtSenha.addActionListener(_ -> fazerLogin());
    }

    private void fazerLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        System.out.println("🔍 TENTATIVA DE LOGIN:");
        System.out.println("📧 Email digitado: '" + email + "'");
        System.out.println("🔑 Senha digitada: '" + senha + "'");

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = sistema.buscarUsuarioPorEmail(email);
            System.out.println("✅ Usuário encontrado: " + usuario.getNome());
            System.out.println("🔑 Senha no sistema: '" + usuario.getSenha() + "'");
            System.out.println("🔍 Senhas são iguais? " + usuario.getSenha().equals(senha));

            if (usuario.getSenha().equals(senha)) {
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome() + "!", "Login bem-sucedido",
                        JOptionPane.INFORMATION_MESSAGE);
                this.dispose();

                /**
                 * --- CORREÇÃO 2: Passar a instância existente do Sistema ---
                 * A MainWindow (e seu construtor) também precisa ser ajustada para receber
                 * a instância do sistema.
                 */
                new MainWindow(sistema, usuario).setVisible(true);

            } else {
                throw new SistemaException("Login", email, "Senha incorreta.");
            }
        } catch (SistemaException ex) {
            System.out.println("❌ Erro de login: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Erro de Login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
            txtSenha.requestFocus();
        }
    }
}