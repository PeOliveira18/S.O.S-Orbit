package com.example.SosOrbit.soap.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RiscoOrbitalResponse {

    private Long regiaoId;
    private Double indiceRisco;
    private String nivelRisco;
    private Boolean alertaNecessario;
    private String recomendacao;

    public RiscoOrbitalResponse() {
    }

    public RiscoOrbitalResponse(Long regiaoId,
                                Double indiceRisco,
                                String nivelRisco,
                                Boolean alertaNecessario,
                                String recomendacao) {
        this.regiaoId = regiaoId;
        this.indiceRisco = indiceRisco;
        this.nivelRisco = nivelRisco;
        this.alertaNecessario = alertaNecessario;
        this.recomendacao = recomendacao;
    }

    public Long getRegiaoId() {
        return regiaoId;
    }

    public void setRegiaoId(Long regiaoId) {
        this.regiaoId = regiaoId;
    }

    public Double getIndiceRisco() {
        return indiceRisco;
    }

    public void setIndiceRisco(Double indiceRisco) {
        this.indiceRisco = indiceRisco;
    }

    public String getNivelRisco() {
        return nivelRisco;
    }

    public void setNivelRisco(String nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public Boolean getAlertaNecessario() {
        return alertaNecessario;
    }

    public void setAlertaNecessario(Boolean alertaNecessario) {
        this.alertaNecessario = alertaNecessario;
    }

    public String getRecomendacao() {
        return recomendacao;
    }

    public void setRecomendacao(String recomendacao) {
        this.recomendacao = recomendacao;
    }
}
