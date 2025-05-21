// superclass usuario
public abstract class Usuario {

    // atributos usuarios
    private int     id;
    private String  tipoUsuario;
    private String  nome;
    private String  email;
    private String  telefone;

    // construtor usuario
    public Usuario(int id, String tipoUsuario, String nome, String email, String telefone) {
        this.id             = id;
        this.tipoUsuario    = tipoUsuario;
        this.nome           = nome;
        this.email          = email;
        this.telefone       = telefone;
    }

    // metodo abstrato
    public void exibirTipoUsuario() {
        System.out.println("Tipo do usuario: " + tipoUsuario);
    }

    // getter
    public String getNome() {
        return nome;
    }

}