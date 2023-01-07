package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.service.ReportService
import br.com.project.projetoIntegrador.utils.ContextUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@RestController
@RequestMapping("/report")
class ReportController @Autowired constructor(private val reportService: ReportService) {

    @GetMapping("/payment")
    fun totalPayment(@RequestParam date: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        try {
            val date: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            return ResponseEntity.ok(reportService.totalPayment(date, username))
        } catch(e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }

    @GetMapping("/receipt")
    fun totalReceipt(@RequestParam date: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        try {
            val date: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            return ResponseEntity.ok(reportService.totalReceipt(date, username))
        } catch(e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }

    @GetMapping("/balance")
    fun totalBalance(@RequestParam date: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        try {
            val date: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            return ResponseEntity.ok(reportService.accountBalance(date, username))
        } catch(e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }

    @GetMapping("/chart")
    fun reportChart(@RequestParam date: String?): ResponseEntity<Any> {
        val username: String = ContextUtils.getUsername()
        try {
            val date: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
            return ResponseEntity.ok(reportService.reportChart(date, username))
        } catch(e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }
}