package br.com.project.controllers;

import br.com.project.enums.TransactionCategory;
import br.com.project.exceptions.InvalidInputException;
import br.com.project.models.FinancialTransaction;
import br.com.project.service.FinancialTransactionService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class FinancialTransactionController {

    private final FinancialTransactionService transactionService;

    public FinancialTransactionController(FinancialTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerTransaction(@RequestBody @Valid FinancialTransaction transaction, @RequestParam String tagName) {
        String username = getUsername();
        transactionService.registerTransaction(transaction, username, tagName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTransaction(@RequestParam String transactionId) {
        String username = getUsername();

        transactionService.deleteTransaction(UUID.fromString(transactionId), username);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/last-transaction")
//    public ResponseEntity<FinancialTransaction> getLastTransaction() {
//        String username = getUsername();
//        return ResponseEntity.ok(transactionService.getLastTransaction(username));
//    }

    @GetMapping("/by-category")
    public ResponseEntity<List<FinancialTransaction>> getAllByCategory(@RequestParam String category, @RequestParam String date) {
        String username = getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.ok(transactionService.findAllByCategory(TransactionCategory.valueOf(category), d, username));
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }

    @GetMapping("/by-id")
    public ResponseEntity<FinancialTransaction> getTransactionById(@RequestParam String id) {
        String username = getUsername();

        return ResponseEntity.ok(transactionService.getTransactionById(UUID.fromString(id), username));
    }

    @GetMapping("/all")
    public ResponseEntity<List<FinancialTransaction>> getAllTransactions(@RequestParam String date) {
        String username = getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.ok(transactionService.getAllTransactions(username, d));
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }

    @PutMapping("/edit/value")
    public ResponseEntity<?> editValue(@RequestParam BigDecimal value, @RequestParam String id) {
        String username = getUsername();

        transactionService.editValue(value, UUID.fromString(id), username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/category")
    public ResponseEntity<?> editCategory(@RequestParam String category, @RequestParam String id) {
        String username = getUsername();

        transactionService.editCategory(TransactionCategory.valueOf(category), UUID.fromString(id), username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/date")
    public ResponseEntity<?> editDate(@RequestParam String date, @RequestParam String id) {
        String username = getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            transactionService.editDate(d, UUID.fromString(id), username);
            return ResponseEntity.ok().build();
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }

    @PutMapping("/edit/description")
    public ResponseEntity<?> editDescription(@RequestParam String description, @RequestParam String id) {
        String username = getUsername();

        if(description == null || description.equals("")){
            transactionService.editDescription(null, UUID.fromString(id), username);
            return ResponseEntity.ok().build();
        }

        transactionService.editDescription(description, UUID.fromString(id), username);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/tag")
    public ResponseEntity<?> editTag(@RequestParam String transactionId, @RequestParam String tagName) {
        String username = getUsername();

        transactionService.changeTag(UUID.fromString(transactionId), tagName, username);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/by-tag")
    public ResponseEntity<List<FinancialTransaction>> findByTag(@RequestParam String tagName, @RequestParam String date) {
        String username = getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.ok(transactionService.findByTag(tagName, username, d));
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}