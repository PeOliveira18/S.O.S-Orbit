package com.example.SosOrbit.api.controller;

import com.example.SosOrbit.api.dto.DadoAmbientalDTO;
import com.example.SosOrbit.api.model.NivelRisco;
import com.example.SosOrbit.api.service.DadoAmbientalService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dados-ambientais")
public class DadoAmbientalController {

    private final DadoAmbientalService service;

    public DadoAmbientalController(DadoAmbientalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<DadoAmbientalDTO>> listar() {
        List<DadoAmbientalDTO> dados = service.listar().stream()
                .map(DadoAmbientalDTO::from)
                .toList();
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/regiao/{regiaoId}")
    public ResponseEntity<List<DadoAmbientalDTO>> listarPorRegiao(@PathVariable Long regiaoId) {
        List<DadoAmbientalDTO> dados = service.listarPorRegiao(regiaoId).stream()
                .map(DadoAmbientalDTO::from)
                .toList();
        return ResponseEntity.ok(dados);
    }

    @GetMapping("/nivel/{nivelRisco}")
    public ResponseEntity<List<DadoAmbientalDTO>> listarPorNivel(@PathVariable NivelRisco nivelRisco) {
        List<DadoAmbientalDTO> dados = service.listarPorNivel(nivelRisco).stream()
                .map(DadoAmbientalDTO::from)
                .toList();
        return ResponseEntity.ok(dados);
    }

    @PostMapping("/{regiaoId}")
    public ResponseEntity<?> criar(@PathVariable Long regiaoId, @Valid @RequestBody DadoAmbientalDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(DadoAmbientalDTO.from(service.salvar(regiaoId, dto)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
