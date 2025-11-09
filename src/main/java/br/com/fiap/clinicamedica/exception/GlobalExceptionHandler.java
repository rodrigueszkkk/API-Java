package br.com.fiap.clinicamedica.exception;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;


@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {

        if (exception instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) exception);
        }

        if (exception instanceof EntidadeNaoEncontradaException) {
            return handleEntidadeNaoEncontrada((EntidadeNaoEncontradaException) exception);
        }

        return handleGenericException(exception);
    }

    private Response handleConstraintViolation(ConstraintViolationException e) {
        List<String> erros = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Erro de validação",
                erros
        );
        return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
    }

    private Response handleEntidadeNaoEncontrada(EntidadeNaoEncontradaException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                Response.Status.NOT_FOUND.getStatusCode(),
                "Recurso não encontrado",
                List.of(e.getMessage())
        );
        return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
    }


    private Response handleGenericException(Exception e) {


        e.printStackTrace();

        ErrorResponse errorResponse = new ErrorResponse(
                Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(),
                "Erro interno do servidor",
                List.of("Ocorreu um erro inesperado. Contate o administrador.")
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
    }


    public static class ErrorResponse {
        private int status;
        private String titulo;
        private List<String> mensagens;

        public ErrorResponse(int status, String titulo, List<String> mensagens) {
            this.status = status;
            this.titulo = titulo;
            this.mensagens = mensagens;
        }


        public int getStatus() { return status; }
        public String getTitulo() { return titulo; }
        public List<String> getMensagens() { return mensagens; }
    }


}