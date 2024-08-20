package com.example.dbbancopicpay.service;

import com.example.dbbancopicpay.model.Conta;
import com.example.dbbancopicpay.repository.ClienteRepository;
import com.example.dbbancopicpay.repository.ContaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class ContaService {
    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    public ContaService(ContaRepository contaRepository, ClienteRepository clienteRepository) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Conta> searchAllContas () { return contaRepository.findAll(); }

    @Transactional
    public Conta saveContas ( Conta conta ) { return contaRepository.save(conta); }

    public Conta searchContasByCpf ( String numeroConta ) {
        return contaRepository.findById(numeroConta).orElseThrow(() ->
                new RuntimeException("Conta não encontrado."));
    }

    public Conta insertAccount ( Conta conta ) {
        Random random = new Random();
        int fourDigitNumber = random.nextInt(1000, 9000);
        String numberStr = Integer.toString(fourDigitNumber);
        int digito = 0;
        for (int i = 0; i < numberStr.length(); i++) {
            digito += Character.getNumericValue(numberStr.charAt(i));
            digito = digito % 10;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(fourDigitNumber);
        sb.append(digito);

        String numeroConta = sb.toString();
        if( !contaRepository.existsById(numeroConta) && !clienteRepository.existsById(conta.getClienteCpf()) ) {
            conta.setNumeroConta(numeroConta);
            return contaRepository.save(conta);
        } else {
            return null;
        }
    }

    @Transactional
    public Conta deposit (String numeroConta, BigDecimal saldo) {
        Conta conta = searchContasByCpf(numeroConta);
        conta.setSaldo(conta.getSaldo().add(saldo));
        return contaRepository.save(conta);
    }

    @Transactional
    public Conta deleteConta ( String numeroConta ) {
        Conta conta = searchContasByCpf(numeroConta);
        contaRepository.delete(conta);
        return conta;
    }
}
