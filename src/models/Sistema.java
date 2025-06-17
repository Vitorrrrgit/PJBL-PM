package models;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import persistencia.SistemaException;

public class Sistema implements Serializable {
    private static final long serialVersionUID = 1L;


    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Frequencia> frequencias = new ArrayList<>();
    private final List<Turma> turmas = new ArrayList<>();
    private final List<Disciplina> disciplinas = new ArrayList<>();

    public Sistema() {
        try {
            criarDadosIniciais();
        } catch (SistemaException e) {
            System.err.println("Erro crítico ao criar dados iniciais: " + e.getMessage());
        }
    }


    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(this.usuarios);
    }

    public List<Frequencia> listarFrequencias() {
        return new ArrayList<>(this.frequencias);
    }

    public List<Disciplina> listarDisciplinas() {
        return new ArrayList<>(this.disciplinas);
    }

    public List<Turma> listarTurmas() {
        return new ArrayList<>(this.turmas);
    }

    public Usuario buscarUsuarioPorEmail(String email) throws SistemaException {
        return usuarios.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new SistemaException("Login", email, "Usuário não encontrado."));
    }

    public List<Frequencia> buscarFrequenciasPorAluno(String matricula) {
        return frequencias.stream()
                .filter(f -> f.getAlunoMatricula().equals(matricula))
                .collect(Collectors.toList());
    }

    public List<Turma> buscarTurmasPorProfessor(Professor professor) {
        return turmas.stream()
                .filter(t -> t.getProfessorResponsavel().equals(professor))
                .collect(Collectors.toList());
    }

    public List<Frequencia> buscarFrequenciasPorDisciplina(String nomeDisciplina) {
        if (nomeDisciplina == null || nomeDisciplina.isEmpty()) {
            return new ArrayList<>(); // Retorna lista vazia se não houver disciplina
        }
        return frequencias.stream()
                .filter(f -> f.getDisciplina().equalsIgnoreCase(nomeDisciplina))
                .collect(Collectors.toList());
    }

    // --- MÉTODOS DE MODIFICAÇÃO DO SISTEMA    ---

    public void adicionarUsuario(Usuario u) throws SistemaException {
        if (u == null)
            throw new SistemaException("Usuario", "Nulo", "Usuário inválido.");
        this.usuarios.add(u);
    }

    public void removerUsuario(Usuario u) throws SistemaException {
        if (u == null)
            throw new SistemaException("Remoção", "Nulo", "Usuário inválido.");
        if (u instanceof Administrador && usuarios.stream().filter(adm -> adm instanceof Administrador).count() <= 1) {
            throw new SistemaException("Remoção", u.getNome(), "Não é possível remover o último administrador.");
        }
        this.usuarios.remove(u);
    }

    public void adicionarFrequencia(Frequencia f) throws SistemaException {
        if (f == null)
            throw new SistemaException("Frequencia", "Nula", "Frequência inválida.");
        this.frequencias.add(f);
    }

    public void adicionarTurma(Turma t) {
        if (t != null)
            this.turmas.add(t);
    }

    public void adicionarDisciplina(Disciplina d) throws SistemaException {
        if (d == null)
            throw new SistemaException("Disciplina", "Nula", "Disciplina inválida.");
        this.disciplinas.add(d);
    }

    public void removerDisciplina(Disciplina d) throws SistemaException {
        if (d == null)
            throw new SistemaException("Remoção", "Nula", "Disciplina inválida.");
        this.disciplinas.remove(d);
    }

    public void alterarSenha(Usuario usuario, String senhaAntiga, String novaSenha) throws SistemaException {
        if (!usuario.getSenha().equals(senhaAntiga)) {
            throw new SistemaException("Senha", "Inválida", "A senha antiga está incorreta.");
        }
        if (novaSenha == null || novaSenha.trim().isEmpty()) {
            throw new SistemaException("Senha", "Inválida", "A nova senha não pode estar em branco.");
        }
        usuario.setSenha(novaSenha.trim());
    }

    // --- MÉTODOS DE EXPORTAÇÃO E RELATÓRIOS ---

    public void exportarUsuariosCSV(String caminhoArquivo) throws java.io.IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(caminhoArquivo))) {
            pw.println("ID;TIPO;NOME;EMAIL;CPF");
            for (Usuario u : usuarios) {
                pw.printf("%d;%s;%s;%s;%s%n", u.getId(), u.getTipoUsuario(), u.getNome(), u.getEmail(), u.getCpf());
            }
        }
    }

    public String gerarRelatorioTodasFrequencias() {
        if (frequencias.isEmpty())
            return "Nenhuma frequência registrada.";
        StringBuilder sb = new StringBuilder("=== RELATÓRIO GERAL DE FREQUÊNCIAS ===\n\n");
        frequencias.forEach(f -> sb.append(f.toString()).append("\n"));
        return sb.toString();
    }


private void criarDadosIniciais() throws SistemaException {
    Random random = new Random();

    // --- Usuários Criados pra tesrar ---

    // Aluno com matrícula e CPF aleatórios
    String matriculaAna = "MAT-" + (1000 + random.nextInt(9000)); 
    String cpfAna = String.valueOf(10000000000L + random.nextLong(90000000000L));
    Aluno ana = new Aluno(1, "Ana Silva", "ana@email.com", cpfAna, matriculaAna, "Engenharia", 2);

    // Professor com CPF aleatório
    String cpfCarlos = String.valueOf(20000000000L + random.nextLong(90000000000L));
    Professor carlos = new Professor(2, "Prof. Carlos", "carlos@email.com", cpfCarlos, "Cálculo", "Doutorado");
    
    // Admin com CPF aleatório
    String cpfAdmin = String.valueOf(30000000000L + random.nextLong(90000000000L));
    Administrador admin = new Administrador(3, "Admin", "admin@email.com", cpfAdmin, "TOTAL");

    // Coordenador com CPF aleatório
    String cpfDebora = String.valueOf(40000000000L + random.nextLong(90000000000L));
    Coordenador debora = new Coordenador(4, "Coord. Débora", "debora@email.com", cpfDebora, "Engenharia");
    
    adicionarUsuario(ana);
    adicionarUsuario(carlos);
    adicionarUsuario(admin);
    adicionarUsuario(debora);
    
    // --- Disciplinas e Turmas ---

    Disciplina calc1 = new Disciplina("Cálculo I", "MAT101", carlos.getCpf());
    adicionarDisciplina(calc1);

    Turma turmaCalculo = new Turma("Cálculo I", 2025, 1, carlos);
    turmaCalculo.adicionarAluno(ana);
    adicionarTurma(turmaCalculo);

    // --- Frequências de Exemplo ---
    adicionarFrequencia(new Frequencia(101, matriculaAna, carlos.getCpf(), "Cálculo I", LocalDate.now().minusDays(1), true, ""));
}
}

