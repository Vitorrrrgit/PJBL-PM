package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import models.*;

public class SerializadorJava {

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // FORMATO ESPERADO NO CSV:
    // TIPO;ID;NOME;EMAIL;CPF;CAMPO1;CAMPO2;CAMPO3
    // Aluno;1;Ana Silva;ana@email.com;111;MAT-01;Engenharia;2
    // Professor;2;Carlos;carlos@email.com;222;Cálculo;Doutorado;
    // Frequencia;101;MAT-01;222;Cálculo I;17/06/2025;true;

    public void carregarDeCSV(String caminhoArquivo, List<Usuario> usuarios, List<Frequencia> frequencias) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] campos = linha.split(";");
                if (campos.length == 0) continue;

                String tipo = campos[0];
                try {
                    if (tipo.equalsIgnoreCase("Aluno") || tipo.equalsIgnoreCase("Professor")) {
                        Usuario novoUsuario = processarLinhaUsuario(campos);
                        if (novoUsuario != null) usuarios.add(novoUsuario);
                    } else if (tipo.equalsIgnoreCase("Frequencia")) {
                        Frequencia novaFrequencia = processarLinhaFrequencia(campos);
                        if (novaFrequencia != null) frequencias.add(novaFrequencia);
                    }
                } catch (Exception e) {
                    System.err.println("AVISO: Linha do CSV ignorada por erro de formato: " + linha);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro geral ao carregar dados do CSV: " + e.getMessage());
        }
    }

    private Usuario processarLinhaUsuario(String[] campos) {
        int id = Integer.parseInt(campos[1]);
        String nome = campos[2];
        String email = campos[3];
        String cpf = campos[4];
        
        if (campos[0].equalsIgnoreCase("Aluno")) {
            String matricula = campos[5];
            String curso = campos[6];
            int semestre = Integer.parseInt(campos[7]);
            return new Aluno(id, nome, email, cpf, matricula, curso, semestre);
        } else if (campos[0].equalsIgnoreCase("Professor")) {
            String area = campos[5];
            String titulacao = campos[6];
            return new Professor(id, nome, email, cpf, area, titulacao);
        }
        return null;
    }

    private Frequencia processarLinhaFrequencia(String[] campos) {
        int id = Integer.parseInt(campos[1]);
        String matriculaAluno = campos[2];
        String cpfProfessor = campos[3];
        String disciplina = campos[4];
        LocalDate data = LocalDate.parse(campos[5], FORMATADOR_DATA);
        boolean presente = Boolean.parseBoolean(campos[6]);
        String obs = (campos.length > 7) ? campos[7] : "";
        
        return new Frequencia(id, matriculaAluno, cpfProfessor, disciplina, data, presente, obs);
    }
}