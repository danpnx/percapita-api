package br.com.project.controllers;

import br.com.project.exceptions.InvalidInputException;
import br.com.project.service.ReportService;
import br.com.project.utils.ContextUtils;
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
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

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
