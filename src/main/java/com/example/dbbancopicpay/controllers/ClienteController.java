package com.example.dbbancopicpay.controllers;

import com.example.dbbancopicpay.model.Cliente;
import com.example.dbbancopicpay.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cliente")
public class ClienteController {
    private ClienteService clienteService;
    private final Validator validator;

    @Autowired
    public ClienteController( ClienteService clienteService, Validator validator ) {
        this.clienteService = clienteService;
        this.validator = validator;
    }

    @GetMapping("/show")
    public List<Cliente> showClientes() {
        return clienteService.searchAllClientes();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCliente (@Valid @RequestBody Cliente cliente, BindingResult result ) {
        if (result.hasErrors()) {
            StringBuilder sb = new StringBuilder("Erros de validação: ");
            result.getAllErrors().forEach(error -> {
                sb.append(" | ");
                sb.append(error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }
        try {
            clienteService.saveClientes(cliente);
            return ResponseEntity.ok("Cliente adicionado com sucesso");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: \n" + e.getMessage());
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao acessar o banco de dados: \n" + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar o cliente: \n" + e.getMessage());
        }
    }

    @DeleteMapping("/drop/{cpf}")
    public ResponseEntity<?> dropClientesByCpf ( @PathVariable String cpf ) {
        clienteService.deleteCliente(cpf);
        return ResponseEntity.ok("Cliente excluído com sucesso");
    }

    @PatchMapping("/update/{cpf}")
    public ResponseEntity<?> updateCliente ( @Valid @PathVariable String cpf,
                                             @RequestBody Map<String, Object> updates ) {
        try {
            Cliente cliente = clienteService.searchClientesByCpf(cpf);
            if(updates.containsKey("cpf")) {
                cliente.setCpf((String) updates.get("cpf"));
            }
            if(updates.containsKey("nome")) {
                cliente.setNome((String) updates.get("nome"));
            }
            if(updates.containsKey("email")) {
                cliente.setEmail((String) updates.get("email"));
            }
            if(updates.containsKey("telefone")) {
                cliente.setTelefone((String) updates.get("telefone"));
            }
            DataBinder binder = new DataBinder(cliente);
            binder.setValidator(validator);
            binder.validate();
            BindingResult result = binder.getBindingResult();
            if(result.hasErrors()) {
                Map errors = validate(result);
                    return ResponseEntity.badRequest().body(errors);
            }

            clienteService.saveClientes(cliente);

            return ResponseEntity.ok("O produto com CPF: '"+cpf+"', foi atualizado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/searchByName/{nome}")
    public ResponseEntity<?> searchByNome ( @PathVariable String nome ) {
        List<Cliente> lCliente = clienteService.searchByName(nome);
        if(!lCliente.isEmpty()) {
            return ResponseEntity.ok(lCliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
    }

    @GetMapping("/searchByEmail/{email}")
    public ResponseEntity<?> searchByEmail ( @PathVariable String email ) {
        List<Cliente> lCliente = clienteService.searchByEmail(email);
        if(!lCliente.isEmpty()) {
            return ResponseEntity.ok(lCliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
    }

    @GetMapping("/searchByPhone/{telefone}")
    public ResponseEntity<?> searchByTelefone ( @PathVariable String telefone ) {
        List<Cliente> lCliente = clienteService.searchByTelefone(telefone);
        if(!lCliente.isEmpty()) {
            return ResponseEntity.ok(lCliente);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        }
    }

    public Map<String, String> validate( BindingResult resultado ) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : resultado.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

}
