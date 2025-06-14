import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.table.DefaultTableModel;

public class Coordenador extends Usuario implements MenuCoordenador, Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Professor> professores;
    
    public Coordenador(int id, String nome, String email, String telefone) {
        super(id, "COORDENADOR", nome, email, telefone);
        this.professores = new ArrayList<>();
    }

    // Métodos existentes (mantidos)
    @Override
    public boolean adicionarProfessor(Professor professor) {
        if (professor == null || existeProfessor(professor.getId())) {
            return false;
        }
        return professores.add(professor);
    }

    @Override 
    public boolean removerProfessor(int professorId) {
        return professores.removeIf(p -> p.getId() == professorId);
    }

    @Override
    public List<Professor> listarProfessores() {
        return new ArrayList<>(professores);
    }

    @Override
    public boolean atribuirDisciplinaProfessor(int professorId, Disciplina disciplina) {
        Optional<Professor> professor = buscarProfessor(professorId);
        if (professor.isPresent() && disciplina != null) {
            professor.get().adicionarDisciplina(disciplina);
            return true;
        }
        return false;
    }

    @Override
    public boolean removerDisciplinaProfessor(int professorId, int disciplinaId) {
        return buscarProfessor(professorId)
                .map(p -> p.removerDisciplina(disciplinaId))
                .orElse(false);
    }

    // ---- Novos métodos para Swing ----
    @Override
    public DefaultTableModel getModeloTabelaProfessores() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Email", "Disciplinas"}, 0);
        
        for (Professor prof : professores) {
            model.addRow(new Object[]{
                prof.getId(),
                prof.getNome(),
                prof.getEmail(),
                String.join(", ", prof.getDisciplinas().stream()
                    .map(Disciplina::getNome).toList())
            });
        }
        return model;
    }

    @Override
    public DefaultTableModel getModeloTabelaDisciplinas(int professorId) {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID Disciplina", "Nome"}, 0);
        
        buscarProfessor(professorId).ifPresent(prof -> {
            prof.getDisciplinas().forEach(d -> {
                model.addRow(new Object[]{d.getId(), d.getNome()});
            });
        });
        return model;
    }

    @Override
    public String gerarRelatorioProfessoresHTML() {
        StringBuilder sb = new StringBuilder("<html><h2>Relatório de Professores</h2><ul>");
        professores.forEach(prof -> {
            sb.append(String.format(
                "<li><b>%s</b> (ID: %d)<br>Email: %s<br>Disciplinas: %s<hr></li>",
                prof.getNome(),
                prof.getId(),
                prof.getEmail(),
                String.join(", ", prof.getDisciplinas().stream()
                    .map(Disciplina::getNome).toList())
            ));
        });
        sb.append("</ul></html>");
        return sb.toString();
    }

    // ---- Métodos existentes mantidos ----
    @Override
    public String gerarRelatorioProfessores() {
        StringBuilder sb = new StringBuilder("--- RELATÓRIO DE PROFESSORES ---\n");
        professores.forEach(prof -> {
            sb.append(String.format(
                "ID: %d | Nome: %s | Email: %s\nDisciplinas: %s\n%s\n",
                prof.getId(),
                prof.getNome(),
                prof.getEmail(),
                prof.getDisciplinas().stream().map(Disciplina::getNome).toList(),
                "-".repeat(50)
            ));
        });
        return sb.toString();
    }

    @Override
    public String gerarRelatorioDisciplinas() {
        // Implementação similar para disciplinas
        return "Relatório de disciplinas não implementado ainda";
    }

    @Override
    public boolean existeProfessor(int professorId) {
        return professores.stream().anyMatch(p -> p.getId() == professorId);
    }

    @Override
    public boolean professorPossuiDisciplina(int professorId, int disciplinaId) {
        return buscarProfessor(professorId)
                .map(p -> p.getDisciplinas().stream().anyMatch(d -> d.getId() == disciplinaId))
                .orElse(false);
    }

    private Optional<Professor> buscarProfessor(int professorId) {
        return professores.stream()
                .filter(p -> p.getId() == professorId)
                .findFirst();
    }
}