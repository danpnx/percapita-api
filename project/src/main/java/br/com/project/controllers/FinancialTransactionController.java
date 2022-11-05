package br.com.project.controllers;

import br.com.project.enums.TransactionCategory;
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
    public ResponseEntity<?> registerTransaction(@RequestBody @Valid FinancialTransaction transaction) {
        String username = getUsername();
        transactionService.registerTransaction(transaction, username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTransaction(@RequestParam String transactionId) {
        String username = getUsername();
        transactionService.deleteTransaction(UUID.fromString(transactionId), username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/last-transaction")
    public ResponseEntity<FinancialTransaction> getLastTransaction() throws ParseException {
        String username = getUsername();
        return ResponseEntity.ok(transactionService.getLastTransaction(username));
    }

    @GetMapping("/by-category")
    public ResponseEntity<List<FinancialTransaction>> getAllByCategory(@RequestParam String category) {
        String username = getUsername();
        return ResponseEntity.ok(transactionService.findAllByCategory(TransactionCategory.valueOf(category), username));
    }

    @GetMapping("/all")
    public ResponseEntity<List<FinancialTransaction>> getAllTransactions() {
        String username = getUsername();
        return ResponseEntity.ok(transactionService.getAllTransactions(username));
    }

    @PutMapping("/edit/value")
    public ResponseEntity<?> editValue(@RequestParam BigDecimal value, @RequestParam String id) {
        String username = getUsername();
        return ResponseEntity.status(transactionService.editValue(value, UUID.fromString(id), username)).build();
    }

    @PutMapping("/edit/category")
    public ResponseEntity<?> editCategory(@RequestParam String category, @RequestParam String id) {
        String username = getUsername();
        return ResponseEntity.status(transactionService.editCategory(TransactionCategory.valueOf(category), UUID.fromString(id), username)).build();
    }

    @PutMapping("/edit/date")
    public ResponseEntity<?> editDate(@RequestParam String date, @RequestParam String id) throws ParseException {
        String username = getUsername();

        // Parse the string to Date with help of Apache Commons DateUtils class
        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.status(transactionService.editDate(d, UUID.fromString(id), username)).build();
        } catch(ParseException e) {
            throw new ParseException("Digite uma data válida", e.getErrorOffset());
        }
    }

    @PutMapping("/edit/description")
    public ResponseEntity<?> editDescription(@RequestParam String description, @RequestParam String id) {
        String username = getUsername();

        if(description == null || description.equals("")){
            return ResponseEntity.status(transactionService.editDescription(null, UUID.fromString(id), username)).build();
        }

        return ResponseEntity.status(transactionService.editDescription(description, UUID.fromString(id), username)).build();
    }

    @PutMapping("/change-tag")
    public ResponseEntity<?> changeTag(@RequestParam String transactionId, @RequestParam String tagName) {
        String username = getUsername();

        return ResponseEntity.status(transactionService.changeTag(UUID.fromString(transactionId), tagName, username)).build();
    }

    @GetMapping("/by-tag")
    public ResponseEntity<List<FinancialTransaction>> findByTag(@RequestParam String tagName) {
        String username = getUsername();
        return ResponseEntity.ok(transactionService.findByTag(tagName, username));
    }

    @GetMapping("/by-year-month")
    public ResponseEntity<List<FinancialTransaction>> findByYearAndMonth(@RequestParam String date) throws ParseException {
        String username = getUsername();
        Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
        return ResponseEntity.ok(transactionService.findByYearAndMonth(d, username));
    }

    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    }
}