package com.example.SosOrbit.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_REGIAO_MONITORADA")
public class RegiaoMonitorada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome e obrigatorio")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Cidade e obrigatoria")
    @Column(nullable = false, length = 80)
    private String cidade;

    @NotBlank(message = "Estado e obrigatorio")
    @Column(nullable = false, length = 2)
    private String estado;

    @NotNull(message = "Latitude e obrigatoria")
    private Double latitude;

    @NotNull(message = "Longitude e obrigatoria")
    private Double longitude;

    @NotNull(message = "Populacao e obrigatoria")
    @PositiveOrZero(message = "Populacao nao pode ser negativa")
    private Integer populacao;

    @NotNull(message = "Tipo de risco e obrigatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoRisco tipoRiscoPrincipal;

    @Min(value = 0, message = "Indice minimo e 0")
    @Max(value = 100, message = "Indice maximo e 100")
    private Integer indiceVulnerabilidade;
}
