package com.example.SosOrbit.soap.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RiscoOrbitalRequest {

    private Long regiaoId;
    private String tipoRisco;
    private Double chuvaPrevistaMm;
    private Double temperatura;
    private Integer umidade;
    private Double velocidadeVentoKmH;
    private Double nivelRioMetros;
    private Integer focosCalor;
    private Integer historicoOcorrencias;
    private Integer indiceVulnerabilidade;

    public RiscoOrbitalRequest() {
    }

    public Long getRegiaoId() {
        return regiaoId;
    }

    public void setRegiaoId(Long regiaoId) {
        this.regiaoId = regiaoId;
    }

    public String getTipoRisco() {
        return tipoRisco;
    }

    public void setTipoRisco(String tipoRisco) {
        this.tipoRisco = tipoRisco;
    }

    public Double getChuvaPrevistaMm() {
        return chuvaPrevistaMm;
    }

    public void setChuvaPrevistaMm(Double chuvaPrevistaMm) {
        this.chuvaPrevistaMm = chuvaPrevistaMm;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getUmidade() {
        return umidade;
    }

    public void setUmidade(Integer umidade) {
        this.umidade = umidade;
    }

    public Double getVelocidadeVentoKmH() {
        return velocidadeVentoKmH;
    }

    public void setVelocidadeVentoKmH(Double velocidadeVentoKmH) {
        this.velocidadeVentoKmH = velocidadeVentoKmH;
    }

    public Double getNivelRioMetros() {
        return nivelRioMetros;
    }

    public void setNivelRioMetros(Double nivelRioMetros) {
        this.nivelRioMetros = nivelRioMetros;
    }

    public Integer getFocosCalor() {
        return focosCalor;
    }

    public void setFocosCalor(Integer focosCalor) {
        this.focosCalor = focosCalor;
    }

    public Integer getHistoricoOcorrencias() {
        return historicoOcorrencias;
    }

    public void setHistoricoOcorrencias(Integer historicoOcorrencias) {
        this.historicoOcorrencias = historicoOcorrencias;
    }

    public Integer getIndiceVulnerabilidade() {
        return indiceVulnerabilidade;
    }

    public void setIndiceVulnerabilidade(Integer indiceVulnerabilidade) {
        this.indiceVulnerabilidade = indiceVulnerabilidade;
    }
}
