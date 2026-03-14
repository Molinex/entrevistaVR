package entrevista.rabbitmq.rabbitmq.service;

    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.MockitoAnnotations;

    import java.util.UUID;

    import static org.junit.jupiter.api.Assertions.assertEquals;

    class PedidoServiceTest {

        @InjectMocks
        private PedidoService pedidoService;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
        }

        @Test
        void testSalvarPedido() {
            UUID id = UUID.randomUUID();
            String status = "ENVIADO";

            pedidoService.salvarPedido(id, status);

            assertEquals(status, pedidoService.obterStatus(id));
        }

        @Test
        void testObterStatusPedidoNaoEncontrado() {
            UUID id = UUID.randomUUID();

            String status = pedidoService.obterStatus(id);

            assertEquals("Status não encontrado", status);
        }

        @Test
        void testAtualizarStatus() {
            UUID id = UUID.randomUUID();
            String statusInicial = "ENVIADO";
            String novoStatus = "PROCESSANDO";

            pedidoService.salvarPedido(id, statusInicial);
            pedidoService.atualizarStatus(id, novoStatus);

            assertEquals(novoStatus, pedidoService.obterStatus(id));
        }

        @Test
        void testPedidoExiste() {
            UUID id = UUID.randomUUID();

            pedidoService.salvarPedido(id, "ENVIADO");

            assertEquals(true, pedidoService.pedidoExiste(id));
        }
    }