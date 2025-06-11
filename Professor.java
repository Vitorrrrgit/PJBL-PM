import java.util.ArrayList;

// subclass professor
public class Professor extends Usuario {

    // atributos do professor
    String disciplina;

    // construtor professor
    
    Professor(int id, String tipoUsuario, String nome, String email, String telefone, String disciplina) {
        // atributos de usuario
        super(id, tipoUsuario, nome, email, telefone);

        // atributos do professor
        this.disciplina = disciplina;
    }

    // metodos do professor:

    // lista dos alunos
    public void listaAlunos() {
        
    }

    // iniciar chamada
    public void iniciarChamada() {

    }

    // exibir chamadas
    public void exibirChamada() {

    }

}
