package models;

public class Administrador extends Usuario {
    private String nivelAcesso;

    public Administrador() {
        super();
    }

    public Administrador(int id, String nome, String email, String cpf, String nivelAcesso) {
        super(id, nome, email, cpf);
        this.nivelAcesso = nivelAcesso;
    }

    // IMPLEMENTAÇÃO OBRIGATÓRIA DOS MÉTODOS ABSTRATOS
    @Override
    public String getTipoUsuario() {
        return "Administrador";
    }

    @Override
    public String[] getPermissoes() {
        return new String[]{"ACESSO_TOTAL"};
    }

    @Override
    public String gerarRelatorioPersonalizado() {
        return "Relatório geral do sistema";
    }

    @Override
    public boolean podeEditarFrequencia() {
        return true;
    }

    @Override
    public boolean podeGerenciarUsuarios() {
        return true;
    }

    @Override
    public boolean podeExportarDados() {
        return true;
    }

    @Override
    public boolean podeVerRelatoriosCompletos() {
        return true;
    }

    // Getters e Setters
    public String getNivelAcesso() { return nivelAcesso; }
    public void setNivelAcesso(String nivelAcesso) { this.nivelAcesso = nivelAcesso; }
}