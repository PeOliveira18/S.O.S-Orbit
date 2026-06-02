package com.example.SosOrbit.api.risco;

import com.example.SosOrbit.api.model.TipoRisco;

public class RiscoContexto {

    private final TipoRisco tipoRisco;
    private final double chuvaPrevistaMm;
    private final double temperatura;
    private final int umidade;
    private final double velocidadeVentoKmH;
    private final double nivelRioMetros;
    private final int focosCalor;
    private final int historicoOcorrencias;
    private final int indiceVulnerabilidade;

    public RiscoContexto(TipoRisco tipoRisco,
                         double chuvaPrevistaMm,
                         double temperatura,
                         int umidade,
                         double velocidadeVentoKmH,
                         double nivelRioMetros,
                         int focosCalor,
                         int historicoOcorrencias,
                         int indiceVulnerabilidade) {
        this.tipoRisco = tipoRisco;
        this.chuvaPrevistaMm = chuvaPrevistaMm;
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.velocidadeVentoKmH = velocidadeVentoKmH;
        this.nivelRioMetros = nivelRioMetros;
        this.focosCalor = focosCalor;
        this.historicoOcorrencias = historicoOcorrencias;
        this.indiceVulnerabilidade = indiceVulnerabilidade;
    }

    public TipoRisco getTipoRisco() {
        return tipoRisco;
    }

    public double getChuvaPrevistaMm() {
        return chuvaPrevistaMm;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public int getUmidade() {
        return umidade;
    }

    public double getVelocidadeVentoKmH() {
        return velocidadeVentoKmH;
    }

    public double getNivelRioMetros() {
        return nivelRioMetros;
    }

    public int getFocosCalor() {
        return focosCalor;
    }

    public int getHistoricoOcorrencias() {
        return historicoOcorrencias;
    }

    public int getIndiceVulnerabilidade() {
        return indiceVulnerabilidade;
    }
}
