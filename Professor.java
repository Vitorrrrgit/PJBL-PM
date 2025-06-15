public class Professor extends Usuario {

    private String disciplina;

    /**
     * Construtor padrão do professor.
     */
    public Professor(int id, String nome, String email, String telefone, String disciplina) {
        super(id, nome, email, telefone);
        this.disciplina = disciplina;
    }

    public String getDisciplina() { return disciplina; }

    /**
     * Registra a presença ou falta de um aluno.
     *
     * @param aluno   aluno a ser registrado
     * @param presente {@code true} para presença, {@code false} para falta
     */
    public void registrarPresenca(Aluno aluno, boolean presente) {
        if (presente) {
            aluno.registrarPresenca();
        } else {
            aluno.registrarFalta();
        }
    }

    @Override
    public void exibirMenu() {
        System.out.println("Menu do Professor");
        System.out.println("1 - Registrar presença");
        System.out.println("2 - Ver turmas");
        System.out.println("3 - Sair");
    }
}
