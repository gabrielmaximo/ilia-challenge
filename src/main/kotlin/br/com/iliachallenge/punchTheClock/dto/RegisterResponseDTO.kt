package br.com.iliachallenge.punchTheClock.dto

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class RegisterResponseDTO(
    val id: UUID,
    val date: LocalDate,
    val time: LocalTime
)