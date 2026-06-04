package com.example.SosOrbit.api.repository;

import com.example.SosOrbit.api.model.PrioridadeRecurso;
import com.example.SosOrbit.api.model.Recurso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecursoRepository extends JpaRepository<Recurso, Long> {
    List<Recurso> findByAlertaId(Long alertaId);
    List<Recurso> findByPrioridade(PrioridadeRecurso prioridade);
    void deleteByAlertaId(Long alertaId);
}
