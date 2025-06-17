package models;

import java.util.ArrayList;
import java.util.List;


public class Coordenador extends Usuario {
    private String curso;
    private List<String> disciplinasGerenciadas = new ArrayList<>();

    public Coordenador() {
        super();
    }

    public Coordenador(int id, String nome, String email, String cpf, String curso) {
        super(id, nome, email, cpf);
        this.curso = curso;
    }

    // IMPLEMENTAÇÃO OBRIGATÓRIA DOS MÉTODOS ABSTRATOS
    @Override
    public String getTipoUsuario() {
        return "Coordenador";
    }

    @Override
    public String[] getPermissoes() {
        return new String[] { "GERENCIAR_DISCIPLINAS", "VER_RELATORIOS_CURSO" };
    }

    @Override
    public String gerarRelatorioPersonalizado() {
        return "Relatório do curso de " + this.curso;
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
        return false; 
    }

    @Override
    public boolean podeVerRelatoriosCompletos() {
        return true;
    }

    // Getters e Setters
    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<String> getDisciplinasGerenciadas() {
        return disciplinasGerenciadas;
    }

    public void setDisciplinasGerenciadas(List<String> disciplinas) {
        this.disciplinasGerenciadas = disciplinas;
    }
}