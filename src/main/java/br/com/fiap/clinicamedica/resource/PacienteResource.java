package br.com.fiap.clinicamedica.resource;

import br.com.fiap.clinicamedica.dao.PacienteDao;
import br.com.fiap.clinicamedica.dto.paciente.PacienteCadastroDto;
import br.com.fiap.clinicamedica.dto.paciente.PacienteResponseDto;
import br.com.fiap.clinicamedica.model.Paciente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid; // Import para o @Valid
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.modelmapper.ModelMapper;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    @Inject
    PacienteDao pacienteDao;

    @Inject
    ModelMapper modelMapper;

    @POST // POST /pacientes: Criação [cite: 43]
    public Response cadastrar(@Valid PacienteCadastroDto pacienteDto) {
        // Converte DTO -> Entidade
        Paciente paciente = modelMapper.map(pacienteDto, Paciente.class);

        // Chama o DAO
        Paciente pacienteSalvo = pacienteDao.cadastrar(paciente);

        // Converte Entidade -> DTO de Resposta
        PacienteResponseDto responseDto = modelMapper.map(pacienteSalvo, PacienteResponseDto.class);

        // Retorna 201 Created com a URI do novo recurso
        URI uri = UriBuilder.fromPath("/pacientes/{id}")
                .build(responseDto.getId());
        return Response.created(uri).entity(responseDto).build();
    }

    @GET // GET /pacientes: Lista todos [cite: 45]
    public Response listarTodos() {
        List<Paciente> pacientes = pacienteDao.listar();

        // Mapeia a lista de Entidades para a lista de DTOs
        List<PacienteResponseDto> dtos = pacientes.stream()
                .map(paciente -> modelMapper.map(paciente, PacienteResponseDto.class))
                .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }

    @GET // GET /pacientes/{id}: Retorna por ID [cite: 44]
    @Path("/{id}")
    public Response pesquisarPorId(@PathParam("id") long id) {
        Paciente paciente = pacienteDao.pesquisarPorId(id);

        // Se o DAO não encontrar, ele lançará a EntidadeNaoEncontradaException,
        // que será tratada pelo nosso Handler global (próximo passo).

        PacienteResponseDto responseDto = modelMapper.map(paciente, PacienteResponseDto.class);
        return Response.ok(responseDto).build();
    }

    @PUT // PUT /pacientes/{id}: Atualiza [cite: 46]
    @Path("/{id}")
    public Response atualizar(@PathParam("id") long id, @Valid PacienteCadastroDto pacienteDto) {
        Paciente paciente = modelMapper.map(pacienteDto, Paciente.class);
        paciente.setId(id); // Garante que o ID da URL seja usado

        Paciente pacienteAtualizado = pacienteDao.atualizar(paciente);

        PacienteResponseDto responseDto = modelMapper.map(pacienteAtualizado, PacienteResponseDto.class);
        return Response.ok(responseDto).build();
    }

    @DELETE // DELETE /pacientes/{id}: Remove [cite: 47]
    @Path("/{id}")
    public Response remover(@PathParam("id") long id) {
        pacienteDao.remover(id);
        // Retorna 204 No Content (sucesso, sem corpo de resposta)
        return Response.noContent().build();
    }
}
