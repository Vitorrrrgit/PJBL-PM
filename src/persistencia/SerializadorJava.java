package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import models.*;

public class SerializadorJava {

    private static final String ARQUIVO_DADOS = "dados_sistema.csv";
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void carregarUsuariosCSV(List<Usuario> usuarios) {

        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_DADOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty())
                    continue;

                String[] campos = linha.split(";");
                if (campos.length < 5)
                    continue; // Pula linhas malformadas

                String tipo = campos[0];

                try {
                    Usuario novoUsuario = null;
                    int id = Integer.parseInt(campos[1]);
                    String nome = campos[2];
                    String email = campos[3];
                    String cpf = campos[4];

                    switch (tipo) {
                        case "Aluno" -> {
                            if (campos.length > 7) {
                                String matricula = campos[5];
                                String curso = campos[6];
                                int semestre = Integer.parseInt(campos[7]);
                                novoUsuario = new Aluno(id, nome, email, cpf, matricula, curso, semestre);
                            }
                        }
                        case "Professor" -> {
                            if (campos.length > 6) {
                                String area = campos[5];
                                String titulacao = campos[6];
                                novoUsuario = new Professor(id, nome, email, cpf, area, titulacao);
                            }
                        }
                        case "Coordenador" -> {
                            if (campos.length > 5) {
                                String cursoCoord = campos[5];
                                novoUsuario = new Coordenador(id, nome, email, cpf, cursoCoord);
                            }
                        }
                        case "Administrador" -> {
                            if (campos.length > 5) {
                                String nivelAcesso = campos[5];
                                novoUsuario = new Administrador(id, nome, email, cpf, nivelAcesso);
                            }
                        }
                    }

                    if (novoUsuario != null) {
                        usuarios.add(novoUsuario);
                    }

                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("AVISO: Linha do CSV de usuário ignorada por erro de formato: " + linha);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro geral ao carregar usuários do CSV: " + e.getMessage());
        }
    }

    public void carregarFrequenciasCSV(List<Frequencia> frequencias) {
        try (BufferedReader br = new BufferedReader(new FileReader("dados_sistema.csv"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty())
                    continue;

                String[] campos = linha.split(";");

                // Pula a linha se não for uma frequência
                if (!campos[0].equals("Frequencia")) {
                    continue;
                }

                try {
                    if (campos.length < 7) {
                        throw new ArrayIndexOutOfBoundsException("Colunas insuficientes para criar uma frequência.");
                    }

                    int id = Integer.parseInt(campos[1]);
                    String matriculaAluno = campos[2];
                    String cpfProfessor = campos[3];
                    String disciplina = campos[4];
                    LocalDate data = LocalDate.parse(campos[5], FORMATADOR_DATA);
                    boolean presente = Boolean.parseBoolean(campos[6]);
                    String obs = (campos.length > 7) ? campos[7] : ""; // Observação é opcional

                    Frequencia novaFrequencia = new Frequencia(id, matriculaAluno, cpfProfessor, disciplina, data,
                            presente, obs);
                    frequencias.add(novaFrequencia);

                } catch (NumberFormatException | DateTimeParseException | ArrayIndexOutOfBoundsException e) {
                    System.err.println("AVISO: Linha de frequência ignorada por erro de formato: " + linha);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro geral ao carregar frequências do CSV: " + e.getMessage());
        }
    }

}