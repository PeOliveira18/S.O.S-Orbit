package com.example.SosOrbit.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = VagasDentroDaCapacidadeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface VagasDentroDaCapacidade {
    String message() default "Vagas disponiveis nao podem ser maiores que a capacidade do abrigo";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
