package br.com.fiap.clinicamedica.resource;

import br.com.fiap.clinicamedica.dao.ConsultaDao;
import br.com.fiap.clinicamedica.dto.consulta.ConsultaCadastroDto;
import br.com.fiap.clinicamedica.dto.consulta.ConsultaResponseDto;
import br.com.fiap.clinicamedica.model.Consulta;
import br.com.fiap.clinicamedica.model.Medico;
import br.com.fiap.clinicamedica.model.Paciente;
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
@Path("/consultas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsultaResource {

    @Inject
    ConsultaDao consultaDao;

    @Inject
    ModelMapper modelMapper;

    @POST // POST /consultas [cite: 55]
    public Response agendar(@Valid ConsultaCadastroDto consultaDto) {
        // Mapeia os campos simples
        Consulta consulta = modelMapper.map(consultaDto, Consulta.class);

        // O DTO nos dá os IDs, mas o DAO espera objetos.
        // Criamos "stubs" (objetos parciais) para o DAO usar.
        Paciente pacienteStub = new Paciente();
        pacienteStub.setId(consultaDto.getIdPaciente());

        Medico medicoStub = new Medico();
        medicoStub.setId(consultaDto.getIdMedico());

        consulta.setPaciente(pacienteStub);
        consulta.setMedico(medicoStub);

        // O DAO irá validar os IDs e preencher o resto
        Consulta consultaAgendada = consultaDao.cadastrar(consulta);

        // O ModelMapper é inteligente e vai mapear os objetos aninhados
        ConsultaResponseDto responseDto = modelMapper.map(consultaAgendada, ConsultaResponseDto.class);

        URI uri = UriBuilder.fromPath("/consultas/{id}").build(responseDto.getId());
        return Response.created(uri).entity(responseDto).build();
    }

    @GET // GET /consultas [cite: 57]
    public Response listarTodas() {
        List<Consulta> consultas = consultaDao.listar();

        // O DAO já fez o JOIN, então o ModelMapper consegue mapear tudo
        List<ConsultaResponseDto> dtos = consultas.stream()
                .map(consulta -> modelMapper.map(consulta, ConsultaResponseDto.class))
                .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }

    @GET // GET /consultas/{id} [cite: 56]
    @Path("/{id}")
    public Response pesquisarPorId(@PathParam("id") long id) {
        Consulta consulta = consultaDao.pesquisarPorId(id);
        ConsultaResponseDto responseDto = modelMapper.map(consulta, ConsultaResponseDto.class);
        return Response.ok(responseDto).build();
    }

    @PUT // PUT /consultas/{id} [cite: 58]
    @Path("/{id}")
    public Response atualizar(@PathParam("id") long id, @Valid ConsultaCadastroDto consultaDto) {
        Consulta consulta = modelMapper.map(consultaDto, Consulta.class);

        // Define o ID da URL
        consulta.setId(id);

        // Define os "stubs" como no POST
        Paciente pacienteStub = new Paciente();
        pacienteStub.setId(consultaDto.getIdPaciente());
        consulta.setPaciente(pacienteStub);

        Medico medicoStub = new Medico();
        medicoStub.setId(consultaDto.getIdMedico());
        consulta.setMedico(medicoStub);

        Consulta consultaAtualizada = consultaDao.atualizar(consulta);
        ConsultaResponseDto responseDto = modelMapper.map(consultaAtualizada, ConsultaResponseDto.class);
        return Response.ok(responseDto).build();
    }

    @DELETE // DELETE /consultas/{id} [cite: 59]
    @Path("/{id}")
    public Response cancelar(@PathParam("id") long id) {
        consultaDao.remover(id);
        return Response.noContent().build();
    }
}