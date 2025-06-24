package models.persistencia;

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
    
    // Métodos estáticos para criar exceções comuns
    public static SistemaException usuarioNaoEncontrado(String identificador) {
        return new SistemaException("Usuario", identificador, "Usuário não encontrado");
    }
    
    public static SistemaException emailJaCadastrado(String email) {
        return new SistemaException("Usuario", email, "Email já cadastrado");
    }
    
    public static SistemaException frequenciaNaoEncontrada(int id) {
        return new SistemaException("Frequencia", String.valueOf(id), "Frequência não encontrada");
    }
    
    public static SistemaException dadosInvalidos(String campo) {
        return new SistemaException("Dados", campo, "Dado inválido");
    }
    
    // Getters
    public String getTipoEntidade() { 
        return tipoEntidade; 
    }
    
    public String getIdentificador() { 
        return identificador; 
    }
    
    public String getCodigoErro() { 
        return codigoErro; 
    }
    
    // Método para compatibilidade com o código existente
    public String getDetalhesErro() {
        return String.format("Erro: %s [%s] - %s", tipoEntidade, identificador, codigoErro);
    }
    
    public String getMensagemFormatada() {
        return String.format("Entidade: %s | ID/Campo: %s | Erro: %s", 
                tipoEntidade, identificador, codigoErro);
    }
}