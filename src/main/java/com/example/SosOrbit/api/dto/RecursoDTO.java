package com.example.SosOrbit.api.dto;

import com.example.SosOrbit.api.model.PrioridadeRecurso;
import com.example.SosOrbit.api.model.Recurso;
import com.example.SosOrbit.api.model.StatusRecurso;
import com.example.SosOrbit.api.model.TipoRecurso;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record RecursoDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @NotNull(message = "Tipo do recurso e obrigatorio")
        TipoRecurso tipo,

        @NotBlank(message = "Descricao e obrigatoria")
        @Size(max = 160, message = "Descricao deve ter no maximo 160 caracteres")
        String descricao,

        @Positive(message = "Quantidade deve ser maior que zero")
        Integer quantidade,

        @Size(max = 30, message = "Unidade de medida deve ter no maximo 30 caracteres")
        String unidadeMedida,

        PrioridadeRecurso prioridade,
        StatusRecurso status,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long alertaId
) {
    public static RecursoDTO from(Recurso recurso) {
        Long alertaId = recurso.getAlerta() == null ? null : recurso.getAlerta().getId();

        return new RecursoDTO(
                recurso.getId(),
                recurso.getTipo(),
                recurso.getDescricao(),
                recurso.getQuantidade(),
                recurso.getUnidadeMedida(),
                recurso.getPrioridade(),
                recurso.getStatus(),
                alertaId
        );
    }
}
