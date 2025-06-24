package visual;

import java.awt.*;
import javax.swing.*;
import models.Sistema;
import models.Usuario;
import models.persistencia.SistemaException;

public class AlterarSenhaDialog extends JDialog {

    private final Sistema sistema;
    private final Usuario usuarioLogado;

    private final JPasswordField txtSenhaAntiga = new JPasswordField(20);
    private final JPasswordField txtNovaSenha = new JPasswordField(20);
    private final JPasswordField txtConfirmarSenha = new JPasswordField(20);

    public AlterarSenhaDialog(Frame owner, Sistema sistema, Usuario usuarioLogado) {
        super(owner, "Alterar Senha", true);
        this.sistema = sistema;
        this.usuarioLogado = usuarioLogado;

        criarInterface();
        configurarJanela();
    }

    private void configurarJanela() {
        setSize(400, 220);
        setLocationRelativeTo(getOwner());
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private void criarInterface() {
        setLayout(new BorderLayout());

        // Painel principal com margem
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Formulário
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));

        formulario.add(new JLabel("Senha Atual:"));
        formulario.add(txtSenhaAntiga);

        formulario.add(new JLabel("Nova Senha:"));
        formulario.add(txtNovaSenha);

        formulario.add(new JLabel("Confirmar:"));
        formulario.add(txtConfirmarSenha);

        painelPrincipal.add(formulario, BorderLayout.CENTER);

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnSalvar = new JButton("Salvar");

        btnCancelar.addActionListener(e -> dispose());
        btnSalvar.addActionListener(e -> salvarSenha());

        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnSalvar);

        painelPrincipal.add(painelBotoes, BorderLayout.SOUTH);

        add(painelPrincipal);
    }

    private void salvarSenha() {
        String senhaAntiga = new String(txtSenhaAntiga.getPassword());
        String novaSenha = new String(txtNovaSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());

        if (senhaAntiga.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!novaSenha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "Nova senha e confirmação não conferem.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (novaSenha.length() < 4) {
            JOptionPane.showMessageDialog(this, "Nova senha deve ter pelo menos 4 caracteres.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            sistema.alterarSenha(usuarioLogado, senhaAntiga, novaSenha);
            JOptionPane.showMessageDialog(this, "Senha alterada com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SistemaException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}