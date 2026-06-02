package com.example.SosOrbit.api.risco;

import com.example.SosOrbit.api.model.NivelRisco;

public class ResultadoRisco {

    private final double indiceRisco;
    private final NivelRisco nivelRisco;
    private final String recomendacao;

    public ResultadoRisco(double indiceRisco, NivelRisco nivelRisco, String recomendacao) {
        this.indiceRisco = indiceRisco;
        this.nivelRisco = nivelRisco;
        this.recomendacao = recomendacao;
    }

    public double getIndiceRisco() {
        return indiceRisco;
    }

    public NivelRisco getNivelRisco() {
        return nivelRisco;
    }

    public String getRecomendacao() {
        return recomendacao;
    }

    public boolean isAlertaNecessario() {
        return nivelRisco == NivelRisco.ALTO || nivelRisco == NivelRisco.CRITICO;
    }
}
