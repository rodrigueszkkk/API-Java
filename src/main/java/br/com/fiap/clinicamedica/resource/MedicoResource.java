package br.com.fiap.clinicamedica.resource;

import br.com.fiap.clinicamedica.dao.MedicoDao;
import br.com.fiap.clinicamedica.dto.medico.MedicoCadastroDto;
import br.com.fiap.clinicamedica.dto.medico.MedicoResponseDto;
import br.com.fiap.clinicamedica.model.Medico;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.modelmapper.ModelMapper;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/medicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicoResource {

    @Inject
    MedicoDao medicoDao;

    @Inject
    ModelMapper modelMapper;

    @POST // POST /medicos [cite: 49]
    public Response cadastrar(@Valid MedicoCadastroDto medicoDto) {
        Medico medico = modelMapper.map(medicoDto, Medico.class);
        Medico medicoSalvo = medicoDao.cadastrar(medico);
        MedicoResponseDto responseDto = modelMapper.map(medicoSalvo, MedicoResponseDto.class);

        URI uri = UriBuilder.fromPath("/medicos/{id}").build(responseDto.getId());
        return Response.created(uri).entity(responseDto).build();
    }

    @GET // GET /medicos [cite: 51]
    public Response listarTodos() {
        List<Medico> medicos = medicoDao.listar();
        List<MedicoResponseDto> dtos = medicos.stream()
                .map(medico -> modelMapper.map(medico, MedicoResponseDto.class))
                .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET // GET /medicos/{id} [cite: 50]
    @Path("/{id}")
    public Response pesquisarPorId(@PathParam("id") long id) {
        Medico medico = medicoDao.pesquisarPorId(id);
        MedicoResponseDto responseDto = modelMapper.map(medico, MedicoResponseDto.class);
        return Response.ok(responseDto).build();
    }

    @PUT // PUT /medicos/{id} [cite: 52]
    @Path("/{id}")
    public Response atualizar(@PathParam("id") long id, @Valid MedicoCadastroDto medicoDto) {
        Medico medico = modelMapper.map(medicoDto, Medico.class);
        medico.setId(id);
        Medico medicoAtualizado = medicoDao.atualizar(medico);
        MedicoResponseDto responseDto = modelMapper.map(medicoAtualizado, MedicoResponseDto.class);
        return Response.ok(responseDto).build();
    }

    @DELETE // DELETE /medicos/{id} [cite: 53]
    @Path("/{id}")
    public Response remover(@PathParam("id") long id) {
        medicoDao.remover(id);
        return Response.noContent().build();
    }
}