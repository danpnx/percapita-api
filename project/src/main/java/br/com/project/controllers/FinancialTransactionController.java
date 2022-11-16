package br.com.project.controllers;

import br.com.project.enums.TransactionCategory;
import br.com.project.exceptions.InvalidInputException;
import br.com.project.models.FinancialTransaction;
import br.com.project.models.StandardMessage;
import br.com.project.service.FinancialTransactionService;
import br.com.project.utils.ContextUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Transação Financeira", description = "Controller para requisições de transações financeiras")
@SecurityRequirement(name = "Bearer Authentication")
public class FinancialTransactionController {

    private final FinancialTransactionService transactionService;

    public FinancialTransactionController(FinancialTransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(
            summary = "Cadastrar transação",
            description = "Cadastro de transação financeira",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "CREATED", responseCode = "201", content = @Content)
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> registerTransaction(@RequestBody @Valid FinancialTransaction transaction, @RequestParam String tagName) {
        String username = ContextUtils.getUsername();
        transactionService.registerTransaction(transaction, username, tagName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Deletar transação",
            description = "Deletar uma transação financeira",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "FORBIDDEN", responseCode = "403", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "NO CONTENT", responseCode = "204", content = @Content)
            }
    )
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTransaction(@RequestParam String transactionId) {
        String username = ContextUtils.getUsername();

        transactionService.deleteTransaction(UUID.fromString(transactionId), username);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/last-transaction")
//    public ResponseEntity<FinancialTransaction> getLastTransaction() {
//        String username = ContextUtils.getUsername();
//        return ResponseEntity.ok(transactionService.getLastTransaction(username));
//    }

    @Operation(
            summary = "Transações por categoria",
            description = "Endpoint para buscar transações financeiras por categoria",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FinancialTransaction.class))
                    ))
            }
    )
    @GetMapping("/by-category")
    public ResponseEntity<List<FinancialTransaction>> getAllByCategory(@RequestParam String category, @RequestParam String date) {
        String username = ContextUtils.getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.ok(transactionService.findAllByCategory(TransactionCategory.valueOf(category), d, username));
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }

    @Operation(
            summary = "Transação do usuário",
            description = "Endpoint para buscar uma transação específica do usuário",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "FORBIDDEN", responseCode = "403", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FinancialTransaction.class)
                    ))
            }
    )
    @GetMapping("/by-id")
    public ResponseEntity<FinancialTransaction> getTransactionById(@RequestParam String id) {
        String username = ContextUtils.getUsername();

        return ResponseEntity.ok(transactionService.getTransactionById(UUID.fromString(id), username));
    }

    @Operation(
            summary = "Transações do usuário",
            description = "Endpoint para buscar todas as transações do usuário",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FinancialTransaction.class))
                    ))
            }
    )
    @GetMapping("/all")
    public ResponseEntity<List<FinancialTransaction>> getAllTransactions(@RequestParam String date) {
        String username = ContextUtils.getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.ok(transactionService.getAllTransactions(username, d));
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }

    @Operation(
            summary = "Editar valor",
            description = "Editar valor de uma transação",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "FORBIDDEN", responseCode = "403", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content)
            }
    )
    @PutMapping("/edit/value")
    public ResponseEntity<?> editValue(@RequestParam BigDecimal value, @RequestParam String id) {
        String username = ContextUtils.getUsername();

        transactionService.editValue(value, UUID.fromString(id), username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Editar categoria",
            description = "Editar categoria de uma transação",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "FORBIDDEN", responseCode = "403", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content)
            }
    )
    @PutMapping("/edit/category")
    public ResponseEntity<?> editCategory(@RequestParam String category, @RequestParam String id) {
        String username = ContextUtils.getUsername();

        transactionService.editCategory(TransactionCategory.valueOf(category), UUID.fromString(id), username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Editar data",
            description = "Editar data de uma transação",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "FORBIDDEN", responseCode = "403", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content)
            }
    )
    @PutMapping("/edit/date")
    public ResponseEntity<?> editDate(@RequestParam String date, @RequestParam String id) {
        String username = ContextUtils.getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            transactionService.editDate(d, UUID.fromString(id), username);
            return ResponseEntity.ok().build();
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }

    @Operation(
            summary = "Editar descrição",
            description = "Editar descrição de uma transação",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "FORBIDDEN", responseCode = "403", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content)
            }
    )
    @PutMapping("/edit/description")
    public ResponseEntity<?> editDescription(@RequestParam String description, @RequestParam String id) {
        String username = ContextUtils.getUsername();

        if(description == null || description.equals("")){
            transactionService.editDescription(null, UUID.fromString(id), username);
            return ResponseEntity.ok().build();
        }

        transactionService.editDescription(description, UUID.fromString(id), username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Editar tag",
            description = "Editar tag de uma transação",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "FORBIDDEN", responseCode = "403", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content)
            }
    )
    @PutMapping("/edit/tag")
    public ResponseEntity<?> editTag(@RequestParam String transactionId, @RequestParam String tagName) {
        String username = ContextUtils.getUsername();

        transactionService.changeTag(UUID.fromString(transactionId), tagName, username);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Transações por tag",
            description = "Endpoint para buscar todas as transações associadas a uma determinada tag",
            tags = {"Transação Financeira"},
            responses = {
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = FinancialTransaction.class))
                    ))
            }
    )
    @GetMapping("/by-tag")
    public ResponseEntity<List<FinancialTransaction>> findByTag(@RequestParam String tagName, @RequestParam String date) {
        String username = ContextUtils.getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.ok(transactionService.findByTag(tagName, username, d));
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }
}