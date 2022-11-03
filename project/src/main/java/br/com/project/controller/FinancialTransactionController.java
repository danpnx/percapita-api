package br.com.project.controller;

import br.com.project.models.FinancialTransaction;
import br.com.project.service.FinancialTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class FinancialTransactionController {

    private final FinancialTransactionService transactionService;

    public FinancialTransactionController(FinancialTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerTransaction(@RequestBody @Valid FinancialTransaction transaction) {
        System.out.println("RANDOM UUID: " + UUID.randomUUID());
        transactionService.registerTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTransaction(@RequestParam String transactionId) {
        System.out.println("STRING: " + transactionId);
        transactionService.deleteTransaction(UUID.fromString(transactionId));
        return ResponseEntity.noContent().build();
    }
}