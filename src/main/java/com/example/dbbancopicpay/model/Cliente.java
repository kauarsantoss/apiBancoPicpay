package com.example.dbbancopicpay.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Cliente {
    @Id
    @CPF(message = "CPF Inválido!")
    private String cpf;
    @NotNull
    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
    private String nome;
    @Email(message = "E-mail inválido")
    private String email;
    private String telefone;

    public Cliente(){}
    public Cliente(String cpf, String nome, String email, String telefone) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    @Override
    public String toString() {
        return "Cliente{" +
                ", cpf='" + cpf +
                ", nome='" + nome +
                ", email='" + email +
                ", telefone='" + telefone +
                '}';
    }
}
