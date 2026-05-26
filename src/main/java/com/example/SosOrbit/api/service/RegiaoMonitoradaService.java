package com.example.SosOrbit.api.service;

import com.example.SosOrbit.api.dto.RegiaoMonitoradaDTO;
import com.example.SosOrbit.api.model.RegiaoMonitorada;
import com.example.SosOrbit.api.model.TipoRisco;
import com.example.SosOrbit.api.repository.RegiaoMonitoradaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegiaoMonitoradaService {

    private final RegiaoMonitoradaRepository repository;

    public RegiaoMonitoradaService(RegiaoMonitoradaRepository repository) {
        this.repository = repository;
    }

    public List<RegiaoMonitorada> listar() {
        return repository.findAll();
    }

    public RegiaoMonitorada buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Regiao nao encontrada"));
    }

    public List<RegiaoMonitorada> buscarPorCidade(String cidade) {
        return repository.findByCidadeContainingIgnoreCase(cidade);
    }

    public List<RegiaoMonitorada> buscarPorTipoRisco(TipoRisco tipoRisco) {
        return repository.findByTipoRiscoPrincipal(tipoRisco);
    }

    public RegiaoMonitorada salvar(RegiaoMonitoradaDTO dto) {
        return repository.save(converterDto(dto));
    }

    public RegiaoMonitorada atualizar(Long id, RegiaoMonitoradaDTO dto) {
        RegiaoMonitorada encontrada = buscarPorId(id);
        encontrada.setNome(dto.nome());
        encontrada.setCidade(dto.cidade());
        encontrada.setEstado(dto.estado());
        encontrada.setLatitude(dto.latitude());
        encontrada.setLongitude(dto.longitude());
        encontrada.setPopulacao(dto.populacao());
        encontrada.setTipoRiscoPrincipal(dto.tipoRiscoPrincipal());
        encontrada.setIndiceVulnerabilidade(dto.indiceVulnerabilidade());
        return repository.save(encontrada);
    }

    private RegiaoMonitorada converterDto(RegiaoMonitoradaDTO dto) {
        RegiaoMonitorada regiao = new RegiaoMonitorada();
        regiao.setNome(dto.nome());
        regiao.setCidade(dto.cidade());
        regiao.setEstado(dto.estado());
        regiao.setLatitude(dto.latitude());
        regiao.setLongitude(dto.longitude());
        regiao.setPopulacao(dto.populacao());
        regiao.setTipoRiscoPrincipal(dto.tipoRiscoPrincipal());
        regiao.setIndiceVulnerabilidade(dto.indiceVulnerabilidade());
        return regiao;
    }

    public void deletar(Long id) {
        RegiaoMonitorada regiao = buscarPorId(id);
        repository.delete(regiao);
    }
}
