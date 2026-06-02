package com.example.SosOrbit.api.service;

import com.example.SosOrbit.api.messaging.AlertaEventPublisher;
import com.example.SosOrbit.api.exception.ResourceNotFoundException;
import com.example.SosOrbit.api.model.Alerta;
import com.example.SosOrbit.api.model.NivelRisco;
import com.example.SosOrbit.api.model.StatusAlerta;
import com.example.SosOrbit.api.repository.AlertaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository repository;
    private final AlertaEventPublisher alertaEventPublisher;

    public AlertaService(AlertaRepository repository, AlertaEventPublisher alertaEventPublisher) {
        this.repository = repository;
        this.alertaEventPublisher = alertaEventPublisher;
    }

    public List<Alerta> listar() {
        return repository.findAll();
    }

    public List<Alerta> listarAbertos() {
        return repository.findByStatus(StatusAlerta.ABERTO);
    }

    public List<Alerta> listarPorNivel(NivelRisco nivelRisco) {
        return repository.findByNivelRisco(nivelRisco);
    }

    public Alerta atualizarStatus(Long id, StatusAlerta status) {
        Alerta alerta = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta nao encontrado"));
        alerta.setStatus(status);
        Alerta alertaSalvo = repository.save(alerta);
        alertaEventPublisher.publicarStatusAtualizado(alertaSalvo);
        return alertaSalvo;
    }
}
