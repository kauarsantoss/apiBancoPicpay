package com.example.dbbancopicpay.repository;

import com.example.dbbancopicpay.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, String> {

}
