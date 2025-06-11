public class Coordenador extends Usuario {

    private String area;

    public Coordenador(int id, String nome, String email, String telefone, String area) {
        super(id, nome, email, telefone);
        this.area = area;
    }

    public String getArea() { return area; }

    @Override
    public void exibirMenu() {
        System.out.println("Menu do Coordenador");
        System.out.println("1 - Atribuir professor a turma");
        System.out.println("2 - Ver relatórios");
        System.out.println("3 - Sair");
    }
}
