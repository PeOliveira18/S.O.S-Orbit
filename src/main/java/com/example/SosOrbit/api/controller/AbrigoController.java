package com.example.SosOrbit.api.controller;

import com.example.SosOrbit.api.dto.AbrigoDTO;
import com.example.SosOrbit.api.service.AbrigoService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/abrigos")
public class AbrigoController {

    private final AbrigoService service;

    public AbrigoController(AbrigoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AbrigoDTO>> listar() {
        List<AbrigoDTO> abrigos = service.listar().stream()
                .map(AbrigoDTO::from)
                .toList();
        return ResponseEntity.ok(abrigos);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<AbrigoDTO>> listarAtivos() {
        List<AbrigoDTO> abrigos = service.listarAtivos().stream()
                .map(AbrigoDTO::from)
                .toList();
        return ResponseEntity.ok(abrigos);
    }

    @GetMapping("/cidade")
    public ResponseEntity<List<AbrigoDTO>> buscarPorCidade(@RequestParam String cidade) {
        List<AbrigoDTO> abrigos = service.buscarPorCidade(cidade).stream()
                .map(AbrigoDTO::from)
                .toList();
        return ResponseEntity.ok(abrigos);
    }

    @PostMapping
    public ResponseEntity<AbrigoDTO> criar(@Valid @RequestBody AbrigoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(AbrigoDTO.from(service.salvar(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AbrigoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AbrigoDTO dto) {
        return ResponseEntity.ok(AbrigoDTO.from(service.atualizar(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
