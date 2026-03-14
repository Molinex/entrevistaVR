package entrevista.rabbitmq.rabbitmq.model;

    import java.util.UUID;

    public class Pedido {
        private UUID id;
        private String produto;
        private int quantidade;
        private String dataCriacao;

        // Getters e setters
        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getProduto() {
            return produto;
        }

        public void setProduto(String produto) {
            this.produto = produto;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public String getDataCriacao() {
            return dataCriacao;
        }

        public void setDataCriacao(String dataCriacao) {
        }
    }