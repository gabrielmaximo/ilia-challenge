package br.com.iliachallenge.punchTheClock.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.YearMonth

data class ReportResponseDTO(
    val month: YearMonth,
    @JsonProperty("total_hours")
    val totalHours: String,
    val missingHours: String?,
    val overtime: String?,
    val registers: List<WorkedDayDTO>
)
