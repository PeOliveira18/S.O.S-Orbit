package com.example.SosOrbit.api.service;

import com.example.SosOrbit.api.dto.RecursoDTO;
import com.example.SosOrbit.api.exception.ResourceNotFoundException;
import com.example.SosOrbit.api.model.Alerta;
import com.example.SosOrbit.api.model.PrioridadeRecurso;
import com.example.SosOrbit.api.model.Recurso;
import com.example.SosOrbit.api.model.StatusRecurso;
import com.example.SosOrbit.api.repository.AlertaRepository;
import com.example.SosOrbit.api.repository.RecursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecursoService {

    private final RecursoRepository recursoRepository;
    private final AlertaRepository alertaRepository;

    public RecursoService(RecursoRepository recursoRepository, AlertaRepository alertaRepository) {
        this.recursoRepository = recursoRepository;
        this.alertaRepository = alertaRepository;
    }

    public List<Recurso> listar() {
        return recursoRepository.findAll();
    }

    public List<Recurso> listarPorAlerta(Long alertaId) {
        return recursoRepository.findByAlertaId(alertaId);
    }

    public List<Recurso> listarPorPrioridade(PrioridadeRecurso prioridade) {
        return recursoRepository.findByPrioridade(prioridade);
    }

    public Recurso salvar(Long alertaId, RecursoDTO dto) {
        Alerta alerta = alertaRepository.findById(alertaId)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta nao encontrado"));
        Recurso recurso = converterDto(dto);
        recurso.setAlerta(alerta);
        return recursoRepository.save(recurso);
    }

    public Recurso atualizar(Long id, RecursoDTO dto) {
        Recurso recurso = buscarPorId(id);
        preencherDados(recurso, dto);
        return recursoRepository.save(recurso);
    }

    public Recurso atualizarStatus(Long id, StatusRecurso status) {
        Recurso recurso = buscarPorId(id);
        recurso.setStatus(status);
        return recursoRepository.save(recurso);
    }

    @Transactional
    public void deletar(Long id) {
        Recurso recurso = buscarPorId(id);
        recursoRepository.delete(recurso);
    }

    private Recurso converterDto(RecursoDTO dto) {
        Recurso recurso = new Recurso();
        preencherDados(recurso, dto);
        return recurso;
    }

    private void preencherDados(Recurso recurso, RecursoDTO dto) {
        recurso.setTipo(dto.tipo());
        recurso.setDescricao(dto.descricao());
        recurso.setQuantidade(dto.quantidade());
        recurso.setUnidadeMedida(dto.unidadeMedida());
        recurso.setPrioridade(dto.prioridade() == null ? PrioridadeRecurso.MEDIA : dto.prioridade());
        recurso.setStatus(dto.status() == null ? StatusRecurso.DISPONIVEL : dto.status());
    }

    private Recurso buscarPorId(Long id) {
        return recursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso nao encontrado"));
    }
}
