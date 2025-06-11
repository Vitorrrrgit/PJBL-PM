public class Main {
    public static void main(String[] args) {
        Usuario[] usuarios = {
            new Aluno(1, "Vitor", "vitor@email.com", "41999999991", "a1234"),
            new Professor(2, "Pedro", "pedro@email.com", "41999999992", "Java"),
            new Coordenador(3, "Cleber", "cleber@email.com", "41999999993", "CS"),
            new Administrador(4, "Admin", "admin@email.com", "41999999994")
        };

        for (Usuario u : usuarios) {
            System.out.println("Usuário: " + u.getNome());
            u.exibirMenu();  
            System.out.println();
        }
    }
}
