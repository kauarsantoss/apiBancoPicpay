package com.example.dbbancopicpay.service;

import com.example.dbbancopicpay.model.Cliente;
import com.example.dbbancopicpay.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> searchAllClientes () { return clienteRepository.findAll(); }

    @Transactional
    public Cliente saveClientes ( Cliente cliente ) { return clienteRepository.save(cliente); }

    public Cliente searchClientesByCpf ( String cpf ) {
        return clienteRepository.findById(cpf).orElseThrow(() ->
                new RuntimeException("Cliente não encontrado."));
    }

    @Transactional
    public Cliente deleteCliente ( String cpf ) {
        Cliente cliente = searchClientesByCpf(cpf);
        clienteRepository.delete(cliente);
        return cliente;
    }

    public List<Cliente> searchByName ( String nome ) { return clienteRepository.findByNomeLikeIgnoreCase(nome); }
    public List<Cliente> searchByEmail ( String email ) { return clienteRepository.findByEmailLikeIgnoreCase(email); }
    public List<Cliente> searchByTelefone ( String telefone ) { return clienteRepository.findByTelefone(telefone); }




}
