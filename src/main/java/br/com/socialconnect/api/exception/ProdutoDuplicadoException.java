package br.com.socialconnect.api.exception;

public class ProdutoDuplicadoException extends RuntimeException {

    private final String nome;

    public ProdutoDuplicadoException(String nome) {
        super("Produto já cadastrado: " + nome);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }
}
