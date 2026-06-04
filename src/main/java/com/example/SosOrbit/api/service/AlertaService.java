package com.example.SosOrbit.api.service;

import com.example.SosOrbit.api.messaging.AlertaEventPublisher;
import com.example.SosOrbit.api.dto.AlertaDTO;
import com.example.SosOrbit.api.exception.ResourceNotFoundException;
import com.example.SosOrbit.api.model.Alerta;
import com.example.SosOrbit.api.model.NivelRisco;
import com.example.SosOrbit.api.model.StatusAlerta;
import com.example.SosOrbit.api.repository.AlertaRepository;
import com.example.SosOrbit.api.repository.RecursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository repository;
    private final RecursoRepository recursoRepository;
    private final AlertaEventPublisher alertaEventPublisher;

    public AlertaService(AlertaRepository repository,
                         RecursoRepository recursoRepository,
                         AlertaEventPublisher alertaEventPublisher) {
        this.repository = repository;
        this.recursoRepository = recursoRepository;
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
        Alerta alerta = buscarPorId(id);
        alerta.setStatus(status);
        Alerta alertaSalvo = repository.save(alerta);
        alertaEventPublisher.publicarStatusAtualizado(alertaSalvo);
        return alertaSalvo;
    }

    public Alerta atualizar(Long id, AlertaDTO dto) {
        Alerta alerta = buscarPorId(id);
        StatusAlerta statusAnterior = alerta.getStatus();
        alerta.setTitulo(dto.titulo());
        alerta.setMensagem(dto.mensagem());
        alerta.setIndiceRisco(dto.indiceRisco());
        alerta.setNivelRisco(dto.nivelRisco());
        alerta.setStatus(dto.status() == null ? StatusAlerta.ABERTO : dto.status());
        Alerta alertaSalvo = repository.save(alerta);

        if (statusAnterior != alertaSalvo.getStatus()) {
            alertaEventPublisher.publicarStatusAtualizado(alertaSalvo);
        }

        return alertaSalvo;
    }

    @Transactional
    public void deletar(Long id) {
        Alerta alerta = buscarPorId(id);
        recursoRepository.deleteByAlertaId(alerta.getId());
        repository.delete(alerta);
    }

    private Alerta buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta nao encontrado"));
    }
}
