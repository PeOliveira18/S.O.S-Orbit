package com.example.SosOrbit.api.risco;

public class CalculadoraRiscoQueimada extends CalculadoraRisco {

    @Override
    protected double calcularIndice(RiscoContexto contexto) {
        double indice = 0;
        indice += limitar(contexto.getFocosCalor() * 5, 35);
        indice += limitar((100 - contexto.getUmidade()) * 0.25, 20);
        indice += limitar(contexto.getVelocidadeVentoKmH() * 0.20, 15);
        indice += limitar(Math.max(contexto.getTemperatura() - 30, 0) * 1.2, 12);
        indice += limitar(contexto.getIndiceVulnerabilidade() * 0.18, 18);
        return indice;
    }
}
