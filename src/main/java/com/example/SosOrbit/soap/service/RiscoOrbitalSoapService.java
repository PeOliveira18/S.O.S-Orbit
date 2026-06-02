package com.example.SosOrbit.soap.service;

import com.example.SosOrbit.api.model.TipoRisco;
import com.example.SosOrbit.api.risco.CalculadoraRisco;
import com.example.SosOrbit.api.risco.CalculadoraRiscoFactory;
import com.example.SosOrbit.api.risco.ResultadoRisco;
import com.example.SosOrbit.api.risco.RiscoContexto;
import com.example.SosOrbit.soap.model.RiscoOrbitalRequest;
import com.example.SosOrbit.soap.model.RiscoOrbitalResponse;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebService(
        serviceName = "RiscoOrbitalService",
        portName = "RiscoOrbitalPort",
        targetNamespace = "http://soap.sosorbit.com/"
)
public class RiscoOrbitalSoapService {

    private static final Map<Long, RiscoOrbitalResponse> ULTIMOS_RISCOS = new ConcurrentHashMap<>();

    @WebMethod
    public RiscoOrbitalResponse consultarRiscoPorRegiao(@WebParam(name = "regiaoId") Long regiaoId) {
        return ULTIMOS_RISCOS.getOrDefault(
                regiaoId,
                new RiscoOrbitalResponse(regiaoId, 0.0, "BAIXO", false, "Regiao ainda sem processamento SOAP")
        );
    }

    @WebMethod
    public RiscoOrbitalResponse processarDadoOrbital(@WebParam(name = "request") RiscoOrbitalRequest request) {
        TipoRisco tipoRisco = converterTipoRisco(request.getTipoRisco());
        RiscoContexto contexto = new RiscoContexto(
                tipoRisco,
                valor(request.getChuvaPrevistaMm()),
                valor(request.getTemperatura()),
                valor(request.getUmidade()),
                valor(request.getVelocidadeVentoKmH()),
                valor(request.getNivelRioMetros()),
                valor(request.getFocosCalor()),
                valor(request.getHistoricoOcorrencias()),
                valor(request.getIndiceVulnerabilidade())
        );

        CalculadoraRisco calculadora = CalculadoraRiscoFactory.obter(tipoRisco);
        ResultadoRisco resultado = calculadora.calcular(contexto);

        RiscoOrbitalResponse response = new RiscoOrbitalResponse(
                request.getRegiaoId(),
                resultado.getIndiceRisco(),
                resultado.getNivelRisco().name(),
                resultado.isAlertaNecessario(),
                resultado.getRecomendacao()
        );

        if (request.getRegiaoId() != null) {
            ULTIMOS_RISCOS.put(request.getRegiaoId(), response);
        }

        return response;
    }

    private TipoRisco converterTipoRisco(String tipoRisco) {
        try {
            return TipoRisco.valueOf(tipoRisco);
        } catch (RuntimeException exception) {
            return TipoRisco.ENCHENTE;
        }
    }

    private double valor(Number numero) {
        return numero == null ? 0 : numero.doubleValue();
    }

    private int valor(Integer numero) {
        return numero == null ? 0 : numero;
    }
}
