package com.example.SosOrbit.api.controller;

import com.example.SosOrbit.api.dto.RecursoDTO;
import com.example.SosOrbit.api.model.PrioridadeRecurso;
import com.example.SosOrbit.api.model.StatusRecurso;
import com.example.SosOrbit.api.service.RecursoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
public class RecursoController {

    private final RecursoService service;

    public RecursoController(RecursoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RecursoDTO>> listar() {
        List<RecursoDTO> recursos = service.listar().stream()
                .map(RecursoDTO::from)
                .toList();
        return ResponseEntity.ok(recursos);
    }

    @GetMapping("/alerta/{alertaId}")
    public ResponseEntity<List<RecursoDTO>> listarPorAlerta(@PathVariable Long alertaId) {
        List<RecursoDTO> recursos = service.listarPorAlerta(alertaId).stream()
                .map(RecursoDTO::from)
                .toList();
        return ResponseEntity.ok(recursos);
    }

    @GetMapping("/prioridade/{prioridade}")
    public ResponseEntity<List<RecursoDTO>> listarPorPrioridade(@PathVariable PrioridadeRecurso prioridade) {
        List<RecursoDTO> recursos = service.listarPorPrioridade(prioridade).stream()
                .map(RecursoDTO::from)
                .toList();
        return ResponseEntity.ok(recursos);
    }

    @PostMapping("/{alertaId}")
    public ResponseEntity<RecursoDTO> criar(@PathVariable Long alertaId, @Valid @RequestBody RecursoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(RecursoDTO.from(service.salvar(alertaId, dto)));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<RecursoDTO> atualizarStatus(@PathVariable Long id, @PathVariable StatusRecurso status) {
        return ResponseEntity.ok(RecursoDTO.from(service.atualizarStatus(id, status)));
    }
}
