package com.example.SosOrbit.api.repository;

import com.example.SosOrbit.api.model.Alerta;
import com.example.SosOrbit.api.model.NivelRisco;
import com.example.SosOrbit.api.model.StatusAlerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByStatus(StatusAlerta status);
    List<Alerta> findByNivelRisco(NivelRisco nivelRisco);
}
