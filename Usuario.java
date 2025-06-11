// superclass usuario
public abstract class Usuario {

    // atributos usuarios
    private int     id;
    private String  nome;
    private String  email;
    private String  telefone;

    // construtor usuario
    public Usuario(int id, String nome, String email, String telefone) {
        this.id             = id;
        this.nome           = nome;
        this.email          = email;
        this.telefone       = telefone;
    }

    // metodo abstrato

    // getter
    public int getId(){
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail(){
        return email;
    }

    public String getTelefone(){
        return telefone;
    }
}