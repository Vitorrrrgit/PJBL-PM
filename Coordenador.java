import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.Serializable;

public class Coordenador extends Usuario implements Serializable{
    private List<Professor> professores = new ArrayList<>();

    public Coordenador(int id, String tipoUsuario, String nome, String email, String telefone){
        super(id, nome, email, telefone);
    }

    public List<Professor> getProfessores() {
        return professores;
    } 

    public void addProfessor(Professor professores){
        this.professores.add(professores);
    }

    public void removeProfessor(Professor professores){
        this.professores.remove(professores);
    }

}