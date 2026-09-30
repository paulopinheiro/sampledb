package br.com.paulopinheiro.sampledb.rest.resource;

import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.stream.Collectors;

/**
 * Interceptor Global do Jakarta REST para capturar erros de validação. Converte
 * exceções complexas do Java em mensagens amigáveis para o Frontend.
 */
@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    @Override
    public Response toResponse(ConstraintViolationException exception) {
        // Varre todas as violações e junta os erros em uma única string legível
        String errorMessage = exception.getConstraintViolations().stream()
                .map(violation -> {
                    // Pega o nome do campo (ex: "email") e o motivo (ex: "deve ser um endereço de e-mail bem formado")
                    String campo = violation.getPropertyPath().toString();
                    String motivo = violation.getMessage();
                    return "Campo '" + campo + "': " + motivo;
                })
                .collect(Collectors.joining(" | "));

        // Devolve o erro formatado em texto simples com status 400 (Bad Request)
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Erro de Validação: " + errorMessage)
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
