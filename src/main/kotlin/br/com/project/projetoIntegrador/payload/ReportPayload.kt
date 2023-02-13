package br.com.project.projetoIntegrador.payload

import java.math.BigDecimal

/**
 * @project projetoIntegrador
 * @author Daniel Augusto on 11/02/2023
 **/
data class ReportPayload(
    val summaryData: Map<String, BigDecimal>,
    val chartData: Map<String, BigDecimal>
)
