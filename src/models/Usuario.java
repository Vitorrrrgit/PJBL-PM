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

    public Usuario(int id, String nome, String email, String cpf) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.senha = gerarSenhaInicial(nome);
    }

    private String gerarSenhaInicial(String nome) {
        if (nome == null || nome.isEmpty()) {
            return "user" + new Random().nextInt(9999);
        }
        String primeiraLetra = nome.substring(0, 1).toUpperCase();
        String digitos = String.format("%04d", new Random().nextInt(10000));
        return primeiraLetra + digitos + "!";
    }

    // --- GETTERS E SETTERS ---
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // --- MÉTODOS ABSTRATOS DE PERMISSÃO (RESTAURADOS) ---
    public abstract String getTipoUsuario();

    public abstract String[] getPermissoes();

    public abstract String gerarRelatorioPersonalizado();

    public abstract boolean podeGerenciarUsuarios();

    public abstract boolean podeVerRelatoriosCompletos();

    public abstract boolean podeExportarDados();

    public abstract boolean podeEditarFrequencia();

    @Override
    public String toString() {
        return nome; // Usado para exibição em JComboBox
    }
}