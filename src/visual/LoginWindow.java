package visual;

import java.awt.*;
import java.io.File;
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

    public LoginWindow(Sistema sistema) {
        super("Sistema de Frequência - Login");
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

        // Estilizando os botões
        btnLogin.setBackground(Color.BLUE);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);

        btnCancelar.setBackground(Color.RED);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setOpaque(true);
        btnCancelar.setBorderPainted(false);
    }

    private void organizarLayout() {
        setLayout(new BorderLayout());

        // ===== PAINEL DO TÍTULO E IMAGEM (NORTE) =====
        JPanel painelTitulo = new JPanel(new BorderLayout());
        painelTitulo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Título
        JLabel lblTitulo = new JLabel("Sistema de Frequência");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        painelTitulo.add(lblTitulo, BorderLayout.NORTH);

        // IMAGEM - com tratamento de erro
        JLabel lblImagem = criarLabelImagem();
        if (lblImagem != null) {
            painelTitulo.add(lblImagem, BorderLayout.CENTER);
        }

        // ===== PAINEL DO FORMULÁRIO (CENTRO) =====
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        painelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtEmail, gbc);

        // Senha
        gbc.gridx = 0; gbc.gridy = 1;
        painelFormulario.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        painelFormulario.add(txtSenha, gbc);

        // Botões
        gbc.gridy = 2; gbc.gridx = 1; gbc.anchor = GridBagConstraints.EAST;
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnLogin);
        painelFormulario.add(painelBotoes, gbc);

        // ===== PAINEL DE INFORMAÇÕES (SUL) =====
        JPanel painelInfo = new JPanel();
        painelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        painelInfo.setBackground(new Color(220, 220, 220));
        JLabel lblInfo = new JLabel(
            "<html><center>Sistema de Frequência Acadêmica - POO - 2025<br>" +
            "<b>Alunos:</b> Alana Vasconcelos, Guilherme Montoya e Vitor Henrique</center></html>"
        );
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        painelInfo.add(lblInfo);

        // Adicionar painéis à janela
        add(painelTitulo, BorderLayout.NORTH);
        add(painelFormulario, BorderLayout.CENTER);
        add(painelInfo, BorderLayout.SOUTH);
    }

    private JLabel criarLabelImagem() {
        // Lista de possíveis locais para a imagem
        String[] possiveisCaminhos = {
            "src/image/imagempraTentarSistema.png",
            "image/imagempraTentarSistema.png",
        };

        for (String caminho : possiveisCaminhos) {
            try {
                File arquivo = new File(caminho);
                if (arquivo.exists()) {
                    ImageIcon icone = new ImageIcon(caminho);
                    
                    // Redimensionar a imagem se necessário
                    Image img = icone.getImage();
                    Image imgRedimensionada = img.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                    ImageIcon iconeRedimensionado = new ImageIcon(imgRedimensionada);
                    
                    JLabel label = new JLabel(iconeRedimensionado);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    System.out.println(" Imagem carregada: " + caminho);
                    return label;
                }
            } catch (Exception e) {
                System.err.println(" Erro ao carregar imagem " + caminho + ": " + e.getMessage());
            }
        }

        // Se não encontrou nenhuma imagem, cria um placeholder
        JLabel placeholder = new JLabel("📚");
        placeholder.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        placeholder.setHorizontalAlignment(SwingConstants.CENTER);
        System.out.println(" Nenhuma imagem encontrada. Usando emoji placeholder.");
        return placeholder;
    }

    private void adicionarListeners() {
        btnLogin.addActionListener(_ -> fazerLogin());
        btnCancelar.addActionListener(_ -> System.exit(0));
        txtSenha.addActionListener(_ -> fazerLogin());
        
        // Enter no campo email vai para senha
        txtEmail.addActionListener(_ -> txtSenha.requestFocus());
    }

    private void fazerLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword()).trim();

        System.out.println(" TENTATIVA DE LOGIN:");
        System.out.println(" Email: '" + email + "'");

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = sistema.buscarUsuarioPorEmail(email);
            System.out.println(" Usuário encontrado: " + usuario.getNome());

            if (usuario.getSenha().equals(senha)) {
                JOptionPane.showMessageDialog(this, "Bem-vindo, " + usuario.getNome() + "!", "Login bem-sucedido",
                        JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                new MainWindow(sistema, usuario).setVisible(true);
            } else {
                throw new SistemaException("Login", email, "Senha incorreta.");
            }
        } catch (SistemaException ex) {
            System.out.println(" Erro de login: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Erro de Login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            txtSenha.setText("");
            txtSenha.requestFocus();
        }
    }
}