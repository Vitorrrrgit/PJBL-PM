import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Coordenador extends Usuario implements MenuCoordenador, Serializable {
    private static final long serialVersionUID = 1L;
    private final List<Professor> professores;
    
    public Coordenador(int id, String nome, String email, String telefone) {
        super(id, "COORDENADOR", nome, email, telefone);
        this.professores = new ArrayList<>();
    }

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
            );
        });
        return sb.toString();
    }

    @Override
    public String gerarRelatorioDisciplinas() {
        // Implementação similar para disciplinas
        return "";
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