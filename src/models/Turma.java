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
    private String curso; // NOVO CAMPO

    public Turma(String nomeDisciplina, int ano, int semestre, Professor professor) {
        this.nomeDisciplina = nomeDisciplina;
        this.ano = ano;
        this.semestre = semestre;
        this.professorResponsavel = professor;
        this.alunosMatriculados = new ArrayList<>();
        // Tenta inferir o curso a partir do primeiro aluno adicionado ou deixa como padrão
        this.curso = "Indefinido"; 
    }

    public void adicionarAluno(Aluno aluno) {
        if (aluno != null && !this.alunosMatriculados.contains(aluno)) {
            this.alunosMatriculados.add(aluno);
            // Se o curso da turma ainda não foi definido, usa o curso do primeiro aluno
            if ("Indefinido".equals(this.curso) && aluno.getCurso() != null) {
                this.curso = aluno.getCurso();
            }
        }
    }

    // Getters
    public String getNomeDisciplina() { return nomeDisciplina; }
    public Professor getProfessorResponsavel() { return professorResponsavel; }
    public List<Aluno> getAlunosMatriculados() { return new ArrayList<>(alunosMatriculados); }
    public String getCurso() { return curso; } // GETTER PARA O NOVO CAMPO
    
    public String getDescricao() {
        return String.format("%s (%d.%d)", nomeDisciplina, ano, semestre);
    }

    @Override
    public String toString() {
        return getDescricao();
    }
}