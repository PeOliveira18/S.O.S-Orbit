package com.example.SosOrbit.api.risco;

public class CalculadoraRiscoEnchente extends CalculadoraRisco {

    @Override
    protected double calcularIndice(RiscoContexto contexto) {
        double indice = 0;
        indice += limitar(contexto.getChuvaPrevistaMm() * 0.35, 35);
        indice += limitar(contexto.getNivelRioMetros() * 5, 25);
        indice += limitar(contexto.getHistoricoOcorrencias() * 1.5, 12);
        indice += limitar(contexto.getIndiceVulnerabilidade() * 0.25, 25);
        indice += limitar(contexto.getUmidade() * 0.03, 3);
        return indice;
    }
}
