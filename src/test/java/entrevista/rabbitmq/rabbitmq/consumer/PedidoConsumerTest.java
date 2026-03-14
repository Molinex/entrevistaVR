package entrevista.rabbitmq.rabbitmq.consumer;

import entrevista.rabbitmq.rabbitmq.config.RabbitConfig;
import entrevista.rabbitmq.rabbitmq.model.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;


class PedidoConsumerTest {

    @InjectMocks
    private PedidoConsumer pedidoConsumer;

    @Mock
    private RabbitConfig rabbitConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessarPedido() {
        Pedido pedido = new Pedido();
        pedido.setId(UUID.randomUUID()); // Use a UUID instead of a long

        pedidoConsumer.processarPedido(pedido);

        // Adicione verificações ou asserts conforme necessário
    }
}