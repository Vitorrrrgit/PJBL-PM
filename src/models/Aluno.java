package models;

import java.util.ArrayList;
import java.util.List;

public class Aluno extends Usuario {
    private final String matricula;
    private final String curso;
    private final int semestre;
    private List<Frequencia> frequencias;

    public Aluno(int id, String nome, String email, String cpf, String matricula, String curso, int semestre) {
        super(id, nome, email, cpf);
        this.matricula = matricula;
        this.curso = curso;
        this.semestre = semestre;
        this.frequencias = new ArrayList<>();
    }

    @Override
    public String getTipoUsuario() {
        return "Aluno";
    }

    @Override
    public String[] getPermissoes() {
        return new String[]{"VISUALIZAR_FREQUENCIA", "GERAR_RELATORIO_PESSOAL"};
    }

    @Override
    public String gerarRelatorioPersonalizado() {
        return String.format("Relatório de Presença do Aluno %s (Mat: %s)", getNome(), this.matricula);
    }

    @Override
    public boolean podeGerenciarUsuarios() { return false; }

    @Override
    public boolean podeVerRelatoriosCompletos() { return false; }

    @Override
    public boolean podeExportarDados() { return false; }

    @Override
    public boolean podeEditarFrequencia() { return false; }

    // Getters e Setters
    public String getMatricula() { return matricula; }
    public String getCurso() { return curso; }
    public int getSemestre() { return semestre; }
    public List<Frequencia> getFrequencias() { return new ArrayList<>(frequencias); }
}