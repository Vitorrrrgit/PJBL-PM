package persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import models.Frequencia;

public class SerializadorJava {

    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public void carregarFrequenciasDeCSV(String caminhoArquivo, List<Frequencia> frequencias, int proximoId) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            br.readLine(); // Pula a linha do cabeçalho
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || !linha.toUpperCase().startsWith("FREQUENCIA")) continue;
                String[] campos = linha.split(";", -1);
                
                if (campos.length > 6) {
                    try {
                        Frequencia novaFrequencia = processarLinhaFrequencia(proximoId++, campos);
                        if (frequencias.stream().noneMatch(f -> f.getId() == novaFrequencia.getId())) {
                            frequencias.add(novaFrequencia);
                        }
                    } catch (Exception e) {
                        System.err.println("AVISO: Linha de frequência ignorada: " + linha);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo CSV: " + e.getMessage());
        }
    }

    public void salvarFrequenciasCSV(String caminhoArquivo, List<Frequencia> frequencias) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(caminhoArquivo))) {
            pw.println("TIPO;ID;MATRICULA_ALUNO;CPF_PROFESSOR;DISCIPLINA;DATA(dd/MM/yyyy);PRESENTE;OBSERVACOES");
            for (Frequencia f : frequencias) {
                pw.printf("Frequencia;%d;%s;%s;%s;%s;%b;%s%n",
                        f.getId(), f.getAlunoMatricula(), f.getRegistradoPorCpf(),
                        f.getDisciplina(), f.getData().format(FORMATADOR_DATA),
                        f.isPresente(), f.getObservacoes());
            }
        }
    }

    private Frequencia processarLinhaFrequencia(int id, String[] campos) {
        String matriculaAluno = campos[2];
        String cpfProfessor = campos[3];
        String disciplina = campos[4];
        LocalDate data = LocalDate.parse(campos[5], FORMATADOR_DATA);
        boolean presente = Boolean.parseBoolean(campos[6]);
        String obs = (campos.length > 7) ? campos[7] : "";
        
        return new Frequencia(id, matriculaAluno, cpfProfessor, disciplina, data, presente, obs);
    }
}