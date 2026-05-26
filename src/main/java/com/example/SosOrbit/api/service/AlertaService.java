package com.example.SosOrbit.api.service;

import com.example.SosOrbit.api.model.Alerta;
import com.example.SosOrbit.api.model.NivelRisco;
import com.example.SosOrbit.api.model.StatusAlerta;
import com.example.SosOrbit.api.repository.AlertaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository repository;

    public AlertaService(AlertaRepository repository) {
        this.repository = repository;
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
                .orElseThrow(() -> new RuntimeException("Alerta nao encontrado"));
        alerta.setStatus(status);
        return repository.save(alerta);
    }
}
