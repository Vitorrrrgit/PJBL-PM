public class Administrador extends Usuario {

    public Administrador(int id, String nome, String email, String telefone) {
        super(id, nome, email, telefone);
    }

    public void cadastrarUsuario(Usuario usuario) {
        System.out.println("Usuário cadastrado: " + usuario.getNome());
    }

    @Override
    public void exibirMenu() {
        System.out.println("Menu do Administrador");
        System.out.println("1 - Gerenciar usuários");
        System.out.println("2 - Relatórios");
        System.out.println("3 - Sair");
    }
}
