package com.example.SosOrbit.api.dto;

import com.example.SosOrbit.api.model.Alerta;
import com.example.SosOrbit.api.model.NivelRisco;
import com.example.SosOrbit.api.model.StatusAlerta;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record AlertaDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @NotBlank(message = "Titulo e obrigatorio")
        @Size(max = 120, message = "Titulo deve ter no maximo 120 caracteres")
        String titulo,

        @Size(max = 500, message = "Mensagem deve ter no maximo 500 caracteres")
        String mensagem,

        Double indiceRisco,
        NivelRisco nivelRisco,
        StatusAlerta status,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime dataCriacao,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long regiaoId,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long dadoAmbientalId
) {
    public static AlertaDTO from(Alerta alerta) {
        Long regiaoId = alerta.getRegiao() == null ? null : alerta.getRegiao().getId();
        Long dadoAmbientalId = alerta.getDadoAmbiental() == null ? null : alerta.getDadoAmbiental().getId();

        return new AlertaDTO(
                alerta.getId(),
                alerta.getTitulo(),
                alerta.getMensagem(),
                alerta.getIndiceRisco(),
                alerta.getNivelRisco(),
                alerta.getStatus(),
                alerta.getDataCriacao(),
                regiaoId,
                dadoAmbientalId
        );
    }
}
