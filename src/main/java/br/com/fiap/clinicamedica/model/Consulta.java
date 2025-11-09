package br.com.fiap.clinicamedica.model;

import java.time.LocalDate; // Import da sua lista

public class Consulta {

    private long id;
    private LocalDate dataConsulta;
    private String horaConsulta;
    private String motivo;
    private Paciente paciente;
    private Medico medico;


    public Consulta() {
    }


    public Consulta(long id, LocalDate dataConsulta, String horaConsulta, String motivo, Paciente paciente, Medico medico) {
        this.id = id;
        this.dataConsulta = dataConsulta;
        this.horaConsulta = horaConsulta;
        this.motivo = motivo;
        this.paciente = paciente;
        this.medico = medico;
    }


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

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }
}