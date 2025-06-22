package models;

import java.util.ArrayList;
import java.util.List;

public class Professor extends Usuario {
    private final String area;
    private final String titulacao;
    private final List<String> disciplinas;

    public Professor(int id, String nome, String email, String cpf, String area, String titulacao) {
        super(id, nome, email, cpf);
        this.area = area;
        this.titulacao = titulacao;
        this.disciplinas = new ArrayList<>();
    }

    @Override
    public String getTipoUsuario() { return "Professor"; }

    @Override
    public String[] getPermissoes() { return new String[] { "EDITAR_FREQUENCIA" }; }

    @Override
    public String gerarRelatorioPersonalizado() { return "Relatório de Aulas do Professor " + getNome(); }

    @Override
    public boolean podeGerenciarUsuarios() { return false; }

    @Override
    public boolean podeVerRelatoriosCompletos() { return false; }

    @Override
    public boolean podeExportarDados() { return false; }

    @Override
    public boolean podeEditarFrequencia() { return true; }

    // Getters
    public String getArea() { return area; }
    public String getTitulacao() { return titulacao; }
}