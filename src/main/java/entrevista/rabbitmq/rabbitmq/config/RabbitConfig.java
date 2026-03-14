package entrevista.rabbitmq.rabbitmq.config;

            import com.fasterxml.jackson.databind.ObjectMapper;
            import com.fasterxml.jackson.databind.SerializationFeature;
            import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
            import org.springframework.amqp.core.*;
            import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
            import org.springframework.amqp.rabbit.connection.ConnectionFactory;
            import org.springframework.amqp.rabbit.core.RabbitTemplate;
            import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
            import org.springframework.context.annotation.Bean;
            import org.springframework.context.annotation.Configuration;

            @Configuration
            public class RabbitConfig {

                public static final String QUEUE_PEDIDOS_ENTRADA = "pedidos.entrada.seu-nome";
                public static final String QUEUE_PEDIDOS_ENTRADA_DLQ = "pedidos.entrada.seu-nome.dlq";
                public static final String QUEUE_PEDIDOS_STATUS_FALHA = "pedidos.status.falha.seu-nome"; // Adicionada a constante faltante
                public static final String QUEUE_PEDIDOS_STATUS_SUCESSO = "pedidos.status.sucesso.seu-nome";
                @Bean
                public ConnectionFactory connectionFactory() {
                    CachingConnectionFactory connectionFactory = new CachingConnectionFactory("jaragua-01.lmq.cloudamqp.com");
                    connectionFactory.setUsername("bjnuffmq");
                    connectionFactory.setPassword("gj-YQIiEXyfxQxjsZtiYDKeXIT8ppUq7");
                    connectionFactory.setVirtualHost("bjnuffmq");
                    return connectionFactory;
                }

                @Bean
                public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
                    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
                    rabbitTemplate.setMessageConverter(messageConverter);
                    return rabbitTemplate;
                }

                @Bean
                public Jackson2JsonMessageConverter messageConverter() {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.registerModule(new JavaTimeModule()); // Registra o suporte para tipos de data/hora do Java 8
                    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Configura para usar o formato ISO-8601
                    return new Jackson2JsonMessageConverter(objectMapper);
                }

                @Bean
                public Queue pedidosEntradaQueue() {
                    return QueueBuilder.durable(QUEUE_PEDIDOS_ENTRADA)
                            .deadLetterExchange("")
                            .deadLetterRoutingKey(QUEUE_PEDIDOS_ENTRADA_DLQ)
                            .build();
                }

                @Bean
                public Queue pedidosEntradaDLQ() {
                    return QueueBuilder.durable(QUEUE_PEDIDOS_ENTRADA_DLQ).build();
                }

                @Bean
                public Queue pedidosStatusSucessoQueue() {
                    return QueueBuilder.durable(QUEUE_PEDIDOS_STATUS_SUCESSO).build();
                }

                @Bean
                public Queue pedidosStatusFalhaQueue() {
                    return QueueBuilder.durable(QUEUE_PEDIDOS_STATUS_FALHA).build();
                }

                @Bean
                public DirectExchange pedidosExchange() {
                    return new DirectExchange("pedidos.exchange.seu-nome");
                }

                @Bean
                public Binding bindingPedidosEntrada(Queue pedidosEntradaQueue, DirectExchange pedidosExchange) {
                    return BindingBuilder.bind(pedidosEntradaQueue).to(pedidosExchange).with(QUEUE_PEDIDOS_ENTRADA);
                }
            }