package src;

import java.util.ArrayList;
import java.util.List;

public class Professor extends Usuario {
    private static final long serialVersionUID = 1L;

    private List<String> disciplinas;

    public Professor() {
        super();
        this.disciplinas = new ArrayList<>();
    }


    public Professor(int id, String nome, String email, String cpf, String area, String titulacao) {
        super(id, nome, email, cpf,);
        this.disciplinas = new ArrayList<>();
    }

    @Override
    public String getTipoUsuario() {
        return "Professor";
    }

    @Override
    public String[] getPermissoes() {
        return new String[] { "VALIDAR_FREQUENCIA", "VISUALIZAR_ALUNOS" };
    }

    @Override
    public boolean podeEditarFrequencia() {
        // Professor deve poder lançar ou editar frequência dos alunos
        return true;
    }

    @Override
    public boolean podeGerenciarUsuarios() {
        // Professor não pode gerenciar todos os usuários
        return false;
    }

    @Override
    public String gerarRelatorioPersonalizado() {
        // retorna um relatorio para o usuario professor
        return String.format("Relatório de Aulas do Professor %s (Área: %s)", nome, area);
    }

    public List<String> getDisciplinas() {
        return new ArrayList<>(disciplinas);
    }
    public void setDisciplinas(List<String> disciplinas) {
        this.disciplinas = (disciplinas != null ? disciplinas : new ArrayList<>());
    }

    public int getProfessorId() {
        try {
            return Integer.parseInt(cpf.replaceAll("\\D+", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
