package models;

import java.util.ArrayList;
import java.util.List;


public class Aluno extends Usuario {
    private String matricula;
    private String curso;
    private int semestre;
    private List<Frequencia> frequencias;

    public Aluno(int par, String ana_Silva, String anaemailcom, String string, String par1) {
        super();
        this.frequencias = new ArrayList<>();
    }

    public Aluno(int id, String nome, String email, String cpf, String matricula, String curso, int semestre) {
        super(id, nome, email, cpf);
        this.matricula = matricula;
        this.curso = curso;
        this.semestre = semestre;
        this.frequencias = new ArrayList<>();
    }

    //  MÉTODOS ABSTRATOS
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
    public boolean podeEditarFrequencia() {
        return false;
    }

    @Override
    public boolean podeGerenciarUsuarios() {
        return false;
    }

    @Override
    public boolean podeExportarDados() {
        return false;
    }

    @Override
    public boolean podeVerRelatoriosCompletos() {
        return false;
    }

    // Getters e Setters
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
    public List<Frequencia> getFrequencias() { return new ArrayList<>(frequencias); }
    public void setFrequencias(List<Frequencia> frequencias) { this.frequencias = frequencias; }
}