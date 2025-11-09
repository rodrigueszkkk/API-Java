package br.com.fiap.clinicamedica.dto.medico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MedicoCadastroDto {


    @NotBlank(message = "Nome não pode ser nulo ou vazio")
    private String nome;


    @NotBlank(message = "Especialidade não pode ser nula ou vazia")
    @Size(min = 3, message = "Especialidade deve ter ao menos 3 caracteres")
    private String especialidade;

    @NotBlank(message = "CRM não pode ser nulo ou vazio")
    private String crm;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }
}