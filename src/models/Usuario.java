package models;

import java.io.Serializable;
import java.util.Random;

public abstract class Usuario implements Serializable {
    @java.io.Serial
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
        this.senha = gerarSenhaInicial(nome);
        this.ativo = true;
    }

    // Construtor vazio para serialização
    public Usuario() {
        this.ativo = true;
    }

    private String gerarSenhaInicial(String nome) {
        if (nome == null || nome.isEmpty()) {
            return "user" + new Random().nextInt(9999);
        }
        String primeiraLetra = nome.substring(0, 1).toUpperCase();
        String digitos = String.format("%04d", new Random().nextInt(10000));
        return primeiraLetra + digitos + "!";
    }

    // ======= MÉTODOS ABSTRATOS QUE TODAS AS SUBCLASSES DEVEM IMPLEMENTAR =======
    public abstract String getTipoUsuario();
    public abstract String[] getPermissoes();
    public abstract String gerarRelatorioPersonalizado();
    public abstract boolean podeGerenciarUsuarios();
    public abstract boolean podeVerRelatoriosCompletos();
    public abstract boolean podeExportarDados();
    public abstract boolean podeEditarFrequencia();

    // ======= MÉTODO CONCRETO (PODE SER SOBRESCRITO) =======
    public String getDescricaoCompleta() {
        return String.format("%s (%s) - %s", nome, getTipoUsuario(), email);
    }

    // ======= GETTERS E SETTERS =======
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return nome; // Usado para exibição em JComboBox
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Usuario usuario = (Usuario) obj;
        return id == usuario.id || (cpf != null && cpf.equals(usuario.cpf));
    }

    @Override
    public int hashCode() {
        return cpf != null ? cpf.hashCode() : Integer.hashCode(id);
    }
}