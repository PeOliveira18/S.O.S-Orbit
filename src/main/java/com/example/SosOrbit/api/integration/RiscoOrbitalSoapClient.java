package com.example.SosOrbit.api.integration;

import com.example.SosOrbit.api.exception.ExternalServiceException;
import com.example.SosOrbit.api.model.DadoAmbiental;
import com.example.SosOrbit.api.model.RegiaoMonitorada;
import com.example.SosOrbit.soap.model.RiscoOrbitalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class RiscoOrbitalSoapClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String soapUrl;

    public RiscoOrbitalSoapClient(
            @Value("${app.soap.risco-orbital-url:http://localhost:8081/risco-orbital}") String soapUrl
    ) {
        this.soapUrl = soapUrl;
    }

    public RiscoOrbitalResponse processarDadoOrbital(RegiaoMonitorada regiao, DadoAmbiental dado) {
        try {
            String envelope = montarEnvelope(regiao, dado);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(soapUrl))
                    .header("Content-Type", "text/xml;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(envelope))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new ExternalServiceException("Erro ao consumir SOAP de risco orbital");
            }

            return lerResposta(response.body());
        } catch (ExternalServiceException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ExternalServiceException("Falha na integracao REST com SOAP: " + exception.getMessage(), exception);
        }
    }

    private String montarEnvelope(RegiaoMonitorada regiao, DadoAmbiental dado) {
        return """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:soap="http://soap.sosorbit.com/">
                   <soapenv:Header/>
                   <soapenv:Body>
                      <soap:processarDadoOrbital>
                         <request>
                            <regiaoId>%d</regiaoId>
                            <tipoRisco>%s</tipoRisco>
                            <chuvaPrevistaMm>%s</chuvaPrevistaMm>
                            <temperatura>%s</temperatura>
                            <umidade>%s</umidade>
                            <velocidadeVentoKmH>%s</velocidadeVentoKmH>
                            <nivelRioMetros>%s</nivelRioMetros>
                            <focosCalor>%s</focosCalor>
                            <historicoOcorrencias>%s</historicoOcorrencias>
                            <indiceVulnerabilidade>%s</indiceVulnerabilidade>
                         </request>
                      </soap:processarDadoOrbital>
                   </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(
                regiao.getId(),
                regiao.getTipoRiscoPrincipal().name(),
                valor(dado.getChuvaPrevistaMm()),
                valor(dado.getTemperatura()),
                valor(dado.getUmidade()),
                valor(dado.getVelocidadeVentoKmH()),
                valor(dado.getNivelRioMetros()),
                valor(dado.getFocosCalor()),
                valor(dado.getHistoricoOcorrencias()),
                valor(regiao.getIndiceVulnerabilidade())
        );
    }

    private RiscoOrbitalResponse lerResposta(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        Document document = factory.newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        RiscoOrbitalResponse response = new RiscoOrbitalResponse();
        response.setRegiaoId(Long.valueOf(texto(document, "regiaoId")));
        response.setIndiceRisco(Double.valueOf(texto(document, "indiceRisco")));
        response.setNivelRisco(texto(document, "nivelRisco"));
        response.setAlertaNecessario(Boolean.valueOf(texto(document, "alertaNecessario")));
        response.setRecomendacao(texto(document, "recomendacao"));
        return response;
    }

    private String texto(Document document, String tag) {
        if (document.getElementsByTagName(tag).getLength() == 0) {
            throw new ExternalServiceException("Resposta SOAP nao possui o campo " + tag);
        }
        return document.getElementsByTagName(tag).item(0).getTextContent();
    }

    private String valor(Number numero) {
        return String.valueOf(numero == null ? 0 : numero);
    }
}
