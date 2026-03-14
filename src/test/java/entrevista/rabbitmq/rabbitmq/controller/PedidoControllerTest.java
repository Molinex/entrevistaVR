package entrevista.rabbitmq.rabbitmq.controller;

import entrevista.rabbitmq.rabbitmq.config.RabbitConfig;
import entrevista.rabbitmq.rabbitmq.model.Pedido;
import entrevista.rabbitmq.rabbitmq.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock
    private AmqpTemplate amqpTemplate;

    @Mock
    private PedidoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarPedido_Sucesso() {
        Pedido pedido = new Pedido();
        pedido.setProduto("Produto Teste");
        pedido.setQuantidade(10);

        doNothing().when(pedidoService).salvarPedido(any(UUID.class), eq("ENVIADO"));

        ResponseEntity<String> response = pedidoController.criarPedido(pedido);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertTrue(response.getBody().contains("Pedido recebido com ID:"));
        verify(amqpTemplate, times(1)).convertAndSend(eq(RabbitConfig.QUEUE_PEDIDOS_ENTRADA), eq(pedido));
        verify(pedidoService, times(1)).salvarPedido(any(UUID.class), eq("ENVIADO"));
    }

    @Test
    void testCriarPedido_PedidoInvalido() {
        Pedido pedido = new Pedido();
        pedido.setProduto(""); // Produto inválido
        pedido.setQuantidade(0); // Quantidade inválida

        ResponseEntity<String> response = pedidoController.criarPedido(pedido);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Pedido inválido.", response.getBody());

        // Verifica que o amqpTemplate não foi chamado
        verify(amqpTemplate, never()).convertAndSend(anyString(), any(Pedido.class));
        verify(pedidoService, never()).salvarPedido(any(UUID.class), anyString());
    }

    @Test
    void testObterStatus_Sucesso() {
        UUID pedidoId = UUID.randomUUID();
        when(pedidoService.pedidoExiste(pedidoId)).thenReturn(true);
        when(pedidoService.obterStatus(pedidoId)).thenReturn("PROCESSANDO");

        ResponseEntity<String> response = pedidoController.obterStatus(pedidoId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status do pedido: PROCESSANDO", response.getBody());
        verify(pedidoService, times(1)).pedidoExiste(pedidoId);
        verify(pedidoService, times(1)).obterStatus(pedidoId);
    }

    @Test
    void testObterStatus_PedidoNaoEncontrado() {
        UUID pedidoId = UUID.randomUUID();
        when(pedidoService.pedidoExiste(pedidoId)).thenReturn(false);

        ResponseEntity<String> response = pedidoController.obterStatus(pedidoId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Pedido não encontrado.", response.getBody());
        verify(pedidoService, times(1)).pedidoExiste(pedidoId);
        verify(pedidoService, never()).obterStatus(pedidoId);
    }
}