package com.example.SosOrbit.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_ABRIGO")
public class Abrigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome e obrigatorio")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "Endereco e obrigatorio")
    @Column(nullable = false, length = 160)
    private String endereco;

    @NotBlank(message = "Cidade e obrigatoria")
    @Column(nullable = false, length = 80)
    private String cidade;

    @NotBlank(message = "Estado e obrigatorio")
    @Column(nullable = false, length = 2)
    private String estado;

    private Double latitude;
    private Double longitude;

    @Positive(message = "Capacidade deve ser maior que zero")
    private Integer capacidade;

    @PositiveOrZero(message = "Vagas disponiveis nao pode ser negativo")
    private Integer vagasDisponiveis;

    @Column(nullable = false)
    private Boolean ativo = true;
}
