import java.io.Serializable;

/**
 * Classe base de todos os tipos de usuário do sistema de frequência.
 *
 * <p>
 * A ideia do projeto é permitir o cadastro de usuários (alunos, professores e
 * administradores) e possibilitar que cada um tenha um menu específico de
 * funcionalidades. Esta classe concentra informações comuns e alguns métodos
 * utilitários.
 * </p>
 */
public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private int    id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;

    /**
     * Construtor básico utilizado pelas classes filhas. Não exige senha.
     */
    public Usuario(int id, String nome, String email, String telefone) {
        this(id, nome, email, telefone, "");
    }

    /**
     * Construtor completo permitindo informar a senha do usuário.
     */
    public Usuario(int id, String nome, String email, String telefone, String senha) {
        this.id       = id;
        this.nome     = nome;
        this.email    = email;
        this.telefone = telefone;
        this.senha    = senha;
    }

    // getters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }

    /** Atualiza email e telefone do usuário. */
    public void atualizarContato(String email, String telefone) {
        this.email    = email;
        this.telefone = telefone;
    }

    /** Define ou altera a senha do usuário. */
    public void setSenha(String senha) { this.senha = senha; }

    /**
     * Verifica se a senha informada é válida para este usuário.
     */
    public boolean autenticar(String senha) {
        return this.senha != null && this.senha.equals(senha);
    }

    /** Menu específico de cada tipo de usuário. */
    public abstract void exibirMenu();
    @Override
    public String toString() {
        return String.format("%s (ID: %d)", nome, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario usuario = (Usuario) obj;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}
