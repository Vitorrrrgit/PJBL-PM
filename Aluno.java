public class Aluno extends Usuario {

    private String matricula;
    private int    presencas;
    private int    faltas;

    public Aluno(int id, String nome, String email, String telefone, String matricula) {
        super(id, nome, email, telefone);
        this.matricula = matricula;
        this.presencas = 0;
        this.faltas     = 0;
    }

    public String getMatricula() { return matricula; }
    public int getPresencas() { return presencas; }
    public int getFaltas() { return faltas; }

    /**
     * Registra uma presença para o aluno.
     */
    public void registrarPresenca() {
        presencas++;
    }

    /**
     * Registra uma falta para o aluno.
     */
    public void registrarFalta() {
        faltas++;
    }

    /**
     * Calcula o percentual de presença do aluno.
     */
    public double getPercentualPresenca() {
        int total = presencas + faltas;
        if (total == 0) {
            return 0.0;
        }
        return (presencas * 100.0) / total;
    }

    @Override
    public void exibirMenu() {
        System.out.println("Menu do Aluno");
        System.out.println("1 - Ver presenças");
        System.out.println("2 - Atualizar dados");
        System.out.println("3 - Sair");
    }
}
