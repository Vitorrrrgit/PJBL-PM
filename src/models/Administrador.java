package models;

public class Administrador extends Usuario {
    private static final long serialVersionUID = 1L;

    private final String nivelAcesso;

    public Administrador(int id, String nome, String email, String cpf, String nivelAcesso) {
        super(id, nome, email, cpf);
        this.nivelAcesso = nivelAcesso;
    }

    // Construtor vazio para serialização
    public Administrador() {
        super();
        this.nivelAcesso = "BÁSICO";
    }

    // ======= IMPLEMENTAÇÃO DOS MÉTODOS ABSTRATOS =======
    @Override
    public String getTipoUsuario() { 
        return "Administrador"; 
    }

    @Override
    public String[] getPermissoes() { 
        return new String[]{"ACESSO_TOTAL", "CRIAR_USUARIO", "EXCLUIR_USUARIO", "VER_TODOS_RELATORIOS"}; 
    }

    @Override
    public String gerarRelatorioPersonalizado() { 
        return String.format("Relatório Geral do Sistema (Nível de Acesso: %s)", nivelAcesso); 
    }

    @Override
    public boolean podeGerenciarUsuarios() { 
        return true; 
    }

    @Override
    public boolean podeVerRelatoriosCompletos() { 
        return true; 
    }

    @Override
    public boolean podeExportarDados() { 
        return true; 
    }

    @Override
    public boolean podeEditarFrequencia() { 
        return true; 
    }

    // ======= GETTERS E SETTERS =======
    public String getNivelAcesso() {
        return nivelAcesso;
    }

    // ======= MÉTODOS AUXILIARES =======
    public boolean temAcessoTotal() {
        return "TOTAL".equalsIgnoreCase(nivelAcesso) || "COMPLETO".equalsIgnoreCase(nivelAcesso);
    }

    public boolean podeAcessarConfiguracoesAvancadas() {
        return temAcessoTotal();
    }

    @Override
    public String toString() {
        return String.format("%s - Administrador (%s)", getNome(), nivelAcesso);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Administrador admin = (Administrador) obj;
        return getCpf().equals(admin.getCpf());
    }

    @Override
    public int hashCode() {
        return getCpf().hashCode();
    }
}