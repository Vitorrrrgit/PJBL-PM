import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Coordenador extends Usuario {
    private List<Professor> professores = new ArrayList<>();

    public Coordenador(String nome, int id, int telefone, String email) {
        super(nome, id, telefone, email);
    }

    public List<Professor> getProfessores() {
        return professores;
    } 

    public void addProfessor(Professor professores){
        professores.add(professores);
    }

    // 1º Função
    public String getNome() {
        return nome;
    }
    //2º Função
    public int getId() {
        return id;
    }
    //3º Função
    public int getTelefone() {
        return telefone;
    }
    //4º Função 
    public String getEmail() {
        return email;
    }

}