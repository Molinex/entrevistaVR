package entrevista.rabbitmq.rabbitmq.service;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.stereotype.Service;

    import java.util.Map;
    import java.util.UUID;
    import java.util.concurrent.ConcurrentHashMap;

    @Service
    public class PedidoService {
        private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);
        private final Map<UUID, String> pedidosStatus = new ConcurrentHashMap<>();

        public void salvarPedido(UUID id, String status) {
            logger.info("Salvando pedido: ID={} Status={}", id, status);
            pedidosStatus.put(id, status);
        }

        public String obterStatus(UUID id) {
            String status = pedidosStatus.getOrDefault(id, "Status não encontrado");
            logger.info("Consultando status do pedido: ID={} Status={}", id, status);
            return status;
        }

        public void atualizarStatus(UUID id, String novoStatus) {
            logger.info("Atualizando status do pedido: ID={} NovoStatus={}", id, novoStatus);
            pedidosStatus.put(id, novoStatus);
        }

        public boolean pedidoExiste(UUID id) {
            return pedidosStatus.containsKey(id);
        }
    }