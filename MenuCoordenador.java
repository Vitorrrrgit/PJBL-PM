

import java.io.Serializable;
import java.util.List;

public interface MenuCoordenador extends Serializable {
    // Métodos básicos de gestão
    boolean adicionarProfessor(Professor professor);
    boolean removerProfessor(int professorId);
    List<Professor> listarProfessores();
    
    // Métodos de atribuição
    boolean atribuirDisciplinaProfessor(int professorId, Disciplina disciplina);
    boolean removerDisciplinaProfessor(int professorId, int disciplinaId);
    
    // Relatórios
    String gerarRelatorioProfessores();
    String gerarRelatorioDisciplinas();
    
    // Validações
    boolean existeProfessor(int professorId);
    boolean professorPossuiDisciplina(int professorId, int disciplinaId);
    
    // Menu textual (para console/debug)
    default void exibirMenuCoordenador() {
        System.out.println("\n--- MENU COORDENADOR ---");
        System.out.println("1. Gerenciar Professores");
        System.out.println("2. Gerenciar Disciplinas");
        System.out.println("3. Gerar Relatórios");
        System.out.println("0. Sair");
    }
}