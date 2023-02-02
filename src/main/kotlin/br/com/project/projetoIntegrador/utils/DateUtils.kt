package br.com.project.projetoIntegrador.utils

import org.apache.commons.lang3.time.DateUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

/**
 * @project projetoIntegrador
 * @author Daniel Augusto on 01/02/2023
 **/

fun getLocalDate(date: String): LocalDate {
    return DateUtils.parseDate(date, "dd/MM/yyyy")
        .toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

fun getLocalDateTime(date: String): LocalDateTime {
    return LocalDateTime.of(getLocalDate(date), LocalTime.now())
}