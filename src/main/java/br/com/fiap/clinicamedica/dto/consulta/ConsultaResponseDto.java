package br.com.fiap.clinicamedica.dto.consulta;

import br.com.fiap.clinicamedica.dto.medico.MedicoResponseDto;
import br.com.fiap.clinicamedica.dto.paciente.PacienteResponseDto;
import java.time.LocalDate;

public class ConsultaResponseDto {

    private long id;
    private LocalDate dataConsulta;
    private String horaConsulta;
    private String motivo;


    private PacienteResponseDto paciente;
    private MedicoResponseDto medico;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public PacienteResponseDto getPaciente() {
        return paciente;
    }

    public void setPaciente(PacienteResponseDto paciente) {
        this.paciente = paciente;
    }

    public MedicoResponseDto getMedico() {
        return medico;
    }

    public void setMedico(MedicoResponseDto medico) {
        this.medico = medico;
    }
}