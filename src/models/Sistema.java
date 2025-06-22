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
    private static final String ARQUIVO_CURSOS = PASTA_DADOS + File.separator + "cursos.ser";
    private static final String ARQUIVO_DISCIPLINAS = PASTA_DADOS + File.separator + "disciplinas.ser";
    private static final String ARQUIVO_TURMAS = PASTA_DADOS + File.separator + "turmas.ser";
    
    private List<Usuario> usuarios;
    private List<Frequencia> frequencias;
    private List<Turma> turmas;
    private List<Curso> cursos;
    private List<Disciplina> disciplinas;

    public Sistema() {
        criarPastaDados();
        carregarDados();
        criarDadosIniciais();
    }

    // ===== PERSISTÊNCIA =====
    private void criarPastaDados() {
        File pasta = new File(PASTA_DADOS);
        if (!pasta.exists()) pasta.mkdirs();
    }

    public void salvarDados() {
        try {
            salvarLista(this.usuarios, ARQUIVO_USUARIOS);
            salvarLista(this.frequencias, ARQUIVO_FREQUENCIAS);
            salvarLista(this.cursos, ARQUIVO_CURSOS);
            salvarLista(this.disciplinas, ARQUIVO_DISCIPLINAS);
            salvarLista(this.turmas, ARQUIVO_TURMAS);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    private void carregarDados() {
        this.usuarios = carregarLista(ARQUIVO_USUARIOS);
        this.frequencias = carregarLista(ARQUIVO_FREQUENCIAS);
        this.cursos = carregarLista(ARQUIVO_CURSOS);
        this.disciplinas = carregarLista(ARQUIVO_DISCIPLINAS);
        this.turmas = carregarLista(ARQUIVO_TURMAS);
    }

    private <T> void salvarLista(List<T> lista, String nomeArquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(lista);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> carregarLista(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    // ===== USUÁRIOS =====
    public List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public Usuario buscarUsuarioPorEmail(String email) throws SistemaException {
        return usuarios.stream()
            .filter(u -> u.getEmail().equalsIgnoreCase(email))
            .findFirst()
            .orElseThrow(() -> new SistemaException("Login", email, "Usuário não encontrado"));
    }

    public void adicionarUsuario(Usuario usuario) throws SistemaException {
        boolean emailExiste = usuarios.stream()
            .anyMatch(u -> u.getEmail().equalsIgnoreCase(usuario.getEmail()));
        if (emailExiste) {
            throw SistemaException.emailJaCadastrado(usuario.getEmail());
        }
        usuarios.add(usuario);
        salvarDados();
    }

    public void removerUsuario(Usuario usuario) throws SistemaException {
        usuarios.remove(usuario);
        salvarDados();
    }

    public void alterarSenha(Usuario usuario, String senhaAntiga, String novaSenha) throws SistemaException {
        if (!usuario.getSenha().equals(senhaAntiga)) {
            throw new SistemaException("Usuario", usuario.getEmail(), "Senha atual incorreta");
        }
        if (novaSenha == null || novaSenha.trim().length() < 4) {
            throw new SistemaException("Usuario", usuario.getEmail(), "Nova senha deve ter pelo menos 4 caracteres");
        }
        usuario.setSenha(novaSenha);
        salvarDados();
    }

    // ===== FREQUÊNCIAS =====
    public List<Frequencia> listarFrequencias() {
        return new ArrayList<>(frequencias);
    }

    public List<Frequencia> buscarFrequenciasPorAluno(String matricula) {
        return frequencias.stream()
            .filter(f -> f.getAlunoMatricula().equals(matricula))
            .collect(Collectors.toList());
    }

    public List<Frequencia> buscarFrequenciasPorDisciplina(String disciplina) {
        return frequencias.stream()
            .filter(f -> f.getDisciplina().equalsIgnoreCase(disciplina))
            .collect(Collectors.toList());
    }

    public void adicionarFrequencia(Frequencia frequencia) {
        frequencias.add(frequencia);
        salvarDados();
    }

    public int obterProximoIdFrequencia() {
        return frequencias.stream().mapToInt(Frequencia::getId).max().orElse(0) + 1;
    }

    public double calcularPercentualDePresenca(Aluno aluno) {
        List<Frequencia> freqAluno = buscarFrequenciasPorAluno(aluno.getMatricula());
        if (freqAluno.isEmpty()) return 100.0; // Se não há registros, considera 100%
        
        long presencas = freqAluno.stream().mapToLong(f -> f.isPresente() ? 1 : 0).sum();
        double percentual = (double) presencas / freqAluno.size() * 100.0;
        return Math.round(percentual * 10.0) / 10.0; // Arredonda para 1 casa decimal
    }

    // ===== CURSOS =====
    public List<Curso> listarCursos() {
        return new ArrayList<>(cursos);
    }

    public void adicionarCurso(String nomeCurso) throws SistemaException {
        boolean existe = cursos.stream().anyMatch(c -> c.getNome().equalsIgnoreCase(nomeCurso));
        if (existe) {
            throw new SistemaException("Curso", nomeCurso, "Curso já existe");
        }
        int novoId = cursos.stream().mapToInt(Curso::getId).max().orElse(0) + 1;
        cursos.add(new Curso(novoId, nomeCurso));
        salvarDados();
    }

    public void removerCurso(Curso curso) throws SistemaException {
        boolean temUsuarios = usuarios.stream().anyMatch(u -> {
            if (u instanceof Aluno) return ((Aluno) u).getCurso().equals(curso);
            if (u instanceof Coordenador) return ((Coordenador) u).getCurso().equals(curso);
            return false;
        });
        
        if (temUsuarios) {
            throw new SistemaException("Curso", curso.getNome(), "Não é possível remover curso com usuários vinculados");
        }
        cursos.remove(curso);
        salvarDados();
    }

    // ===== DISCIPLINAS =====
    public List<Disciplina> listarDisciplinas() {
        return new ArrayList<>(disciplinas);
    }

    public void adicionarDisciplina(Disciplina disciplina) throws SistemaException {
        boolean existe = disciplinas.stream()
            .anyMatch(d -> d.getCodigoDisciplina().equals(disciplina.getCodigoDisciplina()));
        if (existe) {
            throw new SistemaException("Disciplina", disciplina.getCodigoDisciplina(), "Código já existe");
        }
        disciplinas.add(disciplina);
        salvarDados();
    }

    public void removerDisciplina(Disciplina disciplina) throws SistemaException {
        disciplinas.remove(disciplina);
        salvarDados();
    }

    // ===== TURMAS =====
    public List<Turma> listarTurmas() {
        return new ArrayList<>(turmas);
    }

    public List<Turma> buscarTurmasPorProfessor(Professor professor) {
        return turmas.stream()
            .filter(t -> t.getProfessorResponsavel().getCpf().equals(professor.getCpf()))
            .collect(Collectors.toList());
    }

    // CORREÇÃO: Método para buscar turmas por nome do curso (String)
    public List<Turma> buscarTurmasPorCurso(String nomeCurso) {
        return turmas.stream()
            .filter(t -> t.getCurso() != null && t.getCurso().getNome().equalsIgnoreCase(nomeCurso))
            .collect(Collectors.toList());
    }

    // Método para buscar turmas por objeto Curso
    public List<Turma> buscarTurmasPorCurso(Curso curso) {
        return turmas.stream()
            .filter(t -> t.getCurso() != null && t.getCurso().equals(curso))
            .collect(Collectors.toList());
    }

    public void adicionarTurma(Turma turma) throws SistemaException {
        turmas.add(turma);
        salvarDados();
    }

    public void removerTurma(Turma turma) throws SistemaException {
        turmas.remove(turma);
        salvarDados();
    }

    // ===== IMPORTAÇÃO E EXPORTAÇÃO CSV =====
    public void importarFrequenciasCSV(String caminhoArquivo) {
        try {
            persistencia.SerializadorJava serializador = new persistencia.SerializadorJava();
            int proximoId = obterProximoIdFrequencia();
            serializador.carregarFrequenciasDeCSV(caminhoArquivo, this.frequencias, proximoId);
            salvarDados();
            System.out.println("✅ Frequências importadas com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ Erro ao importar frequências: " + e.getMessage());
        }
    }

    public void exportarFrequenciasCSV(String caminhoArquivo) throws java.io.IOException {
        persistencia.SerializadorJava serializador = new persistencia.SerializadorJava();
        serializador.salvarFrequenciasCSV(caminhoArquivo, this.frequencias);
        System.out.println("✅ Frequências exportadas para: " + caminhoArquivo);
    }

    // ===== RELATÓRIOS =====
    public String gerarRelatorioGeral() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO GERAL DO SISTEMA ===\n\n");
        
        sb.append("👥 USUÁRIOS CADASTRADOS: ").append(usuarios.size()).append("\n");
        sb.append("- Alunos: ").append(usuarios.stream().filter(u -> u instanceof Aluno).count()).append("\n");
        sb.append("- Professores: ").append(usuarios.stream().filter(u -> u instanceof Professor).count()).append("\n");
        sb.append("- Coordenadores: ").append(usuarios.stream().filter(u -> u instanceof Coordenador).count()).append("\n");
        sb.append("- Administradores: ").append(usuarios.stream().filter(u -> u instanceof Administrador).count()).append("\n\n");
        
        sb.append("📚 TURMAS: ").append(turmas.size()).append("\n");
        sb.append("📊 FREQUÊNCIAS REGISTRADAS: ").append(frequencias.size()).append("\n\n");
        
        return sb.toString();
    }

    public String gerarRelatorioDeAlunos() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE ALUNOS ===\n\n");
        
        usuarios.stream()
            .filter(u -> u instanceof Aluno)
            .map(u -> (Aluno) u)
            .forEach(aluno -> {
                double presenca = calcularPercentualDePresenca(aluno);
                sb.append(String.format("Nome: %s\nMatrícula: %s\nCurso: %s\nPresença: %.1f%%\n\n", 
                    aluno.getNome(), aluno.getMatricula(), aluno.getCurso().getNome(), presenca));
            });
        
        return sb.toString();
    }

    public String gerarRelatorioDeProfessores() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE PROFESSORES ===\n\n");
        
        usuarios.stream()
            .filter(u -> u instanceof Professor)
            .map(u -> (Professor) u)
            .forEach(professor -> {
                List<Turma> turmasProf = buscarTurmasPorProfessor(professor);
                sb.append(String.format("Nome: %s\nÁrea: %s\nTurmas: %d\n\n", 
                    professor.getNome(), professor.getArea(), turmasProf.size()));
            });
        
        return sb.toString();
    }

    public String gerarRelatorioDeTurmas() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== RELATÓRIO DE TURMAS ===\n\n");
        
        turmas.forEach(turma -> {
            sb.append(String.format("Disciplina: %s\nProfessor: %s\nAlunos Matriculados: %d\nCurso: %s\n\n", 
                turma.getNomeDisciplina(), 
                turma.getProfessorResponsavel().getNome(),
                turma.getAlunosMatriculados().size(),
                turma.getCurso() != null ? turma.getCurso().getNome() : "N/A"));
        });
        
        return sb.toString();
    }

    // ===== MÉTODOS AUXILIARES =====
    public int obterProximoIdUsuario() {
        return usuarios.stream().mapToInt(Usuario::getId).max().orElse(0) + 1;
    }

    public String gerarCPF() {
        return String.format("%011d", (long)(Math.random() * 100000000000L));
    }

    // ===== DADOS INICIAIS =====
    private void criarDadosIniciais() {
        if (!usuarios.isEmpty() || !cursos.isEmpty()) return; // Já tem dados
        
        System.out.println("🔧 Criando dados iniciais...");
        
        try {
            // Criar cursos primeiro
            Curso cc = new Curso(1, "Ciência da Computação");
            Curso es = new Curso(2, "Engenharia de Software");
            Curso si = new Curso(3, "Sistemas de Informação");
            cursos.addAll(Arrays.asList(cc, es, si));
            
            // Criar usuários de teste
            Administrador admin = new Administrador(1, "Administrador", "admin@sistema.com", "00000000000", "TOTAL");
            Professor prof1 = new Professor(2, "Dr. Carlos Silva", "carlos@sistema.com", "11111111111", "Computação", "Doutor");
            Coordenador coord1 = new Coordenador(3, "Maria Santos", "maria@sistema.com", "22222222222", cc);
            Aluno aluno1 = new Aluno(4, "Ana Costa", "ana@sistema.com", "33333333333", "2024001", cc, 1);
            Aluno aluno2 = new Aluno(5, "Pedro Silva", "pedro@sistema.com", "44444444444", "2024002", cc, 1);
            
            usuarios.addAll(Arrays.asList(admin, prof1, coord1, aluno1, aluno2));
            
            // Criar disciplina
            Disciplina prog1 = new Disciplina("Programação I", "PROG001", prof1.getCpf(), cc);
            disciplinas.add(prog1);
            
            // Criar turma de exemplo
            Turma turma1 = new Turma("Programação I", 2025, 1, prof1, cc);
            turma1.adicionarAluno(aluno1);
            turma1.adicionarAluno(aluno2);
            turmas.add(turma1);
            
            // Criar algumas frequências de exemplo
            frequencias.add(new Frequencia(1, "2024001", "11111111111", "Programação I", LocalDate.now().minusDays(5), true, ""));
            frequencias.add(new Frequencia(2, "2024002", "11111111111", "Programação I", LocalDate.now().minusDays(5), false, "Faltou"));
            frequencias.add(new Frequencia(3, "2024001", "11111111111", "Programação I", LocalDate.now().minusDays(3), true, ""));
            frequencias.add(new Frequencia(4, "2024002", "11111111111", "Programação I", LocalDate.now().minusDays(3), true, ""));
            frequencias.add(new Frequencia(5, "2024001", "11111111111", "Programação I", LocalDate.now().minusDays(1), false, "Atestado médico"));
            frequencias.add(new Frequencia(6, "2024002", "11111111111", "Programação I", LocalDate.now().minusDays(1), true, ""));
            
            salvarDados();
            
            System.out.println("✅ Dados iniciais criados!");
            System.out.println("🔑 Credenciais:");
            System.out.println("   Admin: admin@sistema.com / " + admin.getSenha());
            System.out.println("   Professor: carlos@sistema.com / " + prof1.getSenha());
            System.out.println("   Coordenador: maria@sistema.com / " + coord1.getSenha());
            System.out.println("   Aluno: ana@sistema.com / " + aluno1.getSenha());
            System.out.println("   Aluno 2: pedro@sistema.com / " + aluno2.getSenha());
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao criar dados iniciais: " + e.getMessage());
            e.printStackTrace();
        }
    }
}