package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Frequencia implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String alunoMatricula;
    private String registradoPorCpf;
    private String disciplina;
    private LocalDate data;
    private boolean presente;
    private String observacoes;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Frequencia() {}

    public Frequencia(int id, String alunoMatricula, String registradoPorCpf,
            String disciplina, LocalDate data, boolean presente, String observacoes) {
        this.id = id;
        this.alunoMatricula = alunoMatricula;
        this.registradoPorCpf = registradoPorCpf;
        this.disciplina = disciplina;
        this.data = data;
        this.presente = presente;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getAlunoMatricula() { return alunoMatricula; }
    public String getRegistradoPorCpf() { return registradoPorCpf; }
    public String getDisciplina() { return disciplina; }
    public LocalDate getData() { return data; }
    public boolean isPresente() { return presente; }
    public String getObservacoes() { return observacoes; }
    public String getDataFormatada() { return data != null ? data.format(FORMATO_DATA) : "N/A"; }
    public String getStatus() { return presente ? "Presente" : "Falta"; }
}