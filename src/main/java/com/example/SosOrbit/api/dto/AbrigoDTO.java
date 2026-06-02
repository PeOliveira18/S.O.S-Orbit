package com.example.SosOrbit.api.dto;

import com.example.SosOrbit.api.model.Abrigo;
import com.example.SosOrbit.api.validation.VagasDentroDaCapacidade;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

@VagasDentroDaCapacidade
public record AbrigoDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        Long id,

        @NotBlank(message = "Nome e obrigatorio")
        @Size(max = 100, message = "Nome deve ter no maximo 100 caracteres")
        String nome,

        @NotBlank(message = "Endereco e obrigatorio")
        @Size(max = 160, message = "Endereco deve ter no maximo 160 caracteres")
        String endereco,

        @NotBlank(message = "Cidade e obrigatoria")
        @Size(max = 80, message = "Cidade deve ter no maximo 80 caracteres")
        String cidade,

        @NotBlank(message = "Estado e obrigatorio")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres")
        String estado,

        Double latitude,
        Double longitude,

        @Positive(message = "Capacidade deve ser maior que zero")
        Integer capacidade,

        @PositiveOrZero(message = "Vagas disponiveis nao pode ser negativo")
        Integer vagasDisponiveis,

        Boolean ativo
) {
    public static AbrigoDTO from(Abrigo abrigo) {
        return new AbrigoDTO(
                abrigo.getId(),
                abrigo.getNome(),
                abrigo.getEndereco(),
                abrigo.getCidade(),
                abrigo.getEstado(),
                abrigo.getLatitude(),
                abrigo.getLongitude(),
                abrigo.getCapacidade(),
                abrigo.getVagasDisponiveis(),
                abrigo.getAtivo()
        );
    }
}
