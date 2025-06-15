public class Main {
    public static void main(String[] args) {
        Aluno aluno = new Aluno(1, "Vitor", "vitor@email.com", "41999999991", "a1234");
        Professor professor = new Professor(2, "Pedro", "pedro@email.com", "41999999992", "Java");
        Administrador admin = new Administrador(3, "Admin", "admin@email.com", "41999999993");

        // Registrar algumas presenças e faltas
        professor.registrarPresenca(aluno, true);
        professor.registrarPresenca(aluno, false);

        System.out.printf("Aluno %s - Presenças: %d, Faltas: %d, %.2f%% de presença\n",
                aluno.getNome(), aluno.getPresencas(), aluno.getFaltas(), aluno.getPercentualPresenca());

        // Administrador cadastra usuários
        admin.cadastrarUsuario(aluno);
        admin.cadastrarUsuario(professor);

        for (Usuario u : admin.getUsuarios()) {
            System.out.println("Usuário: " + u.getNome());
            u.exibirMenu();
            System.out.println();
        }
    }
}
