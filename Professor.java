public class Professor extends Usuario {

    private String disciplina;

    public Professor(int id, String nome, String email, String telefone, String disciplina) {
        super(id, nome, email, telefone);
        this.disciplina = disciplina;
    }

    public String getDisciplina() { return disciplina; }

    @Override
    public void exibirMenu() {
        System.out.println("Menu do Professor");
        System.out.println("1 - Registrar presença");
        System.out.println("2 - Ver turmas");
        System.out.println("3 - Sair");
    }
}
