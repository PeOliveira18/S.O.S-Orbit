package com.example.SosOrbit.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${app.rabbitmq.alerta-exchange}")
    private String alertaExchange;

    @Value("${app.rabbitmq.alerta-queue}")
    private String alertaQueue;

    @Value("${app.rabbitmq.alerta-routing-key}")
    private String alertaRoutingKey;

    @Bean
    public DirectExchange alertaExchange() {
        return new DirectExchange(alertaExchange);
    }

    @Bean
    public Queue alertaQueue() {
        return QueueBuilder.durable(alertaQueue).build();
    }

    @Bean
    public Binding alertaBinding(Queue alertaQueue, DirectExchange alertaExchange) {
        return BindingBuilder.bind(alertaQueue).to(alertaExchange).with(alertaRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
