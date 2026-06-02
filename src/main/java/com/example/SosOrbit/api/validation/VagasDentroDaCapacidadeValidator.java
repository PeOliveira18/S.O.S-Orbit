package com.example.SosOrbit.api.validation;

import com.example.SosOrbit.api.dto.AbrigoDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class VagasDentroDaCapacidadeValidator implements ConstraintValidator<VagasDentroDaCapacidade, AbrigoDTO> {

    @Override
    public boolean isValid(AbrigoDTO dto, ConstraintValidatorContext context) {
        if (dto.capacidade() == null || dto.vagasDisponiveis() == null) {
            return true;
        }

        return dto.vagasDisponiveis() <= dto.capacidade();
    }
}
