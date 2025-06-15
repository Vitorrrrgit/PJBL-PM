import java.util.ArrayList;
import java.util.List;

public class Administrador extends Usuario {

    private List<Usuario> usuarios = new ArrayList<>();

    public Administrador(int id, String nome, String email, String telefone) {
        super(id, nome, email, telefone);
    }

    /**
     * Cadastra um novo usuário no sistema.
     */
    public void cadastrarUsuario(Usuario usuario) {
        usuarios.add(usuario);
        System.out.println("Usuário cadastrado: " + usuario.getNome());
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    @Override
    public void exibirMenu() {
        System.out.println("Menu do Administrador");
        System.out.println("1 - Gerenciar usuários");
        System.out.println("2 - Relatórios");
        System.out.println("3 - Sair");
    }
}
