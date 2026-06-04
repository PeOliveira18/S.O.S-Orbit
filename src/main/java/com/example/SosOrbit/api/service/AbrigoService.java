package com.example.SosOrbit.api.service;

import com.example.SosOrbit.api.dto.AbrigoDTO;
import com.example.SosOrbit.api.exception.ResourceNotFoundException;
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

    public Abrigo atualizar(Long id, AbrigoDTO dto) {
        Abrigo abrigo = buscarPorId(id);
        preencherDados(abrigo, dto);
        return repository.save(abrigo);
    }

    public void deletar(Long id) {
        Abrigo abrigo = buscarPorId(id);
        repository.delete(abrigo);
    }

    private Abrigo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Abrigo nao encontrado"));
    }

    private Abrigo converterDto(AbrigoDTO dto) {
        Abrigo abrigo = new Abrigo();
        preencherDados(abrigo, dto);
        return abrigo;
    }

    private void preencherDados(Abrigo abrigo, AbrigoDTO dto) {
        abrigo.setNome(dto.nome());
        abrigo.setEndereco(dto.endereco());
        abrigo.setCidade(dto.cidade());
        abrigo.setEstado(dto.estado());
        abrigo.setLatitude(dto.latitude());
        abrigo.setLongitude(dto.longitude());
        abrigo.setCapacidade(dto.capacidade());
        abrigo.setVagasDisponiveis(dto.vagasDisponiveis());
        abrigo.setAtivo(dto.ativo() == null ? true : dto.ativo());
    }
}
