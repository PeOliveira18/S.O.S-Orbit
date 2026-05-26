package com.example.SosOrbit.api.repository;

import com.example.SosOrbit.api.model.Abrigo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbrigoRepository extends JpaRepository<Abrigo, Long> {
    List<Abrigo> findByAtivoTrue();
    List<Abrigo> findByCidadeContainingIgnoreCaseAndAtivoTrue(String cidade);
}
