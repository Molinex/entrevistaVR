package entrevista.rabbitmq.rabbitmq.service;

import entrevista.rabbitmq.rabbitmq.config.RabbitConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.AmqpTemplate;

import static org.mockito.Mockito.*;

class MessageProducerTest {

    @InjectMocks
    private MessageProducer messageProducer;

    @Mock
    private AmqpTemplate amqpTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEnviarPedido() {
        String mensagem = "Pedido Teste";

        messageProducer.sendMessage(mensagem);

        verify(amqpTemplate, times(1)).convertAndSend(eq(RabbitConfig.QUEUE_PEDIDOS_ENTRADA), eq(mensagem));
    }
}