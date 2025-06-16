package src.models;
public abstract class Usuario implements Serializador {
    protected int id;           
    protected String nome;
    protected String email;
    protected String cpf;
    protected String telefone;
    protected boolean ativo;


    public Usuario(int id, String nome, String email, String cpf, String telefone, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
        this.ativo = true;
    }

    public Usuario() {

    }

    public String getDescricaoCompleta() {
        return String.format("%s (%s) - %s", nome, getTipoUsuario(), email);
    }


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getTelefone(String telefone) {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isAtivo() {
        return ativo;
    }
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %d) - %s", nome, id, getTipoUsuario());
    }
}