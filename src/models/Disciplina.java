package models;

import java.io.Serializable;
import java.util.Objects;

public class Disciplina implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nomeDisciplina;
    private String codigoDisciplina;
    private String professorResponsavel; // CPF do professor
    private Curso curso; // ASSOCIAÇÃO COM O CURSO

    public Disciplina(String nomeDisciplina, String codigoDisciplina, String professorResponsavel, Curso curso) {
        this.nomeDisciplina = nomeDisciplina;
        this.codigoDisciplina = codigoDisciplina;
        this.professorResponsavel = professorResponsavel;
        this.curso = curso;
    }
    
    // Getters e Setters
    public String getNomeDisciplina() { return nomeDisciplina; }
    public void setNomeDisciplina(String nome) { this.nomeDisciplina = nome; }
    public String getCodigoDisciplina() { return codigoDisciplina; }
    public void setCodigoDisciplina(String codigo) { this.codigoDisciplina = codigo; }
    public String getProfessorResponsavel() { return professorResponsavel; }
    public void setProfessorResponsavel(String cpf) { this.professorResponsavel = cpf; }
    public Curso getCurso() { return curso; }
    public void setCurso(Curso curso) { this.curso = curso; }

    @Override
    public String toString() {
        return nomeDisciplina + " (" + codigoDisciplina + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disciplina that = (Disciplina) o;
        return Objects.equals(codigoDisciplina, that.codigoDisciplina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoDisciplina);
    }
}