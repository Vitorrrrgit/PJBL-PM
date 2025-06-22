package models;

public class Administrador extends Usuario {
    private final String nivelAcesso;

    public Administrador(int id, String nome, String email, String cpf, String nivelAcesso) {
        super(id, nome, email, cpf);
        this.nivelAcesso = nivelAcesso;
    }

    @Override public String getTipoUsuario() { return "Administrador"; }
    @Override public String[] getPermissoes() { return new String[]{"ACESSO_TOTAL"}; }
    @Override public String gerarRelatorioPersonalizado() { return "Relatório geral do sistema"; }
    @Override public boolean podeGerenciarUsuarios() { return true; }
    @Override public boolean podeVerRelatoriosCompletos() { return true; }
    @Override public boolean podeExportarDados() { return true; }
    @Override public boolean podeEditarFrequencia() { return true; }
}