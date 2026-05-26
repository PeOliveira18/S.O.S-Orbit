package com.example.SosOrbit.api.dto;

import com.example.SosOrbit.api.model.RegiaoMonitorada;
import com.example.SosOrbit.api.model.TipoRisco;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record RegiaoMonitoradaDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
        String nome,

        @NotBlank(message = "Cidade e obrigatoria")
        @Size(max = 80, message = "Cidade deve ter no maximo 80 caracteres")
        String cidade,

        @NotBlank(message = "Estado e obrigatorio")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
        String estado,

        @NotNull(message = "Latitude e obrigatoria")
        Double latitude,

        @NotNull(message = "Longitude e obrigatoria")
        Double longitude,

        @NotNull(message = "Populacao e obrigatoria")
        @PositiveOrZero(message = "Populacao nao pode ser negativa")
        Integer populacao,

        @NotNull(message = "Tipo de risco e obrigatorio")
        TipoRisco tipoRiscoPrincipal,

        @Min(value = 0, message = "Indice minimo e 0")
        @Max(value = 100, message = "Indice maximo e 100")
        Integer indiceVulnerabilidade
) {
    public static RegiaoMonitoradaDTO from(RegiaoMonitorada regiao) {
        return new RegiaoMonitoradaDTO(
                regiao.getId(),
                regiao.getNome(),
                regiao.getCidade(),
                regiao.getEstado(),
                regiao.getLatitude(),
                regiao.getLongitude(),
                regiao.getPopulacao(),
                regiao.getTipoRiscoPrincipal(),
                regiao.getIndiceVulnerabilidade()
        );
    }
}
