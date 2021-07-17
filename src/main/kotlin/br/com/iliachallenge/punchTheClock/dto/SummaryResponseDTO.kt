package br.com.iliachallenge.punchTheClock.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalTime
import java.time.YearMonth

data class SummaryResponseDTO(
    val month: YearMonth,
    @JsonProperty("total_hours")
    val totalHours: LocalTime,
    val missingHours: String,
    val overtime: String,
    val registers: List<WorkedDayDTO>
)
