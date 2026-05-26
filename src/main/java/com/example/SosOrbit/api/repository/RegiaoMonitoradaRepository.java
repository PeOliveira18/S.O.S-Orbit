package com.example.SosOrbit.api.repository;

import com.example.SosOrbit.api.model.RegiaoMonitorada;
import com.example.SosOrbit.api.model.TipoRisco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegiaoMonitoradaRepository extends JpaRepository<RegiaoMonitorada, Long> {
    List<RegiaoMonitorada> findByCidadeContainingIgnoreCase(String cidade);
    List<RegiaoMonitorada> findByTipoRiscoPrincipal(TipoRisco tipoRiscoPrincipal);
}
