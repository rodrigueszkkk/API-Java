package br.com.fiap.clinicamedica.dto.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class ConsultaCadastroDto {


    @NotNull(message = "Data da consulta não pode ser nula")
    @Future(message = "Data da consulta deve ser futura")
    private LocalDate dataConsulta;

    @NotBlank(message = "Hora da consulta não pode ser nula ou vazia")
    private String horaConsulta; // (Ex: "14:30")

    private String motivo;

    @NotNull(message = "ID do Paciente não pode ser nulo")
    private Long idPaciente;

    @NotNull(message = "ID do Médico não pode ser nulo")
    private Long idMedico;


    public LocalDate getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDate dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getHoraConsulta() {
        return horaConsulta;
    }

    public void setHoraConsulta(String horaConsulta) {
        this.horaConsulta = horaConsulta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Long getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(Long idMedico) {
        this.idMedico = idMedico;
    }
}