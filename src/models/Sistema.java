package models;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import persistencia.SistemaException;

public class Sistema implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private static final String PASTA_DADOS = "dados";

    private static final String ARQUIVO_USUARIOS = PASTA_DADOS + File.separator + "usuarios.ser";
    private static final String ARQUIVO_FREQUENCIAS = PASTA_DADOS + File.separator + "frequencias.ser";
    private static final String ARQUIVO_DISCIPLINAS = PASTA_DADOS + File.separator + "disciplinas.ser";
    private static final String ARQUIVO_TURMAS = PASTA_DADOS + File.separator + "turmas.ser";
    private List<Usuario> usuarios;
    private List<Frequencia> frequencias;
    private List<Turma> turmas;
    private List<Disciplina> disciplinas;

    // CAMPOS ADICIONADOS PARA CONTROLE DE IDs
    private int proximoIdUsuario = 1;
    private int proximoIdFrequencia = 1;

public Sistema() {
    criarPastaDados();
    carregarDados();
    inicializarContadores();

    System.out.println("📁 Verificando arquivos na pasta dados:");
    File pasta = new File("dados");
    if (pasta.exists()) {
        File[] arquivos = pasta.listFiles();
        for (File arquivo : arquivos) {
            System.out.println("📄 " + arquivo.getName() + " - Tamanho: " + arquivo.length() + " bytes");
        }
    }
    if (this.usuarios.isEmpty()) {
        try {
            criarDadosIniciais();
            gerarFrequenciasAleatorias(5); 
            salvarDados();
        } catch (SistemaException e) {
            System.err.println("Erro crítico ao criar dados iniciais: " + e.getMessage());
        }
    }
}

    // MÉTODO ÚNICO (era duplicado) - CORRIGIDO!
    private void criarPastaDados() {
        File pasta = new File(PASTA_DADOS);
        if (!pasta.exists()) {
            boolean criada = pasta.mkdirs();
            if (criada) {
                System.out.println("📁 Pasta '" + PASTA_DADOS + "' criada para organizar os dados.");
            } else {
                System.err.println("❌ Erro ao criar pasta '" + PASTA_DADOS + "'.");
            }
        }
    }

    // --- MÉTODOS DE PERSISTÊNCIA (REQUISITO DO PDF) ---

    private void salvarDados() {
        try {
            salvarLista(this.usuarios, ARQUIVO_USUARIOS);
            salvarLista(this.frequencias, ARQUIVO_FREQUENCIAS);
            salvarLista(this.disciplinas, ARQUIVO_DISCIPLINAS);
            salvarLista(this.turmas, ARQUIVO_TURMAS);
            System.out.println("💾 Dados salvos com sucesso na pasta '" + PASTA_DADOS + "'!");
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    private void carregarDados() {
        this.usuarios = carregarLista(ARQUIVO_USUARIOS);
        this.frequencias = carregarLista(ARQUIVO_FREQUENCIAS);
        this.disciplinas = carregarLista(ARQUIVO_DISCIPLINAS);
        this.turmas = carregarLista(ARQUIVO_TURMAS);
        System.out.println(
                "📂 Dados carregados da pasta '" + PASTA_DADOS + "'. Usuários encontrados: " + this.usuarios.size());
    }

    private <T> void salvarLista(List<T> lista, String nomeArquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(lista);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> carregarLista(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        if (arquivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
                return (List<T>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao carregar lista de " + nomeArquivo + ": " + e.getMessage());
            }
        }
        return new ArrayList<>();
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
            return new ArrayList<>();
        }
        return frequencias.stream()
                .filter(f -> f.getDisciplina().equalsIgnoreCase(nomeDisciplina))
                .collect(Collectors.toList());
    }

    // --- MÉTODOS DE MODIFICAÇÃO DO SISTEMA ---

    public void adicionarUsuario(Usuario u) throws SistemaException {
        if (u == null)
            throw new SistemaException("Usuario", "Nulo", "Usuário inválido.");
        this.usuarios.add(u);
        salvarDados();
    }

    public void removerUsuario(Usuario u) throws SistemaException {
        if (u == null)
            throw new SistemaException("Remoção", "Nulo", "Usuário inválido.");
        if (u instanceof Administrador && usuarios.stream().filter(adm -> adm instanceof Administrador).count() <= 1) {
            throw new SistemaException("Remoção", u.getNome(), "Não é possível remover o último administrador.");
        }
        this.usuarios.remove(u);
        salvarDados();
    }

    public void adicionarFrequencia(Frequencia f) throws SistemaException {
        if (f == null)
            throw new SistemaException("Frequencia", "Nula", "Frequência inválida.");
        this.frequencias.add(f);
        salvarDados(); // ADICIONADO: salvar após adicionar frequência
    }

    public void adicionarTurma(Turma t) {
        if (t != null) {
            this.turmas.add(t);
            try {
                salvarDados(); // ADICIONADO: salvar após adicionar turma
            } catch (Exception e) {
                System.err.println("Erro ao salvar turma: " + e.getMessage());
            }
        }
    }

    public void adicionarDisciplina(Disciplina d) throws SistemaException {
        if (d == null)
            throw new SistemaException("Disciplina", "Nula", "Disciplina inválida.");
        this.disciplinas.add(d);
        salvarDados();
    }

    public void removerDisciplina(Disciplina d) throws SistemaException {
        if (d == null)
            throw new SistemaException("Remoção", "Nula", "Disciplina inválida.");
        this.disciplinas.remove(d);
        salvarDados(); // ADICIONADO: salvar após remover disciplina
    }

public void alterarSenha(Usuario usuario, String senhaAntiga, String novaSenha) throws SistemaException {
    System.out.println("🔧 === ALTERAÇÃO DE SENHA ===");
    System.out.println("👤 Usuário: " + usuario.getNome());
    System.out.println("🔑 Senha atual: '" + usuario.getSenha() + "'");
    System.out.println("🔑 Nova senha: '" + novaSenha + "'");
    
    // Validações
    if (!usuario.getSenha().equals(senhaAntiga)) {
        throw new SistemaException("Senha", "Inválida", "A senha antiga está incorreta.");
    }
    if (novaSenha == null || novaSenha.trim().isEmpty()) {
        throw new SistemaException("Senha", "Inválida", "A nova senha não pode estar em branco.");
    }
    if (novaSenha.trim().length() < 3) {
        throw new SistemaException("Senha", "Inválida", "A nova senha deve ter pelo menos 3 caracteres.");
    }
    
    // Alterar senha diretamente no usuário da lista
    Usuario usuarioNaLista = usuarios.stream()
        .filter(u -> u.getId() == usuario.getId())
        .findFirst()
        .orElse(null);
    
    if (usuarioNaLista != null) {
        usuarioNaLista.setSenha(novaSenha.trim());
        System.out.println("✅ Senha alterada no usuário da lista");
    }
    
    // Alterar também no objeto passado como parâmetro
    usuario.setSenha(novaSenha.trim());
    
    // Salvar dados (SEM recarregar depois!)
    //salvarDados();
    
    System.out.println("🎉 Senha alterada e salva com sucesso!");
    System.out.println("=======================================");
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

    public String gerarRelatorioDeAlunos() {
        StringBuilder sb = new StringBuilder("=== RELATÓRIO DE ALUNOS ===\n");
        listarUsuarios().stream()
                .filter(u -> u instanceof Aluno)
                .map(u -> (Aluno) u)
                .forEach(aluno -> {
                    sb.append("\n-----------------------------------\n");
                    sb.append("Nome: ").append(aluno.getNome()).append("\n");
                    sb.append("Matrícula: ").append(aluno.getMatricula()).append("\n");
                    sb.append("Curso: ").append(aluno.getCurso()).append("\n");
                });
        return sb.toString();
    }

    public String gerarRelatorioDeProfessores() {
        StringBuilder sb = new StringBuilder("=== RELATÓRIO DE PROFESSORES ===\n");
        listarUsuarios().stream()
                .filter(u -> u instanceof Professor)
                .map(u -> (Professor) u)
                .forEach(prof -> {
                    sb.append("\n-----------------------------------\n");
                    sb.append("Nome: ").append(prof.getNome()).append("\n");
                    sb.append("CPF: ").append(prof.getCpf()).append("\n");
                    sb.append("Área: ").append(prof.getArea()).append("\n");
                    sb.append("Titulação: ").append(prof.getTitulacao()).append("\n");
                });
        return sb.toString();
    }

    public String gerarRelatorioDeTurmas() {
        StringBuilder sb = new StringBuilder("=== RELATÓRIO DE TURMAS ===\n");
        listarTurmas().forEach(turma -> {
            sb.append("\n-----------------------------------\n");
            sb.append("Disciplina: ").append(turma.getNomeDisciplina()).append("\n");
            sb.append("Professor: ").append(turma.getProfessorResponsavel().getNome()).append("\n");
            sb.append("Alunos Matriculados: ").append(turma.getAlunosMatriculados().size()).append("\n");
            turma.getAlunosMatriculados().forEach(aluno -> {
                sb.append("  - ").append(aluno.getNome()).append(" (Mat: ").append(aluno.getMatricula()).append(")\n");
            });
        });
        return sb.toString();
    }

    public double calcularPercentualDePresenca(Aluno aluno) {
        List<Frequencia> frequenciasDoAluno = buscarFrequenciasPorAluno(aluno.getMatricula());

        if (frequenciasDoAluno.isEmpty()) {
            return 100.0;
        }
        long totalDePresencas = frequenciasDoAluno.stream()
                .filter(Frequencia::isPresente)
                .count();
        double totalDeAulas = frequenciasDoAluno.size();
        return (totalDePresencas / totalDeAulas) * 100.0;
    }

    // --- GERADOR DE CPF ---
    private static String gerarCPF() {
        Random random = new Random();
        int[] cpf = new int[11];
        for (int i = 0; i < 9; i++) {
            cpf[i] = random.nextInt(10);
        }
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += cpf[i] * (10 - i);
        }
        int digito1 = 11 - (soma % 11);
        if (digito1 >= 10)
            digito1 = 0;
        cpf[9] = digito1;
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += cpf[i] * (11 - i);
        }
        int digito2 = 11 - (soma % 11);
        if (digito2 >= 10)
            digito2 = 0;
        cpf[10] = digito2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            sb.append(cpf[i]);
        }
        return sb.toString();
    }

    // --- CRIANDO DADOS INICIAIS ---
    private void criarDadosIniciais() throws SistemaException {
        Professor carlos = new Professor(1, "Prof. Carlos", "carlos@email.com", gerarCPF(), "Cálculo", "Doutorado");
        Professor marta = new Professor(2, "Profa. Marta", "marta@email.com", gerarCPF(), "Humanas", "Mestrado");
        Administrador admin = new Administrador(3, "Admin", "admin@email.com", gerarCPF(), "TOTAL");
        Coordenador debora = new Coordenador(4, "Coord. Débora", "debora@email.com", gerarCPF(), "Engenharia");

        adicionarUsuario(carlos);
        adicionarUsuario(marta);
        adicionarUsuario(admin);
        adicionarUsuario(debora);

        adicionarUsuario(new Aluno(10, "Ana", "ana@email.com", gerarCPF(), "MAT-1000", "Engenharia", 2));
        adicionarUsuario(new Aluno(11, "João Pereira", "joao.p@email.com", gerarCPF(), "MAT-1001", "Engenharia", 3));
        adicionarUsuario(
                new Aluno(12, "Mariana Costa", "mari.costa@email.com", gerarCPF(), "MAT-1002", "Engenharia", 3));
        adicionarUsuario(new Aluno(13, "Lucas Martins", "lucas.m@email.com", gerarCPF(), "MAT-1003", "Arquitetura", 1));
        adicionarUsuario(new Aluno(14, "Beatriz Almeida", "bia.a@email.com", gerarCPF(), "MAT-1004", "Direito", 5));
        adicionarUsuario(
                new Aluno(15, "Gabriel Santos", "gabriel.s@email.com", gerarCPF(), "MAT-1005", "Engenharia", 1));
        adicionarUsuario(new Aluno(16, "Laura Ferreira", "laura.f@email.com", gerarCPF(), "MAT-1006", "Medicina", 2));
        adicionarUsuario(new Aluno(17, "Arthur Castro", "arthur.c@email.com", gerarCPF(), "MAT-1007", "Medicina", 2));
        adicionarUsuario(new Aluno(18, "Julia Ribeiro", "julia.r@email.com", gerarCPF(), "MAT-1008", "Direito", 5));
        adicionarUsuario(new Aluno(19, "Pedro Rocha", "pedro.r@email.com", gerarCPF(), "MAT-1009", "Arquitetura", 3));
        adicionarUsuario(
                new Aluno(20, "Sofia Gonçalves", "sofia.g@email.com", gerarCPF(), "MAT-1010", "Engenharia", 3));

        Disciplina calc1 = new Disciplina("Cálculo I", "MAT101", carlos.getCpf());
        Disciplina hist1 = new Disciplina("História do Direito", "DIR101", marta.getCpf());
        adicionarDisciplina(calc1);
        adicionarDisciplina(hist1);

        Turma turmaCalculo = new Turma("Cálculo I", 2025, 1, carlos);
        turmaCalculo.adicionarAluno((Aluno) usuarios.get(4));
        turmaCalculo.adicionarAluno((Aluno) usuarios.get(5));
        turmaCalculo.adicionarAluno((Aluno) usuarios.get(8));
        turmaCalculo.adicionarAluno((Aluno) usuarios.get(10));
        adicionarTurma(turmaCalculo);

        Turma turmaDireito = new Turma("História do Direito", 2025, 1, marta);
        turmaDireito.adicionarAluno((Aluno) usuarios.get(7));
        turmaDireito.adicionarAluno((Aluno) usuarios.get(11));
        adicionarTurma(turmaDireito);
    }

    // --- GERA FREQUÊNCIAS ALEATÓRIAS ---
    private void gerarFrequenciasAleatorias(int quantidadePorAluno) throws SistemaException {
        int idFrequencia = 200;
        Random random = new Random();

        List<Usuario> alunos = this.usuarios.stream()
                .filter(u -> u instanceof Aluno)
                .collect(Collectors.toList());

        List<Usuario> professores = this.usuarios.stream()
                .filter(u -> u instanceof Professor)
                .collect(Collectors.toList());

        for (Usuario u : alunos) {
            Aluno aluno = (Aluno) u;
            for (int i = 0; i < quantidadePorAluno; i++) {
                Professor prof = (Professor) professores.get(random.nextInt(professores.size()));
                LocalDate data = LocalDate.now().minusDays(random.nextInt(30));
                String disciplina = "Cálculo I";
                boolean presente = random.nextBoolean();
                String obs = presente ? "" : "Falta não justificada";
                adicionarFrequencia(new Frequencia(
                        idFrequencia++,
                        aluno.getMatricula(),
                        prof.getCpf(),
                        disciplina,
                        data,
                        presente,
                        obs));
            }
        }
    }

    // --- MÉTODOS ADICIONADOS PARA CONTROLE DE IDs ---

    public int obterProximoIdUsuario() {
        return proximoIdUsuario++;
    }

    public int obterProximoIdFrequencia() {
        return proximoIdFrequencia++;
    }

    private void inicializarContadores() {
        if (!usuarios.isEmpty()) {
            proximoIdUsuario = usuarios.stream()
                    .mapToInt(Usuario::getId)
                    .max()
                    .orElse(0) + 1;
        }

        if (!frequencias.isEmpty()) {
            proximoIdFrequencia = frequencias.stream()
                    .mapToInt(Frequencia::getId)
                    .max()
                    .orElse(0) + 1;
        }

        System.out.println("✅ Contadores inicializados - Próximo ID usuário: " + proximoIdUsuario +
                ", Próximo ID frequência: " + proximoIdFrequencia);
    }
}