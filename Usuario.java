package models;

public abstract class Usuario {
    private String nome;
    private String id;
    private String email;
    private String senha;
    private String tipoUsuario;
    private int  telefone;


    public Usuario(String nome, String id, String email, String senha, String cargo, int telefone) {
        this.nome = nome;
        this.id = id;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = cargo;
        this.telefone = telefone;
    }
    public String getNome() {
        return nome;
    }
    public String getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getSenha() {
        return senha;
    }
    public String gettipoUsuario() {
        return tipoUsuario;
    }
    public int getTelefone() {
        return telefone;
    }
}


