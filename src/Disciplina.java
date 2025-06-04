package src;

public class Disciplina {
    private String nomeDisciplina;
    private String codigoDisciplina;
    private String professorResponsavel;

    public Disciplina(String nomeDisciplina, String codigoDisciplina, String professorResponsavel) {
        this.nomeDisciplina = nomeDisciplina;
        this.codigoDisciplina = codigoDisciplina;
        this.professorResponsavel = professorResponsavel;
    }
    public String getNomeDisciplina() {
        return nomeDisciplina;
    }
    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }
    public String getProfessorResponsavel() {
        return professorResponsavel;
    }
    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }
    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }
    public void setProfessorResponsavel(String professorResponsavel) {
        this.professorResponsavel = professorResponsavel;
    }
    @Override
    public String toString() {
        return "Disciplina{" +
                "nomeDisciplina='" + nomeDisciplina + '\'' +
                ", codigoDisciplina='" + codigoDisciplina + '\'' +
                ", professorResponsavel='" + professorResponsavel + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Disciplina)) return false;
        Disciplina that = (Disciplina) o;
        return nomeDisciplina.equals(that.nomeDisciplina) &&
                codigoDisciplina.equals(that.codigoDisciplina) &&
                professorResponsavel.equals(that.professorResponsavel);
    }
}
