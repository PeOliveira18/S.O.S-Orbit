package com.example.SosOrbit.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ALERTA")
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Titulo e obrigatorio")
    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(length = 500)
    private String mensagem;

    private Double indiceRisco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NivelRisco nivelRisco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusAlerta status = StatusAlerta.ABERTO;

    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "regiao_id")
    private RegiaoMonitorada regiao;

    @ManyToOne
    @JoinColumn(name = "dado_ambiental_id")
    private DadoAmbiental dadoAmbiental;
}
