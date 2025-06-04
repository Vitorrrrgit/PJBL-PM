package src;
import java.util.List;

public class Professor extends Usuario {
private List<String> disciplina;
    
public Professor(String nome, String id, String email, String senha, String cargo, int telefone, List<String> disciplina) {
        super(nome, id, email, senha, cargo, telefone);
        this.disciplina = disciplina;
    }

    public List<String> getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(List<String> disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "disciplina=" + disciplina +
                '}';
    }
}
