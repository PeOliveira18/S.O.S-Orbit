package com.example.SosOrbit.api.controller;

import com.example.SosOrbit.api.dto.RegiaoMonitoradaDTO;
import com.example.SosOrbit.api.model.TipoRisco;
import com.example.SosOrbit.api.service.RegiaoMonitoradaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
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
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(RegiaoMonitoradaDTO.from(service.buscarPorId(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody RegiaoMonitoradaDTO dto) {
        try {
            return ResponseEntity.ok(RegiaoMonitoradaDTO.from(service.atualizar(id, dto)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
