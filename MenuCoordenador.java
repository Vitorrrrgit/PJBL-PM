import java.io.Serializable;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public interface MenuCoordenador extends Serializable {
    
    // Métodos básicos de gestão
    boolean adicionarProfessor(Professor professor);
    boolean removerProfessor(int professorId);
    List<Professor> listarProfessores();
    
    // Métodos de atribuição
    boolean atribuirDisciplinaProfessor(int professorId, Disciplina disciplina);
    boolean removerDisciplinaProfessor(int professorId, int disciplinaId);
    
    // Relatórios (versões para Swing)
    DefaultTableModel getModeloTabelaProfessores();
    DefaultTableModel getModeloTabelaDisciplinas(int professorId);
    String gerarRelatorioProfessoresHTML();
    
    // Validações
    boolean existeProfessor(int professorId);
    boolean professorPossuiDisciplina(int professorId, int disciplinaId);
    
    // Métodos para diálogos Swing
    default void exibirMenuCoordenador(JFrame parentFrame) {
        JDialog menuDialog = new JDialog(parentFrame, "Menu Coordenador", true);
        menuDialog.setLayout(new BorderLayout());
        menuDialog.setSize(400, 300);
        
        JTabbedPane abas = new JTabbedPane();
        
        // Aba de Professores
        abas.addTab("Professores", criarPainelProfessores());
        
        // Aba de Disciplinas
        abas.addTab("Disciplinas", criarPainelDisciplinas());
        
        menuDialog.add(abas, BorderLayout.CENTER);
        menuDialog.setLocationRelativeTo(parentFrame);
        menuDialog.setVisible(true);
    }
    
    // Métodos default para componentes Swing
    default JPanel criarPainelProfessores() {
        JPanel panel = new JPanel(new BorderLayout());
        
        DefaultTableModel modelo = getModeloTabelaProfessores();
        JTable tabela = new JTable(modelo);
        
        JScrollPane scrollPane = new JScrollPane(tabela);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel botoesPanel = new JPanel();
        JButton adicionarBtn = new JButton("Adicionar Professor");
        JButton removerBtn = new JButton("Remover Professor");
        
        botoesPanel.add(adicionarBtn);
        botoesPanel.add(removerBtn);
        panel.add(botoesPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    default JPanel criarPainelDisciplinas() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Implementação similar para disciplinas
        JLabel placeholder = new JLabel("Painel de Disciplinas", JLabel.CENTER);
        panel.add(placeholder, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Método para console (mantido para compatibilidade)
    default void exibirMenuCoordenador() {
        System.out.println("\n--- MENU COORDENADOR ---");
        System.out.println("1. Gerenciar Professores");
        System.out.println("2. Gerenciar Disciplinas");
        System.out.println("3. Gerar Relatórios");
        System.out.println("0. Sair");
    }
}