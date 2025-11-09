package br.com.fiap.clinicamedica.model;

public class Paciente {

    private long id;
    private String nome;
    private int idade;
    private String email;
    private String telefone;


    public Paciente() {
    }


    public Paciente(long id, String nome, int idade, String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.email = email;
        this.telefone = telefone;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
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