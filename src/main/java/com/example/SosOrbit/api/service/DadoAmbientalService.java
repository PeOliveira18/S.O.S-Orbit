package com.example.SosOrbit.api.service;

import com.example.SosOrbit.api.dto.DadoAmbientalDTO;
import com.example.SosOrbit.api.exception.ResourceNotFoundException;
import com.example.SosOrbit.api.integration.RiscoOrbitalSoapClient;
import com.example.SosOrbit.api.messaging.AlertaEventPublisher;
import com.example.SosOrbit.api.model.*;
import com.example.SosOrbit.api.repository.AlertaRepository;
import com.example.SosOrbit.api.repository.DadoAmbientalRepository;
import com.example.SosOrbit.api.repository.RecursoRepository;
import com.example.SosOrbit.api.repository.RegiaoMonitoradaRepository;
import com.example.SosOrbit.soap.model.RiscoOrbitalResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DadoAmbientalService {

    private final DadoAmbientalRepository dadoRepository;
    private final RegiaoMonitoradaRepository regiaoRepository;
    private final AlertaRepository alertaRepository;
    private final RecursoRepository recursoRepository;
    private final RiscoOrbitalSoapClient riscoOrbitalSoapClient;
    private final AlertaEventPublisher alertaEventPublisher;

    public DadoAmbientalService(DadoAmbientalRepository dadoRepository,
                                RegiaoMonitoradaRepository regiaoRepository,
                                AlertaRepository alertaRepository,
                                RecursoRepository recursoRepository,
                                RiscoOrbitalSoapClient riscoOrbitalSoapClient,
                                AlertaEventPublisher alertaEventPublisher) {
        this.dadoRepository = dadoRepository;
        this.regiaoRepository = regiaoRepository;
        this.alertaRepository = alertaRepository;
        this.recursoRepository = recursoRepository;
        this.riscoOrbitalSoapClient = riscoOrbitalSoapClient;
        this.alertaEventPublisher = alertaEventPublisher;
    }

    public List<DadoAmbiental> listar() {
        return dadoRepository.findAll();
    }

    public List<DadoAmbiental> listarPorRegiao(Long regiaoId) {
        return dadoRepository.findByRegiaoId(regiaoId);
    }

    public List<DadoAmbiental> listarPorNivel(NivelRisco nivelRisco) {
        return dadoRepository.findByNivelRisco(nivelRisco);
    }

    public DadoAmbiental salvar(Long regiaoId, DadoAmbientalDTO dto) {
        RegiaoMonitorada regiao = regiaoRepository.findById(regiaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Regiao nao encontrada"));

        DadoAmbiental dado = converterDto(dto);
        RiscoOrbitalResponse riscoSoap = riscoOrbitalSoapClient.processarDadoOrbital(regiao, dado);
        double indice = riscoSoap.getIndiceRisco();
        NivelRisco nivel = NivelRisco.valueOf(riscoSoap.getNivelRisco());

        dado.setRegiao(regiao);
        dado.setIndiceRisco(indice);
        dado.setNivelRisco(nivel);
        dado.setDataRegistro(LocalDateTime.now());

        DadoAmbiental salvo = dadoRepository.save(dado);

        if (nivel == NivelRisco.ALTO || nivel == NivelRisco.CRITICO) {
            criarAlerta(regiao, salvo, indice, nivel);
        }

        return salvo;
    }

    public DadoAmbiental atualizar(Long id, DadoAmbientalDTO dto) {
        DadoAmbiental dado = buscarPorId(id);
        preencherDados(dado, dto);
        RegiaoMonitorada regiao = dado.getRegiao();
        RiscoOrbitalResponse riscoSoap = riscoOrbitalSoapClient.processarDadoOrbital(regiao, dado);
        double indice = riscoSoap.getIndiceRisco();
        NivelRisco nivel = NivelRisco.valueOf(riscoSoap.getNivelRisco());

        dado.setIndiceRisco(indice);
        dado.setNivelRisco(nivel);
        dado.setDataRegistro(LocalDateTime.now());

        DadoAmbiental salvo = dadoRepository.save(dado);

        if (nivel == NivelRisco.ALTO || nivel == NivelRisco.CRITICO) {
            criarOuAtualizarAlerta(regiao, salvo, indice, nivel);
        }

        return salvo;
    }

    @Transactional
    public void deletar(Long id) {
        DadoAmbiental dado = buscarPorId(id);
        List<Alerta> alertas = alertaRepository.findByDadoAmbientalId(dado.getId());
        for (Alerta alerta : alertas) {
            recursoRepository.deleteByAlertaId(alerta.getId());
        }
        alertaRepository.deleteAll(alertas);
        dadoRepository.delete(dado);
    }

    private DadoAmbiental converterDto(DadoAmbientalDTO dto) {
        DadoAmbiental dado = new DadoAmbiental();
        preencherDados(dado, dto);
        return dado;
    }

    private void preencherDados(DadoAmbiental dado, DadoAmbientalDTO dto) {
        dado.setChuvaPrevistaMm(dto.chuvaPrevistaMm());
        dado.setTemperatura(dto.temperatura());
        dado.setUmidade(dto.umidade());
        dado.setVelocidadeVentoKmH(dto.velocidadeVentoKmH());
        dado.setNivelRioMetros(dto.nivelRioMetros());
        dado.setFocosCalor(dto.focosCalor());
        dado.setHistoricoOcorrencias(dto.historicoOcorrencias());
    }

    private void criarAlerta(RegiaoMonitorada regiao, DadoAmbiental dado, double indice, NivelRisco nivel) {
        Alerta alerta = new Alerta();
        alerta.setTitulo("Risco " + nivel + " em " + regiao.getNome());
        alerta.setMensagem("Indice de risco chegou a " + Math.round(indice)
                + ". Verificar abrigos, rotas seguras e recursos para resposta.");
        alerta.setIndiceRisco(indice);
        alerta.setNivelRisco(nivel);
        alerta.setStatus(StatusAlerta.ABERTO);
        alerta.setDataCriacao(LocalDateTime.now());
        alerta.setRegiao(regiao);
        alerta.setDadoAmbiental(dado);
        Alerta alertaSalvo = alertaRepository.save(alerta);
        alertaEventPublisher.publicarAlertaCriado(alertaSalvo);
    }

    private void criarOuAtualizarAlerta(RegiaoMonitorada regiao, DadoAmbiental dado, double indice, NivelRisco nivel) {
        Alerta alerta = alertaRepository.findByDadoAmbientalId(dado.getId()).stream()
                .findFirst()
                .orElseGet(Alerta::new);
        boolean novoAlerta = alerta.getId() == null;
        alerta.setTitulo("Risco " + nivel + " em " + regiao.getNome());
        alerta.setMensagem("Indice de risco chegou a " + Math.round(indice)
                + ". Verificar abrigos, rotas seguras e recursos para resposta.");
        alerta.setIndiceRisco(indice);
        alerta.setNivelRisco(nivel);
        alerta.setStatus(alerta.getStatus() == null ? StatusAlerta.ABERTO : alerta.getStatus());
        alerta.setDataCriacao(alerta.getDataCriacao() == null ? LocalDateTime.now() : alerta.getDataCriacao());
        alerta.setRegiao(regiao);
        alerta.setDadoAmbiental(dado);
        Alerta alertaSalvo = alertaRepository.save(alerta);

        if (novoAlerta) {
            alertaEventPublisher.publicarAlertaCriado(alertaSalvo);
        }
    }

    private DadoAmbiental buscarPorId(Long id) {
        return dadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dado ambiental nao encontrado"));
    }
}
