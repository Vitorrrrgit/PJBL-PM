import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // lista com usuarios do sistema
        ArrayList<Usuario> usuarios = new ArrayList<>();

        Usuario user1 = new Professor(
            0, 
            "professor", 
            "pedrao", 
            "pedrao@email.com", 
            "41999999999", 
            "java");
            
        usuarios.add(user1);

        for (Usuario U : usuarios) {
            System.out.println(U.getNome());
        }
    }
}