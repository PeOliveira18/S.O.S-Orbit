package com.example.SosOrbit.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> tratarValidacao(MethodArgumentNotValidException exception,
                                                    HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();

        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));

        exception.getBindingResult().getGlobalErrors()
                .forEach(error -> fields.put(error.getObjectName(), error.getDefaultMessage()));

        ApiError error = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Dados invalidos",
                request.getRequestURI(),
                fields
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> tratarNaoEncontrado(ResourceNotFoundException exception,
                                                        HttpServletRequest request) {
        ApiError error = ApiError.of(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<ApiError> tratarServicoExterno(ExternalServiceException exception,
                                                         HttpServletRequest request) {
        ApiError error = ApiError.of(
                HttpStatus.BAD_GATEWAY.value(),
                "Bad Gateway",
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> tratarTipoInvalido(MethodArgumentTypeMismatchException exception,
                                                       HttpServletRequest request) {
        String campo = exception.getName();
        String valor = exception.getValue() == null ? "" : exception.getValue().toString();
        String tipoEsperado = exception.getRequiredType() == null
                ? "tipo esperado"
                : exception.getRequiredType().getSimpleName();

        ApiError error = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Parametro invalido",
                request.getRequestURI(),
                Map.of(campo, "Valor '" + valor + "' nao e valido para " + tipoEsperado)
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> tratarJsonInvalido(HttpMessageNotReadableException exception,
                                                       HttpServletRequest request) {
        ApiError error = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Corpo da requisicao invalido ou mal formatado",
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> tratarParametroObrigatorio(MissingServletRequestParameterException exception,
                                                               HttpServletRequest request) {
        ApiError error = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Parametro obrigatorio ausente",
                request.getRequestURI(),
                Map.of(exception.getParameterName(), "Parametro e obrigatorio")
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> tratarConstraintViolation(ConstraintViolationException exception,
                                                             HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getConstraintViolations().forEach(violation ->
                fields.put(violation.getPropertyPath().toString(), violation.getMessage()));

        ApiError error = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "Dados invalidos",
                request.getRequestURI(),
                fields
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ApiError> tratarAutenticacao(AuthenticationException exception,
                                                       HttpServletRequest request) {
        ApiError error = ApiError.of(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Usuario ou senha invalidos",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> tratarConflitoBanco(DataIntegrityViolationException exception,
                                                        HttpServletRequest request) {
        ApiError error = ApiError.of(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "Operacao viola uma regra de integridade do banco de dados",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> tratarRuntime(RuntimeException exception, HttpServletRequest request) {
        ApiError error = ApiError.of(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> tratarErroNaoEsperado(Exception exception, HttpServletRequest request) {
        logger.error("Erro nao esperado na requisicao {}", request.getRequestURI(), exception);

        ApiError error = ApiError.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Erro interno ao processar a requisicao",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
