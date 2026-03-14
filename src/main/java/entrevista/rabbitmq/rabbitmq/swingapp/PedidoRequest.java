package entrevista.rabbitmq.rabbitmq.swingapp;

import java.util.UUID;

public class PedidoRequest {
    private UUID id;
    private String produto;
    private int quantidade;

    public PedidoRequest(UUID id, String produto, int quantidade) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public UUID getId() {
        return id;
    }

    public String getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }
}