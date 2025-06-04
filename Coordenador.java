public class Coordenador extends Usuario {
    private String nome;
    private int id;
    private int telefone;
    private String email;

    public Coordenador(String nome, int id, int telefone, String email) {
        this.nome = nome;
        this.id = id;
        this.telefone = telefone;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public int getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

}