package models;

import java.io.Serializable;

public abstract class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected String nome;
    protected String email;
    protected String cpf;
    protected String senha;
    protected boolean ativo;

    public Usuario(int id, String nome, String email, String cpf) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.senha = "123"; // Senha padrão para todos
        this.ativo = true;
    }


    public Usuario() {
        this.senha = "123";
        this.ativo = true;
    }

    public abstract String getTipoUsuario();
    public abstract String[] getPermissoes();
    public abstract String gerarRelatorioPersonalizado();
    public abstract boolean podeEditarFrequencia();
    public abstract boolean podeGerenciarUsuarios();
    public abstract boolean podeExportarDados();
    public abstract boolean podeVerRelatoriosCompletos();

    public String getDescricaoCompleta() {
        return String.format("%s (%s) - %s", nome, getTipoUsuario(), email);
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %d) - %s", nome, id, getTipoUsuario());
    }

    // ======= GETTERS E SETTERS =======
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Usuario usuario = (Usuario) o;
    return this.id == usuario.id; // Compara os usuários pelo ID
}

@Override
public int hashCode() {
    return Integer.hashCode(id); // Gera um hash code baseado no ID
}
}