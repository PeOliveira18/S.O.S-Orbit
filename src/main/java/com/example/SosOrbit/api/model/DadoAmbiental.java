package com.example.SosOrbit.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_DADO_AMBIENTAL")
public class DadoAmbiental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PositiveOrZero(message = "Chuva prevista nao pode ser negativa")
    private Double chuvaPrevistaMm;

    private Double temperatura;

    @Min(value = 0, message = "Umidade minima e 0")
    @Max(value = 100, message = "Umidade maxima e 100")
    private Integer umidade;

    @PositiveOrZero(message = "Velocidade do vento nao pode ser negativa")
    private Double velocidadeVentoKmH;

    @PositiveOrZero(message = "Nivel do rio nao pode ser negativo")
    private Double nivelRioMetros;

    @PositiveOrZero(message = "Focos de calor nao pode ser negativo")
    private Integer focosCalor;

    @PositiveOrZero(message = "Historico nao pode ser negativo")
    private Integer historicoOcorrencias;

    private Double indiceRisco;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private NivelRisco nivelRisco;

    private LocalDateTime dataRegistro;

    @ManyToOne
    @JoinColumn(name = "regiao_id")
    private RegiaoMonitorada regiao;
}
