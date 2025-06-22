package models;

import java.util.ArrayList;
import java.util.List;

public class Professor extends Usuario {
    private static final long serialVersionUID = 1L;

    private final String area;
    private final String titulacao;
    private final List<String> disciplinas;

    public Professor(int id, String nome, String email, String cpf, String area, String titulacao) {
        super(id, nome, email, cpf);
        this.area = area;
        this.titulacao = titulacao;
        this.disciplinas = new ArrayList<>();
    }

    // Construtor vazio para serialização
    public Professor() {
        super();
        this.area = "";
        this.titulacao = "";
        this.disciplinas = new ArrayList<>();
    }

    // ======= IMPLEMENTAÇÃO DOS MÉTODOS ABSTRATOS =======
    @Override
    public String getTipoUsuario() { 
        return "Professor"; 
    }

    @Override
    public String[] getPermissoes() { 
        return new String[] { "EDITAR_FREQUENCIA", "VISUALIZAR_ALUNOS" }; 
    }

    @Override
    public String gerarRelatorioPersonalizado() { 
        return "Relatório de Aulas do Professor " + getNome() + " (Área: " + area + ")"; 
    }

    @Override
    public boolean podeGerenciarUsuarios() { 
        return false; 
    }

    @Override
    public boolean podeVerRelatoriosCompletos() { 
        return false; 
    }

    @Override
    public boolean podeExportarDados() { 
        return false; 
    }

    @Override
    public boolean podeEditarFrequencia() { 
        return true; 
    }

    // ======= GETTERS E SETTERS =======
    public String getArea() { 
        return area; 
    }

    public String getTitulacao() { 
        return titulacao; 
    }

    public List<String> getDisciplinas() {
        return new ArrayList<>(disciplinas);
    }

    public void setDisciplinas(List<String> disciplinas) {
        this.disciplinas.clear();
        if (disciplinas != null) {
            this.disciplinas.addAll(disciplinas);
        }
    }

    public void adicionarDisciplina(String disciplina) {
        if (disciplina != null && !disciplinas.contains(disciplina)) {
            disciplinas.add(disciplina);
        }
    }

    public void removerDisciplina(String disciplina) {
        disciplinas.remove(disciplina);
    }

    // ======= MÉTODOS AUXILIARES =======
    /**
     * Método auxiliar para obter ID numérico baseado no CPF
     */
    public int getProfessorId() {
        try {
            return Integer.parseInt(getCpf().replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return getId();
        }
    }

    public boolean leciona(String disciplina) {
        return disciplinas.contains(disciplina);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", getNome(), area);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Professor professor = (Professor) obj;
        return getCpf().equals(professor.getCpf());
    }

    @Override
    public int hashCode() {
        return getCpf().hashCode();
    }
}