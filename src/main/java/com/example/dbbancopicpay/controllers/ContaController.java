package com.example.dbbancopicpay.controllers;

import com.example.dbbancopicpay.model.Conta;
import com.example.dbbancopicpay.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conta")
public class ContaController {
    private ContaService contaService;
    private final Validator validator;

    @Autowired
    public ContaController(ContaService contaService, Validator validator) {
        this.contaService = contaService;
        this.validator = validator;
    }



    @GetMapping("/show")
    @Operation(summary = "Show all accounts", description = "Returns a list of all available accounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Conta.class)))
    })
    public List<Conta> showContas() { return contaService.searchAllContas(); }

    @PostMapping("/add")
    public ResponseEntity<?> addConta ( @RequestBody Conta conta, BindingResult result ) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação: ");
            result.getAllErrors().forEach(error -> {
                sb.append(" | ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }
        try {
            if(contaService.insertAccount(conta) != null) {
                return ResponseEntity.ok("Conta adicionada com sucesso");
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Conta já existente ou Cliente não existente");
            }
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar a conta: \n" + e.getMessage());
        }
    }


    @PostMapping("/deposit/{numero_conta}")
    @Operation(summary = "Delete accounts", description = "Returns a deleted a account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Conta.class)))
    })
    public ResponseEntity<?> depositSaldo ( @RequestBody Conta conta, @PathVariable String numeroConta, BindingResult result ) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação: ");
            result.getAllErrors().forEach(error -> {
                sb.append(" | ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }
        try {
            contaService.deposit(conta.getNumeroConta(), conta.getSaldo());
            return ResponseEntity.ok("Saldo depositado com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao depositar o saldo: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/drop/{numero_conta}")
    public ResponseEntity<?> dropContasByNumber ( @Parameter(description = "Numero da Conta", example = "123")
                                                      @PathVariable String numero_conta ) {
        contaService.deleteConta(numero_conta);
        return ResponseEntity.ok("Conta excluída com sucesso");
    }

}
