package com.example.SosOrbit.soap.publisher;

import com.example.SosOrbit.soap.service.RiscoOrbitalSoapService;
import jakarta.xml.ws.Endpoint;

public class SoapServicePublisher {

    public static final String DEFAULT_URL = "http://0.0.0.0:8081/risco-orbital";

    public static void main(String[] args) {
        Endpoint.publish(DEFAULT_URL, new RiscoOrbitalSoapService());
        System.out.println("SOAP WebService rodando em:");
        System.out.println(DEFAULT_URL + "?wsdl");
    }
}
