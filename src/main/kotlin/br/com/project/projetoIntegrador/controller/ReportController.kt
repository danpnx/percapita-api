package br.com.project.projetoIntegrador.controller

import br.com.project.projetoIntegrador.exceptions.InvalidInputException
import br.com.project.projetoIntegrador.payload.ReportPayload
import br.com.project.projetoIntegrador.payload.StandardMessage
import br.com.project.projetoIntegrador.service.ReportService
import br.com.project.projetoIntegrador.utils.ContextUtils
import br.com.project.projetoIntegrador.utils.getLocalDate
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.text.ParseException
import java.util.*

@RestController
@RequestMapping("/report")
@Tag(name = "Report", description = "Endpoints para obter dados de relatório")
@SecurityRequirement(name = "Bearer Authentication")
class ReportController @Autowired constructor(private val reportService: ReportService) {

    @Operation(
        summary = "Relatório",
        description = "Endpoint para obter os dados de relatório",
        tags = ["Report"],
        responses = [
            ApiResponse(
                description = "OK",
                responseCode = "200",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Map::class)
                    )
                ]
            ),
            ApiResponse(
                description = "BAD REQUEST",
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = StandardMessage::class)
                    )
                ]
            )
        ]
    )
    @GetMapping
    fun getReport(): ResponseEntity<ReportPayload> {
        val username: String = ContextUtils.getUsername()

        try {
            return ResponseEntity.ok(reportService.getReport(username))
        } catch(e: ParseException) {
            throw InvalidInputException("Não foi possível converter a data")
        }
    }
//    @GetMapping("/payment")
//    fun totalPayment(@RequestParam date: String): ResponseEntity<Map<String, BigDecimal>> {
//        val username: String = ContextUtils.getUsername()
//        val dateFormatted = getLocalDate(date)
//        try {
//            return ResponseEntity.ok(reportService.totalPayment(dateFormatted, username))
//        } catch(e: ParseException) {
//            throw InvalidInputException("Não foi possível converter a data")
//        }
//    }
//
//    @Operation(
//        summary = "Total de recebimentos",
//        description = "Endpoint para obter o total de recebimentos no mês",
//        tags = ["Report"],
//        responses = [
//            ApiResponse(
//                description = "OK",
//                responseCode = "200",
//                content = [
//                    Content(
//                        mediaType = "application/json",
//                        schema = Schema(implementation = Map::class)
//                    )
//                ]
//            ),
//            ApiResponse(
//                description = "BAD REQUEST",
//                responseCode = "400",
//                content = [
//                    Content(
//                        mediaType = "application/json",
//                        schema = Schema(implementation = StandardMessage::class)
//                    )
//                ]
//            )
//        ]
//    )
//    @GetMapping("/receipt")
//    fun totalReceipt(@RequestParam date: String): ResponseEntity<Map<String, BigDecimal>> {
//        val username: String = ContextUtils.getUsername()
//        val dateFormatted = getLocalDate(date)
//        try {
//            return ResponseEntity.ok(reportService.totalReceipt(dateFormatted, username))
//        } catch(e: ParseException) {
//            throw InvalidInputException("Não foi possível converter a data")
//        }
//    }
//
//    @Operation(
//        summary = "Saldo do usuário",
//        description = "Endpoint para obter o saldo do usuário no mês",
//        tags = ["Report"],
//        responses = [
//            ApiResponse(
//                description = "OK",
//                responseCode = "200",
//                content = [
//                    Content(
//                        mediaType = "application/json",
//                        schema = Schema(implementation = Map::class)
//                    )
//                ]
//            ),
//            ApiResponse(
//                description = "BAD REQUEST",
//                responseCode = "400",
//                content = [
//                    Content(
//                        mediaType = "application/json",
//                        schema = Schema(implementation = StandardMessage::class)
//                    )
//                ]
//            )
//        ]
//    )
//    @GetMapping("/balance")
//    fun totalBalance(@RequestParam date: String): ResponseEntity<Map<String, BigDecimal>> {
//        val username: String = ContextUtils.getUsername()
//        val dateFormatted = getLocalDate(date)
//        try {
//            return ResponseEntity.ok(reportService.accountBalance(dateFormatted, username))
//        } catch(e: ParseException) {
//            throw InvalidInputException("Não foi possível converter a data")
//        }
//    }
//
//    @Operation(
//        summary = "Despesas por tag",
//        description = "Endpoint para obter as despesas por tag no mês",
//        tags = ["Report"],
//        responses = [
//            ApiResponse(
//                description = "OK",
//                responseCode = "200",
//                content = [
//                    Content(
//                        mediaType = "application/json",
//                        schema = Schema(implementation = Map::class)
//                    )
//                ]
//            ),
//            ApiResponse(
//                description = "BAD REQUEST",
//                responseCode = "400",
//                content = [
//                    Content(
//                        mediaType = "application/json",
//                        schema = Schema(implementation = StandardMessage::class)
//                    )
//                ]
//            ),
//            ApiResponse(
//                description = "BNOT FOUND",
//                responseCode = "404",
//                content = [
//                    Content(
//                        mediaType = "application/json",
//                        schema = Schema(implementation = StandardMessage::class)
//                    )
//                ]
//            )
//        ]
//    )
//    @GetMapping("/chart")
//    fun reportChart(@RequestParam date: String): ResponseEntity<Map<String, BigDecimal>> {
//        val username: String = ContextUtils.getUsername()
//        val dateFormatted = getLocalDate(date)
//        try {
//            return ResponseEntity.ok(reportService.reportChart(dateFormatted, username))
//        } catch(e: ParseException) {
//            throw InvalidInputException("Não foi possível converter a data")
//        }
//    }
}