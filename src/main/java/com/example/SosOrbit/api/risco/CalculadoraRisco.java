package com.example.SosOrbit.api.risco;

import com.example.SosOrbit.api.model.NivelRisco;

public abstract class CalculadoraRisco {

    public ResultadoRisco calcular(RiscoContexto contexto) {
        double indice = Math.min(calcularIndice(contexto), 100);
        NivelRisco nivel = classificarNivel(indice);
        return new ResultadoRisco(indice, nivel, gerarRecomendacao(nivel));
    }

    protected abstract double calcularIndice(RiscoContexto contexto);

    protected double limitar(double valor, double limite) {
        return Math.min(Math.max(valor, 0), limite);
    }

    protected NivelRisco classificarNivel(double indice) {
        if (indice >= 80) return NivelRisco.CRITICO;
        if (indice >= 60) return NivelRisco.ALTO;
        if (indice >= 30) return NivelRisco.MEDIO;
        return NivelRisco.BAIXO;
    }

    protected String gerarRecomendacao(NivelRisco nivel) {
        return switch (nivel) {
            case CRITICO -> "Acionar resposta imediata, priorizar evacuacao e recursos essenciais";
            case ALTO -> "Preparar equipes, verificar abrigos e monitorar a regiao continuamente";
            case MEDIO -> "Manter monitoramento preventivo e validar rotas seguras";
            case BAIXO -> "Sem acao emergencial, manter acompanhamento normal";
        };
    }
}
