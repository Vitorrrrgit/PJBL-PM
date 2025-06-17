package visual;

import java.awt.*;
import javax.swing.*;
import models.Sistema;
import models.Usuario;
import persistencia.SistemaException;

 // ALTERAR SENHA USUARIO LOGADO

public class AlterarSenhaDialog extends JDialog {

    private final Sistema sistema;
    private final Usuario usuarioLogado;

    private final JPasswordField txtSenhaAntiga = new JPasswordField(20);
    private final JPasswordField txtNovaSenha = new JPasswordField(20);
    private final JPasswordField txtConfirmarSenha = new JPasswordField(20);

    public AlterarSenhaDialog(Frame owner, Sistema sistema, Usuario usuarioLogado) {
        super(owner, "Alterar Minha Senha", true);
        this.sistema = sistema;
        this.usuarioLogado = usuarioLogado;

        setLayout(new BorderLayout(10, 10));
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    private JPanel criarFormulario() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0; gbc.gridx = 0; painel.add(new JLabel("Senha Atual:"), gbc);
        gbc.gridx = 1; painel.add(txtSenhaAntiga, gbc);

        gbc.gridy = 1; gbc.gridx = 0; painel.add(new JLabel("Nova Senha:"), gbc);
        gbc.gridx = 1; painel.add(txtNovaSenha, gbc);

        gbc.gridy = 2; gbc.gridx = 0; painel.add(new JLabel("Confirmar Nova Senha:"), gbc);
        gbc.gridx = 1; painel.add(txtConfirmarSenha, gbc);

        return painel;
    }

    private JPanel criarPainelBotoes() {
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(_ -> salvarNovaSenha());
        btnCancelar.addActionListener(_ -> dispose());

        painel.add(btnCancelar);
        painel.add(btnSalvar);
        return painel;
    }

    private void salvarNovaSenha() {
        String senhaAntiga = new String(txtSenhaAntiga.getPassword());
        String novaSenha = new String(txtNovaSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());

        if (!novaSenha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "A 'Nova Senha' e a 'Confirmação' não são iguais.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            sistema.alterarSenha(usuarioLogado, senhaAntiga, novaSenha);
            JOptionPane.showMessageDialog(this, "Senha alterada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SistemaException e) {
            JOptionPane.showMessageDialog(this, "Não foi possível alterar a senha: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}