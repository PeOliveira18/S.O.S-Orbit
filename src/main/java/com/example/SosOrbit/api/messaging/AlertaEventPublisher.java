package com.example.SosOrbit.api.messaging;

import com.example.SosOrbit.api.model.Alerta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlertaEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(AlertaEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String alertaExchange;
    private final String alertaRoutingKey;

    public AlertaEventPublisher(RabbitTemplate rabbitTemplate,
                                @Value("${app.rabbitmq.alerta-exchange}") String alertaExchange,
                                @Value("${app.rabbitmq.alerta-routing-key}") String alertaRoutingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.alertaExchange = alertaExchange;
        this.alertaRoutingKey = alertaRoutingKey;
    }

    public void publicarAlertaCriado(Alerta alerta) {
        publicar("ALERTA_CRIADO", alerta);
    }

    public void publicarStatusAtualizado(Alerta alerta) {
        publicar("STATUS_ATUALIZADO", alerta);
    }

    private void publicar(String tipoEvento, Alerta alerta) {
        try {
            AlertaRiscoEvent evento = AlertaRiscoEvent.from(tipoEvento, alerta);
            rabbitTemplate.convertAndSend(alertaExchange, alertaRoutingKey, evento);
            logger.info("Evento enviado ao RabbitMQ: {} - alerta {}", tipoEvento, alerta.getId());
        } catch (AmqpException exception) {
            logger.warn("Nao foi possivel enviar evento de alerta ao RabbitMQ: {}", exception.getMessage());
        }
    }
}
