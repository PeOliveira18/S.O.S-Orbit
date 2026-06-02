package com.example.SosOrbit.api.risco;

public class CalculadoraRiscoDeslizamento extends CalculadoraRisco {

    @Override
    protected double calcularIndice(RiscoContexto contexto) {
        double indice = 0;
        indice += limitar(contexto.getChuvaPrevistaMm() * 0.40, 40);
        indice += limitar(contexto.getHistoricoOcorrencias() * 2, 20);
        indice += limitar(contexto.getIndiceVulnerabilidade() * 0.30, 30);
        indice += limitar(contexto.getUmidade() * 0.10, 10);
        return indice;
    }
}
