package com.example.SosOrbit.api.controller;

import com.example.SosOrbit.api.dto.AlertaDTO;
import com.example.SosOrbit.api.model.NivelRisco;
import com.example.SosOrbit.api.model.StatusAlerta;
import com.example.SosOrbit.api.service.AlertaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
public class AlertaController {

    private final AlertaService service;

    public AlertaController(AlertaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AlertaDTO>> listar() {
        List<AlertaDTO> alertas = service.listar().stream()
                .map(AlertaDTO::from)
                .toList();
        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/abertos")
    public ResponseEntity<List<AlertaDTO>> listarAbertos() {
        List<AlertaDTO> alertas = service.listarAbertos().stream()
                .map(AlertaDTO::from)
                .toList();
        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/nivel/{nivelRisco}")
    public ResponseEntity<List<AlertaDTO>> listarPorNivel(@PathVariable NivelRisco nivelRisco) {
        List<AlertaDTO> alertas = service.listarPorNivel(nivelRisco).stream()
                .map(AlertaDTO::from)
                .toList();
        return ResponseEntity.ok(alertas);
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<AlertaDTO> atualizarStatus(@PathVariable Long id, @PathVariable StatusAlerta status) {
        return ResponseEntity.ok(AlertaDTO.from(service.atualizarStatus(id, status)));
    }
}
