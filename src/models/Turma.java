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

    public Turma(String nomeDisciplina, int ano, int semestre, Professor professor) {
        this.nomeDisciplina = nomeDisciplina;
        this.ano = ano;
        this.semestre = semestre;
        this.professorResponsavel = professor;
        this.alunosMatriculados = new ArrayList<>();
    }

    public void adicionarAluno(Aluno aluno) {
        if (aluno != null && !this.alunosMatriculados.contains(aluno)) {
            this.alunosMatriculados.add(aluno);
        }
    }

    // Getters
    public String getNomeDisciplina() { return nomeDisciplina; }
    public Professor getProfessorResponsavel() { return professorResponsavel; }
    public List<Aluno> getAlunosMatriculados() { return new ArrayList<>(alunosMatriculados); }
    public String getDescricao() {
        return String.format("%s (%d.%d)", nomeDisciplina, ano, semestre);
    }

    @Override
    public String toString() {
        return getDescricao();
    }
}