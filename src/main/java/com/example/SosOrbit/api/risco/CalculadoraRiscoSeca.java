package com.example.SosOrbit.api.risco;

public class CalculadoraRiscoSeca extends CalculadoraRisco {

    @Override
    protected double calcularIndice(RiscoContexto contexto) {
        double indice = 0;
        indice += limitar((100 - contexto.getUmidade()) * 0.35, 30);
        indice += limitar(Math.max(contexto.getTemperatura() - 25, 0) * 1.5, 15);
        indice += contexto.getChuvaPrevistaMm() < 10 ? 25 : limitar((30 - contexto.getChuvaPrevistaMm()) * 0.5, 15);
        indice += limitar(contexto.getHistoricoOcorrencias() * 1.2, 10);
        indice += limitar(contexto.getIndiceVulnerabilidade() * 0.20, 20);
        return indice;
    }
}
