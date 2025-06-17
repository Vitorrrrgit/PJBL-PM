package persistencia;


public class SistemaException extends Exception {
private final String tipoEntidade;
private final String identificador;
private final String codigoErro;
    
    public SistemaException(String tipoEntidade, String identificador, String codigoErro) {
        super(String.format("[%s:%s] %s", tipoEntidade, identificador, codigoErro));
        this.tipoEntidade = tipoEntidade;
        this.identificador = identificador;
        this.codigoErro = codigoErro;
    }
    

    public static SistemaException usuarioNaoEncontrado(String identificador) {
        return new SistemaException("Usuario", identificador, "Usuário não encontrado");
    }
    
    public static SistemaException emailJaCadastrado(String email) {
        return new SistemaException("Usuario", email, "Email já cadastrado");
    }
    
    public static SistemaException frequenciaNaoEncontrada(int id) {
        return new SistemaException("Frequencia", String.valueOf(id), "Frequência não encontrada");
    }
    
    // Getters
    public String getTipoEntidade() { return tipoEntidade; }
    public String getIdentificador() { return identificador; }
    public String getCodigoErro() { return codigoErro; }
    
    public String getDetalhesErro() {
        return String.format("Erro: %s [%s] - %s", tipoEntidade, identificador, codigoErro);
    }
}
