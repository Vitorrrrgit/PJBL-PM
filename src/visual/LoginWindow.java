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

    public LoginWindow() {
        super("Sistema de Frequência - Login");
        this.sistema = new Sistema();

        configurarJanela();
        criarComponentes();
        organizarLayout();
        adicionarListeners();
        setVisible(true);
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
    
    // --- LINHAS FALTANTES PARA CORRIGIR A COR ---
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

        // Criando um JLabel para exibir a imagem
        JLabel lblImagem = new JLabel(icone);
        painelTitulo.add(lblImagem, BorderLayout.SOUTH);

    } catch (Exception e) {
        // Se a imagem não for encontrada, apenas imprime um aviso no console
        System.err.println("Aviso: Imagem do logo não encontrada.");
    }

        // Painel do Formulário e Botões (Centro)
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Linha 0: Email
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtEmail, gbc);

        // Linha 1: Senha
        gbc.gridx = 0; gbc.gridy = 1;
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
        JLabel lblInfo = new JLabel("<html>Projeto de Sistema de Frequência<br>Alunos:<b>Alana Vasconcelos, Guilherme Montoya e Vitor Henrique</b></html>");
        painelInfo.add(lblInfo);

        // Adicionando os painéis principais à janela
        setLayout(new BorderLayout());
        add(painelTitulo, BorderLayout.NORTH);
        add(painelFormulario, BorderLayout.CENTER);
        add(painelInfo, BorderLayout.SOUTH);
    }
    
    private void adicionarListeners() {
        // Ação para o botão de Login
        btnLogin.addActionListener(_ -> fazerLogin());

        // Ação para o botão Cancelar
        btnCancelar.addActionListener(_ -> System.exit(0));

        // Pressionar Enter no campo de senha também tenta fazer login
        txtSenha.addActionListener(_ -> fazerLogin());
    }

    private void fazerLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = sistema.buscarUsuarioPorEmail(email);
            if (usuario != null && usuario.getSenha().equals(senha)) {
                // Login bem-sucedido
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome() + "!", "Login bem-sucedido", JOptionPane.INFORMATION_MESSAGE);
                this.dispose(); // Fecha a janela de login
                new MainWindow(usuario).setVisible(true); // Abre a janela principal
            } else {
                // Senha incorreta
                throw new SistemaException("Login", email, "Senha incorreta.");
            }
        } catch (SistemaException ex) {
            // Usuário não encontrado ou senha incorreta
            JOptionPane.showMessageDialog(this, "Erro de Login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
            txtSenha.requestFocus();
        }
    }
}