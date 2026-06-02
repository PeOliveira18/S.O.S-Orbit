package com.example.SosOrbit.api.config;

import com.example.SosOrbit.soap.service.RiscoOrbitalSoapService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.xml.ws.Endpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SoapPublisherConfig {

    @Value("${app.soap.risco-orbital-publish-url:http://0.0.0.0:8081/risco-orbital}")
    private String publishUrl;

    private Endpoint endpoint;

    @PostConstruct
    public void publicar() {
        endpoint = Endpoint.publish(publishUrl, new RiscoOrbitalSoapService());
        System.out.println("SOAP WebService S.O.S Orbit publicado em: " + publishUrl + "?wsdl");
    }

    @PreDestroy
    public void parar() {
        if (endpoint != null) {
            endpoint.stop();
        }
    }
}
