package br.com.iliachallenge.punchTheClock.dto

import java.time.LocalDate
import java.time.LocalTime

data class WorkedDayDTO(
    val day: LocalDate,
    val hours: List<LocalTime>
)