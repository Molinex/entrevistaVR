package entrevista.rabbitmq.rabbitmq.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import entrevista.rabbitmq.rabbitmq.swingapp.PedidoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.Mockito.*;

class MessageConsumerTest {

    @InjectMocks
    private MessageConsumer messageConsumer;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessarPedido() throws Exception {
        // JSON representing a PedidoRequest
        String mensagem = "{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"produto\":\"Produto Teste\",\"quantidade\":5}";

        // Mock PedidoRequest object
        UUID id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        PedidoRequest pedidoRequest = new PedidoRequest(id, "Produto Teste", 5);

        // Configure ObjectMapper behavior
        when(objectMapper.readValue(mensagem, PedidoRequest.class)).thenReturn(pedidoRequest);

        // Execute the method
        messageConsumer.consumeMessage(mensagem);

        // Verify if atualizarStatus was called correctly
        verify(pedidoService, times(1)).atualizarStatus(pedidoRequest.getId(), "PROCESSADO");
    }
}