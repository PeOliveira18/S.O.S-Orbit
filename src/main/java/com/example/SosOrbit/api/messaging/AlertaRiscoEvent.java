package com.example.SosOrbit.api.messaging;

import com.example.SosOrbit.api.model.Alerta;

import java.time.LocalDateTime;

public record AlertaRiscoEvent(
        String tipoEvento,
        Long alertaId,
        Long regiaoId,
        String nomeRegiao,
        String cidade,
        String estado,
        String nivelRisco,
        Double indiceRisco,
        String status,
        LocalDateTime dataCriacao,
        String mensagem
) {
    public static AlertaRiscoEvent from(String tipoEvento, Alerta alerta) {
        Long regiaoId = alerta.getRegiao() == null ? null : alerta.getRegiao().getId();
        String nomeRegiao = alerta.getRegiao() == null ? null : alerta.getRegiao().getNome();
        String cidade = alerta.getRegiao() == null ? null : alerta.getRegiao().getCidade();
        String estado = alerta.getRegiao() == null ? null : alerta.getRegiao().getEstado();
        String nivelRisco = alerta.getNivelRisco() == null ? null : alerta.getNivelRisco().name();
        String status = alerta.getStatus() == null ? null : alerta.getStatus().name();

        return new AlertaRiscoEvent(
                tipoEvento,
                alerta.getId(),
                regiaoId,
                nomeRegiao,
                cidade,
                estado,
                nivelRisco,
                alerta.getIndiceRisco(),
                status,
                alerta.getDataCriacao(),
                alerta.getMensagem()
        );
    }
}
