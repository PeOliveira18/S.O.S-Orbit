package com.example.SosOrbit.api.dto;

import com.example.SosOrbit.api.model.DadoAmbiental;
import com.example.SosOrbit.api.model.NivelRisco;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record DadoAmbientalDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @PositiveOrZero(message = "Chuva prevista nao pode ser negativa")
        Double chuvaPrevistaMm,

        Double temperatura,

        @Min(value = 0, message = "Umidade minima e 0")
        @Max(value = 100, message = "Umidade maxima e 100")
        Integer umidade,

        @PositiveOrZero(message = "Velocidade do vento nao pode ser negativa")
        Double velocidadeVentoKmH,

        @PositiveOrZero(message = "Nivel do rio nao pode ser negativo")
        Double nivelRioMetros,

        @PositiveOrZero(message = "Focos de calor nao pode ser negativo")
        Integer focosCalor,

        @PositiveOrZero(message = "Historico nao pode ser negativo")
        Integer historicoOcorrencias,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Double indiceRisco,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        NivelRisco nivelRisco,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime dataRegistro,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long regiaoId
) {
    public static DadoAmbientalDTO from(DadoAmbiental dado) {
        Long regiaoId = dado.getRegiao() == null ? null : dado.getRegiao().getId();

        return new DadoAmbientalDTO(
                dado.getId(),
                dado.getChuvaPrevistaMm(),
                dado.getTemperatura(),
                dado.getUmidade(),
                dado.getVelocidadeVentoKmH(),
                dado.getNivelRioMetros(),
                dado.getFocosCalor(),
                dado.getHistoricoOcorrencias(),
                dado.getIndiceRisco(),
                dado.getNivelRisco(),
                dado.getDataRegistro(),
                regiaoId
        );
    }
}
