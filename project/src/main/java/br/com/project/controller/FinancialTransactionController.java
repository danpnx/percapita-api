package br.com.project.controller;

import br.com.project.enums.TransactionCategory;
import br.com.project.models.FinancialTransaction;
import br.com.project.service.FinancialTransactionService;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
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
    public ResponseEntity<?> registerTransaction(@RequestBody @Valid FinancialTransaction transaction) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        transactionService.registerTransaction(transaction, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTransaction(@RequestParam String transactionId) {
        transactionService.deleteTransaction(UUID.fromString(transactionId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<FinancialTransaction>> getAllTransactions() {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return ResponseEntity.ok(transactionService.getAllTransactions(username));
    }

    @PutMapping("/edit/value")
    public ResponseEntity<?> editValue(@RequestParam BigDecimal value, @RequestParam String id) {
        transactionService.editValue(value, UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/category")
    public ResponseEntity<?> editCategory(@RequestParam String category, @RequestParam String id) {
        transactionService.editCategory(TransactionCategory.valueOf(category), UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/date")
    public ResponseEntity<?> editDate(@RequestParam String date, @RequestParam String id) throws ParseException {
        // Parse the string to Date with help of Apache Commons DateUtils class
        Date d = DateUtils.parseDate(date, "dd/MM/yyyy");

        transactionService.editDate(d, UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/edit/description")
    public ResponseEntity<?> editDescription(@RequestParam String description, @RequestParam String id) {
        if(description == null || description.equals("")){
            transactionService.editDescription(null, UUID.fromString(id));
        }
        transactionService.editDescription(description, UUID.fromString(id));
        return ResponseEntity.ok().build();
    }
}
