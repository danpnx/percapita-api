package br.com.project.controllers;

import br.com.project.exceptions.InvalidInputException;
import br.com.project.models.ReportResponse;
import br.com.project.models.StandardMessage;
import br.com.project.service.ReportService;
import br.com.project.utils.ContextUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/report")
@Tag(name = "Relatório", description = "Controller para obter dados de relatório")
@SecurityRequirement(name = "Bearer Authentication")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "Total de pagamentos",
            description = "Endpoint para obter o total de pagamentos em um determinado mês",
            tags = {"Relatório"},
            responses = {
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponse.class)
                    ))
            }
    )
    @GetMapping("/payment")
    public ResponseEntity<?> totalPayment(@RequestParam String date) {
        String username = ContextUtils.getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.ok(reportService.totalPayment(d, username));
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }

    @Operation(
            summary = "Total de recebimentos",
            description = "Endpoint para obter o total de recebimentos em um determinado mês",
            tags = {"Relatório"},
            responses = {
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponse.class)
                    ))
            }
    )
    @GetMapping("/receipt")
    public ResponseEntity<?> totalReceipt(@RequestParam String date) {
        String username = ContextUtils.getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.ok(reportService.totalReceipt(d, username));
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }

    @Operation(
            summary = "Saldo do usuário",
            description = "Endpoint para obter o saldo do usuário em um determinado mês",
            tags = {"Relatório"},
            responses = {
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponse.class)
                    ))
            }
    )
    @GetMapping("/balance")
    public ResponseEntity<?> accountBalance(@RequestParam String date) {
        String username = ContextUtils.getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.ok(reportService.accountBalance(d, username));
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }

    @Operation(
            summary = "Despesas por tag",
            description = "Endpoint para obter as despesas por tag em um determinado mês",
            tags = {"Relatório"},
            responses = {
                    @ApiResponse(description = "BAD REQUEST", responseCode = "400", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    )),
                    @ApiResponse(description = "OK", responseCode = "200", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReportResponse.class)
                    )),
                    @ApiResponse(description = "NOT FOUND", responseCode = "404", content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StandardMessage.class)
                    ))
            }
    )
    @GetMapping("/chart")
    public ResponseEntity<?> reportChart(@RequestParam String date) {
        String username = ContextUtils.getUsername();

        try{
            Date d = DateUtils.parseDate(date, "dd/MM/yyyy");
            return ResponseEntity.ok(reportService.reportChart(d, username));
        } catch(ParseException e) {
            throw new InvalidInputException("Não foi possível converter a data");
        }
    }
}
