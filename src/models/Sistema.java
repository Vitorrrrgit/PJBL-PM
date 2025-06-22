package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import persistencia.SistemaException;

public class Sistema implements Serializable {
    @java.io.Serial
    private static final long serialVersionUID = 1L;

    private static final String PASTA_DADOS = "dados";
    private static final String ARQUIVO_USUARIOS = PASTA_DADOS + File.separator + "usuarios.ser";
    private static final String ARQUIVO_FREQUENCIAS = PASTA_DADOS + File.separator + "frequencias.ser";
    private List<Usuario> usuarios;
    private List<Frequencia> frequencias;

    public Sistema() {
        criarPastaDados();
        carregarDados();
    }

    private void criarPastaDados() {
        File pasta = new File(PASTA_DADOS);
        if (!pasta.exists()) pasta.mkdirs();
    }

    private void salvarDados() {
        try {
            salvarLista(this.usuarios, ARQUIVO_USUARIOS);
            salvarLista(this.frequencias, ARQUIVO_FREQUENCIAS);
        } catch (IOException e) {
            System.err.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    private void carregarDados() {
        this.usuarios = carregarLista(ARQUIVO_USUARIOS);
        this.frequencias = carregarLista(ARQUIVO_FREQUENCIAS);
    }

    private <T> void salvarLista(List<T> lista, String nomeArquivo) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) { oos.writeObject(lista); }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> carregarLista(String nomeArquivo) {
        File arquivo = new File(nomeArquivo);
        if (!arquivo.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) { return new ArrayList<>(); }
    }

    public List<Usuario> listarUsuarios() { return new ArrayList<>(this.usuarios); }
    
    public Usuario buscarUsuarioPorEmail(String email) throws SistemaException {
        return usuarios.stream().filter(u -> u.getEmail().equalsIgnoreCase(email)).findFirst()
            .orElseThrow(() -> new SistemaException("Login", email, "Usuário não encontrado."));
    }

    public List<Frequencia> buscarFrequenciasPorDisciplina(String nomeDisciplina) {
        if (nomeDisciplina == null || nomeDisciplina.isEmpty()) return new ArrayList<>();
        return frequencias.stream().filter(f -> f.getDisciplina().equalsIgnoreCase(nomeDisciplina)).collect(Collectors.toList());
    }
    
    public int obterProximoIdFrequencia() {
        return frequencias.stream().mapToInt(Frequencia::getId).max().orElse(0) + 1;
    }

    public void adicionarFrequencia(Frequencia f) {
        this.frequencias.add(f);
        salvarDados();
    }
}