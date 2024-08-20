package com.example.dbbancopicpay.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Entity
public class Conta {
    @Id
    @Column(name = "numero_conta", length = 11)
    @Schema(description = "Account Number", example = "58564")
    private String numeroConta;

    @NotNull
    @Column(name = "saldo", precision = 10, scale = 2)
    @Schema(description = "Balance", example = "1500.00")
    private BigDecimal saldo;

    @NotNull
    @Column(name = "limite_especial", precision = 10, scale = 2)
    @Schema(description = "Special Limit", example = "1000.00")
    private BigDecimal limiteEspecial;

    @NotNull
    @CPF(message = "CPF Inválido!")
    @Column(name = "cliente_cpf", length = 11)
    @Schema(description = "Client CPF", example = "34567890123")
    private String clienteCpf;

    public Conta(String numeroConta, BigDecimal saldo, BigDecimal limiteEspecial, String clienteCpf) {
        this.numeroConta = numeroConta;
        this.saldo = saldo;
        this.limiteEspecial = limiteEspecial;
        this.clienteCpf = clienteCpf;
    }

    public Conta (BigDecimal saldo, BigDecimal limiteEspecial, String clienteCpf) {
        this.saldo = saldo;
        this.limiteEspecial = limiteEspecial;
        this.clienteCpf = clienteCpf;
    }

    public Conta() {}

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getLimiteEspecial() {
        return limiteEspecial;
    }

    public void setLimiteEspecial(BigDecimal limiteEspecial) {
        this.limiteEspecial = limiteEspecial;
    }

    public String getClienteCpf() {
        return clienteCpf;
    }

    public void setClienteCpf(String clienteCpf) {
        this.clienteCpf = clienteCpf;
    }

    @Override
    public String toString() {
        return "Conta{" +
                "numeroConta='" + numeroConta +
                ", saldo=" + saldo +
                ", limiteEspecial=" + limiteEspecial +
                ", clienteCpf='" + clienteCpf +
                '}';
    }
}
