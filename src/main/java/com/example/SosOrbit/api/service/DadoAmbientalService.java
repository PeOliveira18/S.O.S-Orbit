package com.example.SosOrbit.api.service;

import com.example.SosOrbit.api.dto.DadoAmbientalDTO;
import com.example.SosOrbit.api.exception.ResourceNotFoundException;
import com.example.SosOrbit.api.integration.RiscoOrbitalSoapClient;
import com.example.SosOrbit.api.messaging.AlertaEventPublisher;
import com.example.SosOrbit.api.model.*;
import com.example.SosOrbit.api.repository.AlertaRepository;
import com.example.SosOrbit.api.repository.DadoAmbientalRepository;
import com.example.SosOrbit.api.repository.RegiaoMonitoradaRepository;
import com.example.SosOrbit.soap.model.RiscoOrbitalResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DadoAmbientalService {

    private final DadoAmbientalRepository dadoRepository;
    private final RegiaoMonitoradaRepository regiaoRepository;
    private final AlertaRepository alertaRepository;
    private final RiscoOrbitalSoapClient riscoOrbitalSoapClient;
    private final AlertaEventPublisher alertaEventPublisher;

    public DadoAmbientalService(DadoAmbientalRepository dadoRepository,
                                RegiaoMonitoradaRepository regiaoRepository,
                                AlertaRepository alertaRepository,
                                RiscoOrbitalSoapClient riscoOrbitalSoapClient,
                                AlertaEventPublisher alertaEventPublisher) {
        this.dadoRepository = dadoRepository;
        this.regiaoRepository = regiaoRepository;
        this.alertaRepository = alertaRepository;
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

    private DadoAmbiental converterDto(DadoAmbientalDTO dto) {
        DadoAmbiental dado = new DadoAmbiental();
        dado.setChuvaPrevistaMm(dto.chuvaPrevistaMm());
        dado.setTemperatura(dto.temperatura());
        dado.setUmidade(dto.umidade());
        dado.setVelocidadeVentoKmH(dto.velocidadeVentoKmH());
        dado.setNivelRioMetros(dto.nivelRioMetros());
        dado.setFocosCalor(dto.focosCalor());
        dado.setHistoricoOcorrencias(dto.historicoOcorrencias());
        return dado;
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
}
