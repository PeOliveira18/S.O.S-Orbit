package com.example.SosOrbit.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_RECURSO")
public class Recurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Tipo do recurso e obrigatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoRecurso tipo;

    @NotBlank(message = "Descricao e obrigatoria")
    @Column(nullable = false, length = 160)
    private String descricao;

    @Positive(message = "Quantidade deve ser maior que zero")
    private Integer quantidade;

    @Column(length = 30)
    private String unidadeMedida;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioridadeRecurso prioridade = PrioridadeRecurso.MEDIA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusRecurso status = StatusRecurso.DISPONIVEL;

    @ManyToOne
    @JoinColumn(name = "alerta_id")
    private Alerta alerta;
}
