package br.com.fiap.clinicamedica.dto.paciente;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PacienteCadastroDto {


    @NotBlank(message = "Nome não pode ser nulo ou vazio")
    private String nome;

    @NotNull(message = "Idade não pode ser nula")
    @Min(value = 0, message = "Idade não pode ser negativa")
    private Integer idade; // Usar Integer para permitir @NotNull

    @NotBlank(message = "Email não pode ser nulo ou vazio")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    private String telefone;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}