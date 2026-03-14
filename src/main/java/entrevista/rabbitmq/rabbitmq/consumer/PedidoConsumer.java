package entrevista.rabbitmq.rabbitmq.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import entrevista.rabbitmq.rabbitmq.config.RabbitConfig;
import entrevista.rabbitmq.rabbitmq.model.Pedido;

import java.util.Random;

@Component
public class PedidoConsumer {
    private final Random random = new Random();

    @RabbitListener(queues = RabbitConfig.QUEUE_PEDIDOS_ENTRADA)
    public void processarPedido(Pedido pedido) {
        System.out.println("Processando pedido: " + pedido.getId());

        try {
            Thread.sleep((1 + random.nextInt(3)) * 1000); // Simula o tempo de processamento

            if (random.nextDouble() < 0.2) {
                throw new RuntimeException("Erro ao processar o pedido.");
            }

            System.out.println("Pedido processado com sucesso: " + pedido.getId());
            // Publica na fila de sucesso
        } catch (Exception e) {
            System.out.println("Falha ao processar o pedido: " + pedido.getId());
            // Publica na fila de falha
        }
    }
}