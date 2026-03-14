package entrevista.rabbitmq.rabbitmq.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entrevista.rabbitmq.rabbitmq.config.RabbitConfig;

@Service
public class MessageProducer {
    private final AmqpTemplate amqpTemplate;

    @Autowired
    public MessageProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendMessage(String msg) {
        amqpTemplate.convertAndSend(RabbitConfig.QUEUE_PEDIDOS_ENTRADA, msg);
        System.out.println("Mensagem enviada: " + msg);
    }
}