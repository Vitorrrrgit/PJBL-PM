import java.io.Serializable;

// Superclasse abstrata Usuario
public abstract class Usuario implements Serializable {

    private int     id;
    private String  nome;
    private String  email;
    private String  telefone;

    public Usuario(int id, String nome, String email, String telefone) {
        this.id       = id;
        this.nome     = nome;
        this.email    = email;
        this.telefone = telefone;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }

    // metodo abstrato para subclasses
    public abstract void exibirMenu();
}
