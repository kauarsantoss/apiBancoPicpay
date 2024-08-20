package com.example.dbbancopicpay.repository;

import com.example.dbbancopicpay.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    List<Cliente> findByNomeLikeIgnoreCase(String nome);
    List<Cliente> findByEmailLikeIgnoreCase(String email);
    List<Cliente> findByTelefone(String telefone);
}
