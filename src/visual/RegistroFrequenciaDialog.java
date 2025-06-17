package visual;

// IMPORTS ADICIONADOS PARA RESOLVER OS ERROS
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;
import models.Frequencia;
import models.Sistema;
import models.Usuario;


public class RegistroFrequenciaDialog extends JDialog {

    private final Sistema sistema;
    private final Usuario usuarioLogado;

    private final JTextField txtMatricula = new JTextField(15);
    private final JTextField txtDisciplina = new JTextField(15);
    private final JTextField txtData = new JTextField(12);
    private final JComboBox<String> cbPresenca = new JComboBox<>(new String[] { "Presente", "Falta" });
    private final JTextArea txtObservacoes = new JTextArea(3, 15);

    public RegistroFrequenciaDialog(Frame owner, Sistema sistema, Usuario usuarioLogado) {
        super(owner, "Registrar Nova Frequência", true);
        this.sistema = sistema;
        this.usuarioLogado = usuarioLogado;

        setLayout(new BorderLayout(10, 10));
        add(criarFormulario(), BorderLayout.CENTER);
        add(criarPainelBotoes(), BorderLayout.SOUTH);

        pack(); // Ajusta o tamanho do diálogo ao conteúdo
        setLocationRelativeTo(owner);
    }

    private JPanel criarFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Matricula
        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(new JLabel("Matrícula Aluno:"), gbc);
        gbc.gridx = 1;
        panel.add(txtMatricula, gbc);

        // Disciplina
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Disciplina:"), gbc);
        gbc.gridx = 1;
        panel.add(txtDisciplina, gbc);

        // Data
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Data (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        panel.add(txtData, gbc);
        txtData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Presença
        gbc.gridy = 3;
        gbc.gridx = 0;
        panel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        panel.add(cbPresenca, gbc);

        // Observações
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panel.add(new JLabel("Observações:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JScrollPane(txtObservacoes), gbc);

        return panel;
    }

    private JPanel criarPainelBotoes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(_ -> salvarFrequencia());
        btnCancelar.addActionListener(_ -> dispose());

        panel.add(btnSalvar);
        panel.add(btnCancelar);
        return panel;
    }

    private void salvarFrequencia() {
        String matricula = txtMatricula.getText().trim();
        String disciplina = txtDisciplina.getText().trim();
        String dataStr = txtData.getText().trim();

        if (matricula.isEmpty() || disciplina.isEmpty() || dataStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate data = LocalDate.parse(dataStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            boolean presente = cbPresenca.getSelectedIndex() == 0;
            String observacoes = txtObservacoes.getText().trim();
            int novoId = (int) System.currentTimeMillis(); // Usando int para consistência

            Frequencia freq = new Frequencia(novoId, matricula, usuarioLogado.getCpf(), disciplina, data, presente,
                    observacoes);
            sistema.adicionarFrequencia(freq);

            JOptionPane.showMessageDialog(this, "Frequência registrada com sucesso!", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Fecha o diálogo

        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use dd/MM/yyyy.", "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (persistencia.SistemaException ex) { // <-- Bloco específico para erros do nosso sistema
            JOptionPane.showMessageDialog(this, "Erro ao registrar frequência: " + ex.getMessage(), "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
