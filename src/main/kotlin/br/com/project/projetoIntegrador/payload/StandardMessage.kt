package br.com.project.projetoIntegrador.payload

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.Instant


class StandardMessage(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "GMT-3")
    private var timestamp: Instant,

    private var status: Int,

    private var error: String,

    private var message: String?,

    private var path: String) {
}