package entrevista.rabbitmq.rabbitmq.model;

    import org.junit.jupiter.api.Test;

    import java.util.UUID;

    import static org.junit.jupiter.api.Assertions.assertEquals;

    class PedidoTest {

        @Test
        void testPedidoCreation() {
            UUID id = UUID.randomUUID();
            Pedido pedido = new Pedido();
            pedido.setId(id);
            pedido.setProduto("Produto Teste");
            pedido.setQuantidade(5);
            pedido.setDataCriacao("2023-11-01T10:00:00");

            assertEquals(id, pedido.getId());
            assertEquals("Produto Teste", pedido.getProduto());
            assertEquals(5, pedido.getQuantidade());
            assertEquals("2023-11-01T10:00:00", pedido.getDataCriacao());
        }
    }