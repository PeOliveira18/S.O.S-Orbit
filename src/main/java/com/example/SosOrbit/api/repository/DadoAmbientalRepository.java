package com.example.SosOrbit.api.repository;

import com.example.SosOrbit.api.model.DadoAmbiental;
import com.example.SosOrbit.api.model.NivelRisco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DadoAmbientalRepository extends JpaRepository<DadoAmbiental, Long> {
    List<DadoAmbiental> findByRegiaoId(Long regiaoId);
    List<DadoAmbiental> findByNivelRisco(NivelRisco nivelRisco);
}
