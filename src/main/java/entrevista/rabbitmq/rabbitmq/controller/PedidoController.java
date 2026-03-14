package entrevista.rabbitmq.rabbitmq.controller;

import entrevista.rabbitmq.rabbitmq.config.RabbitConfig;
import entrevista.rabbitmq.rabbitmq.model.Pedido;
import entrevista.rabbitmq.rabbitmq.service.MessageConsumer;
import entrevista.rabbitmq.rabbitmq.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);
    private final AmqpTemplate amqpTemplate;
    private final PedidoService pedidoService;

    public PedidoController(AmqpTemplate amqpTemplate, PedidoService pedidoService) {
        this.amqpTemplate = amqpTemplate;
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<String> criarPedido(@Valid @RequestBody Pedido pedido) {
        try {
            if (pedido.getQuantidade() <= 0 || (pedido.getProduto() == null || pedido.getProduto().isEmpty())) {
                logger.warn("Pedido inválido recebido: {}", pedido);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pedido inválido.");
            }

            // Usar o ID enviado pelo cliente (Swing) ou gerar um novo se não existir
            if (pedido.getId() == null) {
                pedido.setId(UUID.randomUUID());
            }
            
            if (pedido.getDataCriacao() == null) {
                pedido.setDataCriacao(LocalDateTime.now().toString());
            }

            // Salvar o pedido com status inicial
            pedidoService.salvarPedido(pedido.getId(), "ENVIADO");

            // Enviar o pedido para a fila RabbitMQ
            amqpTemplate.convertAndSend(RabbitConfig.QUEUE_PEDIDOS_ENTRADA, pedido);

            logger.info("Pedido criado e enviado: ID={} Produto={}", pedido.getId(), pedido.getProduto());
            // Retorna o ID para garantir que o cliente saiba qual foi usado
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(pedido.getId().toString());
        } catch (Exception e) {
            logger.error("Erro ao criar pedido: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar pedido.");
        }
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<String> obterStatus(@PathVariable UUID id) {
        try {
            if (!pedidoService.pedidoExiste(id)) {
                logger.warn("Pedido não encontrado: ID={}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado.");
            }

            String status = pedidoService.obterStatus(id);
            logger.info("Status do pedido consultado: ID={} Status={}", id, status);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("Erro ao obter status do pedido: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao consultar status.");
        }
    }
}
