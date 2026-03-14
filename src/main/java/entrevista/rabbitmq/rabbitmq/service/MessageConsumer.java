package entrevista.rabbitmq.rabbitmq.service;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import entrevista.rabbitmq.rabbitmq.config.RabbitConfig;
    import entrevista.rabbitmq.rabbitmq.swingapp.PedidoRequest;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.amqp.rabbit.annotation.RabbitListener;
    import org.springframework.stereotype.Service;

    @Service
    public class MessageConsumer {

        private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
        private final ObjectMapper objectMapper;
        private final PedidoService pedidoService;

        public MessageConsumer(ObjectMapper objectMapper, PedidoService pedidoService) {
            this.objectMapper = objectMapper;
            this.pedidoService = pedidoService;
        }

        @RabbitListener(queues = RabbitConfig.QUEUE_PEDIDOS_ENTRADA)
        public void consumeMessage(String message) {
            try {
                PedidoRequest pedido = objectMapper.readValue(message, PedidoRequest.class);
                logger.info("Pedido recebido: ID={} Produto={}", pedido.getId(), pedido.getProduto());

                // Atualizar status no backend
                pedidoService.atualizarStatus(pedido.getId(), "PROCESSADO");
                logger.info("Status do pedido atualizado para PROCESSADO: ID={}", pedido.getId());
            } catch (Exception e) {
                logger.error("Erro ao processar mensagem: {}", e.getMessage(), e);
            }
        }
    }