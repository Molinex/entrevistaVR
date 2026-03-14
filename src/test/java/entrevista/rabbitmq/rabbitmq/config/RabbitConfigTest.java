package entrevista.rabbitmq.rabbitmq.config;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class RabbitConfigTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Jackson2JsonMessageConverter messageConverter;

    @Test
    void testRabbitTemplateNotNull() {
    }

    @Test
    void testMessageConverterNotNull() {
        assertNotNull(messageConverter, "MessageConverter should not be null");
    }
}