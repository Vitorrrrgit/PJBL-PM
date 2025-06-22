package models;

public class Coordenador extends Usuario {
    private final Curso curso; // Mudança: agora é objeto Curso

    public Coordenador(int id, String nome, String email, String cpf, Curso curso) {
        super(id, nome, email, cpf);
        this.curso = curso;
    }

    @Override public String getTipoUsuario() { return "Coordenador"; }
    @Override public String[] getPermissoes() { return new String[]{"VER_RELATORIOS_CURSO"}; }
    @Override public String gerarRelatorioPersonalizado() { return "Relatório do curso de " + this.curso.getNome(); }
    @Override public boolean podeGerenciarUsuarios() { return true; }
    @Override public boolean podeVerRelatoriosCompletos() { return true; }
    @Override public boolean podeExportarDados() { return true; }
    @Override public boolean podeEditarFrequencia() { return true; }

    public Curso getCurso() { return curso; } // Retorna objeto Curso
}