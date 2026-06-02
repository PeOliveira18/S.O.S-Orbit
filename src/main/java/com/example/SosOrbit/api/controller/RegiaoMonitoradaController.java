package com.example.SosOrbit.api.controller;

import com.example.SosOrbit.api.dto.RegiaoMonitoradaDTO;
import com.example.SosOrbit.api.model.TipoRisco;
import com.example.SosOrbit.api.service.RegiaoMonitoradaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regioes")
public class RegiaoMonitoradaController {

    private final RegiaoMonitoradaService service;

    public RegiaoMonitoradaController(RegiaoMonitoradaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RegiaoMonitoradaDTO>> listar() {
        List<RegiaoMonitoradaDTO> regioes = service.listar().stream()
                .map(RegiaoMonitoradaDTO::from)
                .toList();
        return ResponseEntity.ok(regioes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegiaoMonitoradaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(RegiaoMonitoradaDTO.from(service.buscarPorId(id)));
    }

    @GetMapping("/cidade")
    public ResponseEntity<List<RegiaoMonitoradaDTO>> buscarPorCidade(@RequestParam String cidade) {
        List<RegiaoMonitoradaDTO> regioes = service.buscarPorCidade(cidade).stream()
                .map(RegiaoMonitoradaDTO::from)
                .toList();
        return ResponseEntity.ok(regioes);
    }

    @GetMapping("/risco/{tipoRisco}")
    public ResponseEntity<List<RegiaoMonitoradaDTO>> buscarPorTipoRisco(@PathVariable TipoRisco tipoRisco) {
        List<RegiaoMonitoradaDTO> regioes = service.buscarPorTipoRisco(tipoRisco).stream()
                .map(RegiaoMonitoradaDTO::from)
                .toList();
        return ResponseEntity.ok(regioes);
    }

    @PostMapping
    public ResponseEntity<RegiaoMonitoradaDTO> criar(@Valid @RequestBody RegiaoMonitoradaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(RegiaoMonitoradaDTO.from(service.salvar(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegiaoMonitoradaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody RegiaoMonitoradaDTO dto) {
        return ResponseEntity.ok(RegiaoMonitoradaDTO.from(service.atualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
