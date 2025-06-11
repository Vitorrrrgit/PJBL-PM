public class Aluno extends Usuario {

    private String matricula;
    private int faltas;

    public Aluno(int id, String nome, String email, String telefone, String matricula) {
        super(id, nome, email, telefone);
        this.matricula = matricula;
        this.faltas = 0;
    }

    public String getMatricula() { return matricula; }
    public int getFaltas() { return faltas; }

    public void adicionarFalta() {
        faltas++;
    }

    @Override
    public void exibirMenu() {
        System.out.println("Menu do Aluno");
        System.out.println("1 - Ver presenças");
        System.out.println("2 - Atualizar dados");
        System.out.println("3 - Sair");
    }
}
