package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Subclasse de Usuario que representa um professor.
 * VERSÃO CORRIGIDA FINAL
 */
public class Professor extends Usuario {
    private String area;
    private String titulacao;
    private List<String> disciplinas = new ArrayList<>();

    public Professor() {
        super();
    }

    public Professor(int id, String nome, String email, String cpf, String area, String titulacao) {
        super(id, nome, email, cpf);
        this.area = area;
        this.titulacao = titulacao;
    }

    // IMPLEMENTAÇÃO OBRIGATÓRIA DOS MÉTODOS ABSTRATOS
    @Override
    public String getTipoUsuario() {
        return "Professor";
    }

    @Override
    public String[] getPermissoes() {
        return new String[] { "EDITAR_FREQUENCIA", "CONSULTAR_ALUNOS", "GERAR_RELATORIOS" };
    }

    @Override
    public String gerarRelatorioPersonalizado() {
        return String.format("Relatório de Aulas do Professor %s (Área: %s)", getNome(), getArea());
    }

    @Override
    public boolean podeEditarFrequencia() {
        return true;
    }

    @Override
    public boolean podeGerenciarUsuarios() {
        return false;
    }

    @Override
    public boolean podeExportarDados() {
        return false; // CORRIGIDO: Professor não faz exportação de dados gerais.
    }

    @Override
    public boolean podeVerRelatoriosCompletos() {
        return false;
    }

    // Getters e Setters
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTitulacao() {
        return titulacao;
    }

    public void setTitulacao(String titulacao) {
        this.titulacao = titulacao;
    }

    public List<String> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<String> disciplinas) {
        this.disciplinas = disciplinas;
    }
}