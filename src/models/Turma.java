package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Turma implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String nomeDisciplina;
    private final Professor professorResponsavel;
    private final List<Aluno> alunosMatriculados;
    private final int ano;
    private final int semestre;
    private Curso curso; // ASSOCIAÇÃO COM CURSO

    // Construtor original
    public Turma(String nomeDisciplina, int ano, int semestre, Professor professor) {
        this.nomeDisciplina = nomeDisciplina;
        this.ano = ano;
        this.semestre = semestre;
        this.professorResponsavel = professor;
        this.alunosMatriculados = new ArrayList<>();
        this.curso = null; // Será definido depois
    }

    // Novo construtor que inclui o curso
    public Turma(String nomeDisciplina, int ano, int semestre, Professor professor, Curso curso) {
        this.nomeDisciplina = nomeDisciplina;
        this.ano = ano;
        this.semestre = semestre;
        this.professorResponsavel = professor;
        this.alunosMatriculados = new ArrayList<>();
        this.curso = curso;
    }

    public void adicionarAluno(Aluno aluno) {
        if (aluno != null && !this.alunosMatriculados.contains(aluno)) {
            this.alunosMatriculados.add(aluno);
            // Se o curso da turma ainda não foi definido, usa o curso do primeiro aluno
            if (this.curso == null && aluno.getCurso() != null) {
                this.curso = aluno.getCurso();
            }
        }
    }

    // Getters
    public String getNomeDisciplina() { return nomeDisciplina; }
    public Professor getProfessorResponsavel() { return professorResponsavel; }
    public List<Aluno> getAlunosMatriculados() { return new ArrayList<>(alunosMatriculados); }
    public Curso getCurso() { return curso; }
    public int getAno() { return ano; }
    public int getSemestre() { return semestre; }
    
    // Setter para curso
    public void setCurso(Curso curso) { this.curso = curso; }
    
    public String getDescricao() {
        return String.format("%s (%d.%d)", nomeDisciplina, ano, semestre);
    }

    @Override
    public String toString() {
        return getDescricao();
    }
}