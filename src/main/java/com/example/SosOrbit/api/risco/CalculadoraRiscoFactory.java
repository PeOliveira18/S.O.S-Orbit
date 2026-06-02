package com.example.SosOrbit.api.risco;

import com.example.SosOrbit.api.model.TipoRisco;

public class CalculadoraRiscoFactory {

    private CalculadoraRiscoFactory() {
    }

    public static CalculadoraRisco obter(TipoRisco tipoRisco) {
        return switch (tipoRisco) {
            case ENCHENTE -> new CalculadoraRiscoEnchente();
            case QUEIMADA -> new CalculadoraRiscoQueimada();
            case DESLIZAMENTO -> new CalculadoraRiscoDeslizamento();
            case SECA -> new CalculadoraRiscoSeca();
        };
    }
}
