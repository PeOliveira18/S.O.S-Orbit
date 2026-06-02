package com.example.SosOrbit.api.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AlertaEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(AlertaEventConsumer.class);

    @RabbitListener(queues = "${app.rabbitmq.alerta-queue}")
    public void receberAlerta(AlertaRiscoEvent evento) {
        logger.info(
                "Evento recebido do RabbitMQ: {} | alerta={} | regiao={} | nivel={} | status={}",
                evento.tipoEvento(),
                evento.alertaId(),
                evento.nomeRegiao(),
                evento.nivelRisco(),
                evento.status()
        );
    }
}
