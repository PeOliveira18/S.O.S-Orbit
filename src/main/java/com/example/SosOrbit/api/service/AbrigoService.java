package com.example.SosOrbit.api.service;

import com.example.SosOrbit.api.dto.AbrigoDTO;
import com.example.SosOrbit.api.model.Abrigo;
import com.example.SosOrbit.api.repository.AbrigoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AbrigoService {

    private final AbrigoRepository repository;

    public AbrigoService(AbrigoRepository repository) {
        this.repository = repository;
    }

    public List<Abrigo> listar() {
        return repository.findAll();
    }

    public List<Abrigo> listarAtivos() {
        return repository.findByAtivoTrue();
    }

    public List<Abrigo> buscarPorCidade(String cidade) {
        return repository.findByCidadeContainingIgnoreCaseAndAtivoTrue(cidade);
    }

    public Abrigo salvar(AbrigoDTO dto) {
        return repository.save(converterDto(dto));
    }

    private Abrigo converterDto(AbrigoDTO dto) {
        Abrigo abrigo = new Abrigo();
        abrigo.setNome(dto.nome());
        abrigo.setEndereco(dto.endereco());
        abrigo.setCidade(dto.cidade());
        abrigo.setEstado(dto.estado());
        abrigo.setLatitude(dto.latitude());
        abrigo.setLongitude(dto.longitude());
        abrigo.setCapacidade(dto.capacidade());
        abrigo.setVagasDisponiveis(dto.vagasDisponiveis());
        abrigo.setAtivo(dto.ativo() == null ? true : dto.ativo());
        return abrigo;
    }
}
