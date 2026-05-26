package com.example.SosOrbit.api.service;

import com.example.SosOrbit.api.dto.DadoAmbientalDTO;
import com.example.SosOrbit.api.model.*;
import com.example.SosOrbit.api.repository.AlertaRepository;
import com.example.SosOrbit.api.repository.DadoAmbientalRepository;
import com.example.SosOrbit.api.repository.RegiaoMonitoradaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DadoAmbientalService {

    private final DadoAmbientalRepository dadoRepository;
    private final RegiaoMonitoradaRepository regiaoRepository;
    private final AlertaRepository alertaRepository;

    public DadoAmbientalService(DadoAmbientalRepository dadoRepository,
                                RegiaoMonitoradaRepository regiaoRepository,
                                AlertaRepository alertaRepository) {
        this.dadoRepository = dadoRepository;
        this.regiaoRepository = regiaoRepository;
        this.alertaRepository = alertaRepository;
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
                .orElseThrow(() -> new RuntimeException("Regiao nao encontrada"));

        DadoAmbiental dado = converterDto(dto);
        double indice = calcularIndiceRisco(regiao, dado);
        NivelRisco nivel = classificarNivel(indice);

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

    private double calcularIndiceRisco(RegiaoMonitorada regiao, DadoAmbiental dado) {
        double indice = 0;

        indice += limitar(valor(dado.getChuvaPrevistaMm()) * 0.30, 30);
        indice += limitar(valor(dado.getNivelRioMetros()) * 4, 20);
        indice += limitar(valor(dado.getFocosCalor()) * 3, 18);
        indice += limitar((100 - valor(dado.getUmidade())) * 0.10, 10);
        indice += limitar(valor(dado.getVelocidadeVentoKmH()) * 0.12, 8);
        indice += limitar(valor(dado.getHistoricoOcorrencias()) * 1.5, 9);
        indice += limitar(valor(regiao.getIndiceVulnerabilidade()) * 0.15, 15);

        return Math.min(indice, 100);
    }

    private NivelRisco classificarNivel(double indice) {
        if (indice >= 80) return NivelRisco.CRITICO;
        if (indice >= 60) return NivelRisco.ALTO;
        if (indice >= 30) return NivelRisco.MEDIO;
        return NivelRisco.BAIXO;
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
        alertaRepository.save(alerta);
    }

    private double limitar(double valor, double limite) {
        return Math.min(Math.max(valor, 0), limite);
    }

    private double valor(Number numero) {
        return numero == null ? 0 : numero.doubleValue();
    }
}
